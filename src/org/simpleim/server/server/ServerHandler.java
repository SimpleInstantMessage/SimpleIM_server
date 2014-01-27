package org.simpleim.server.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.simpleim.common.message.ChatMessage;
import org.simpleim.common.message.FailureResponse;
import org.simpleim.common.message.LoginFailureResponse;
import org.simpleim.common.message.LoginFailureResponse.Cause;
import org.simpleim.common.message.LoginNotification;
import org.simpleim.common.message.LoginOkResponse;
import org.simpleim.common.message.LoginRequest;
import org.simpleim.common.message.LogoutNotification;
import org.simpleim.common.message.LogoutRequest;
import org.simpleim.common.message.NewAccountOkResponse;
import org.simpleim.common.message.NewAccountRequest;
import org.simpleim.common.message.Notification;
import org.simpleim.common.message.ReceiveMessageNotification;
import org.simpleim.common.message.Response;
import org.simpleim.common.message.SendMessageRequest;
import org.simpleim.common.message.UpdateFinishedNotification;
import org.simpleim.common.message.User;
import org.simpleim.common.message.sendMessageFinishedNotification;
import org.simpleim.server.database.DataBase;
import org.simpleim.server.util.AccountGenerator;

import com.lambdaworks.crypto.SCryptUtil;

public class ServerHandler extends ChannelHandlerAdapter {

	private static final Logger logger = Logger.getLogger(ServerHandler.class.getName());
	private static ConcurrentHashMap<String, ChannelHandlerContext> onlineUsers = new ConcurrentHashMap<>();
	private String UserId;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		boolean closeNow = true;
		Response response = null;
		Notification notification = null;
		String id;
		String password;
		/**********************************************************************************************************
		 * @see 用户注册部分 返回用户注册信息
		 * */
		if(msg instanceof NewAccountRequest) {
			closeNow = true;
			id=AccountGenerator.nextId();
			password=AccountGenerator.generatePassword();
			response = new NewAccountOkResponse()
						.setId(id)
						.setPassword(password);
			String hashedPassword = SCryptUtil.scrypt(password, 1 << 15, 8, 1);
			DataBase.InsertNumberRow(id, hashedPassword);
			/*********************************************************************************************************
			 * @see 登陆部分
			 * */
		} else if(msg instanceof LoginRequest){
			closeNow = false;
			LoginRequest request = (LoginRequest) msg;
			id = request.getId();
			password = request.getPassword();
			if (DataBase.selectNumerRow(id).equals(password)) {
				// TODO 处理 返回值为null 的情况
				response = new LoginOkResponse().setOnlineUsersIds(onlineUsers.keySet().toArray(new String[0]));
				UserId = id;
			} else if (DataBase.selectNumerRow(id).equals(null))
				response = new LoginFailureResponse().setCause(Cause.ID_NOT_FOUND);
			else
				response = new LoginFailureResponse().setCause(Cause.PASSWORD_INCORRECT);
		} else if (msg instanceof UpdateFinishedNotification) {
			closeNow = false;
			Iterator<ChannelHandlerContext> onlineNumbers = onlineUsers.values().iterator();
			notification = new LoginNotification().setNewUserId(UserId);
			while (onlineNumbers.hasNext())
				onlineNumbers.next().write(notification);
			onlineUsers.put(UserId, ctx);
		}
		/*********************************************************************************************************
		 * @see 转发部分
		 * */
		else if (msg instanceof SendMessageRequest) {
			closeNow = false;
			// 消息发送方
			User sender = ((SendMessageRequest) msg).getSender();
			// 消息内容
			ChatMessage message = ((SendMessageRequest) msg).getMessage();
			// 消息接收方
			String[] targetsIds = ((SendMessageRequest) msg).getTargetsIds();
			// 转发的消息内容
			notification = new ReceiveMessageNotification().setMessage(message).setSender(sender);
			// 消息转发
			for (String targetId : targetsIds)
				onlineUsers.get(targetId).write(notification);
			// 设置转发完成确认
			notification = new sendMessageFinishedNotification();
		}
		/*******************************************************************************************************
		 * @see 登出部分
		 * */
		else if (msg instanceof LogoutRequest) {
			closeNow = true;
			onlineUsers.remove(UserId);
			Iterator<ChannelHandlerContext> onlineNumbers = onlineUsers.values().iterator();
			notification = new LogoutNotification().setUserLoggedOutId(UserId);
			while (onlineNumbers.hasNext())
				onlineNumbers.next().write(notification);
		} else {
		/*******************************************************************************************************
 *
 * */
			closeNow = true;
			response = new FailureResponse();
		}
		ChannelFuture f = ctx.write(response);
		if(closeNow)
			f.addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		logger.log(Level.WARNING, "Unexpected exception from downstream.", cause);
		ctx.close();
	}
}
