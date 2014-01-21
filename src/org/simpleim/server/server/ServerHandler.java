package org.simpleim.server.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.simpleim.common.message.FailureResponse;
import org.simpleim.common.message.NewAccountOkResponse;
import org.simpleim.common.message.NewAccountRequest;
import org.simpleim.common.message.Response;
import org.simpleim.server.database.DataBase;
import org.simpleim.server.util.AccountGenerator;

public class ServerHandler extends ChannelHandlerAdapter {

	private static final Logger logger = Logger.getLogger(ServerHandler.class.getName());
    private String id;
    private String password;
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		boolean keepAlive = false;
		Response response = null;
		if(msg instanceof NewAccountRequest) {
			keepAlive = false;
			id=AccountGenerator.nextId();
			password=AccountGenerator.generatePassword();
			response = new NewAccountOkResponse()
						.setId(id)
						.setPassword(password);
			DataBase.InsertNumberRow(id, password);
		} else {
			keepAlive = false;
			response = new FailureResponse();
		}
		ChannelFuture f = ctx.write(response);
		if(!keepAlive)
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
