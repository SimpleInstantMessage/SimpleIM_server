package org.simpleim.server.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;

public final class AccountGenerator {
	private static final AtomicLong COUNTER = new AtomicLong(0);
	private static final SecureRandom random = new SecureRandom();

	// 禁止实例化
	private AccountGenerator() {}

	public static String nextId() {
		return Long.toString(COUNTER.getAndIncrement(), Character.MAX_RADIX) +
				Long.toString(System.currentTimeMillis(), Character.MAX_RADIX);
	}

	public static String generatePassword() {
		return new BigInteger(1024, random).toString(Character.MAX_RADIX);
	}
}
