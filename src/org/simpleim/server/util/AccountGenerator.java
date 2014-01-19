package org.simpleim.server.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;

import org.simpliem.preference.AmountOfUser;

public final class AccountGenerator {
	@SuppressWarnings("unused")
	private static final AtomicLong COUNTER = new AtomicLong(0);
	private static final SecureRandom random = new SecureRandom();
	private static Long amountOfUser;//用户的数量

	// 禁止实例化
	private AccountGenerator() {}
	/**
	 * 为了使用的时候比较方便，我在配置文件中将用户数量初始化为10000
	 * 当统计用户数量的时候，应该减去这一数字
	 * 另外这个方法是原来nextId的重写，方法名并没有改变
	 * @throws Exception 
	 * */
    public static String nextId() throws Exception{
    	try{
    		amountOfUser=new AmountOfUser().getAmountOfUser();
    		return Long.toString(amountOfUser, Character.MAX_RADIX) +
    				Long.toString(System.currentTimeMillis(), Character.MAX_RADIX);
    	}
    	finally{
    		new AmountOfUser().setAmountOfUser(amountOfUser);
    	}
    }
    /**
	public static String nextId() {
		return Long.toString(COUNTER.getAndIncrement(), Character.MAX_RADIX) +
				Long.toString(System.currentTimeMillis(), Character.MAX_RADIX);
	}
    */
	public static String generatePassword() {
		return new BigInteger(1024, random).toString(Character.MAX_RADIX);
	}
}
