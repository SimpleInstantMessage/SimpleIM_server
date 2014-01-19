package org.simpleim.server.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;

public final class AccountGenerator {
	private static final AtomicLong COUNTER = new AtomicLong(Preference.getAmountOfUser());
	private static final SecureRandom random = new SecureRandom();

	// 禁止实例化
	private AccountGenerator() {}

	public static String nextId() {
		return Long.toString(COUNTER.incrementAndGet(), Character.MAX_RADIX) +
				Long.toString(System.currentTimeMillis(), Character.MAX_RADIX);
	}
	public static long currentCounter() {
		return COUNTER.get();
	}

	public static String generatePassword() {
		return new BigInteger(1024, random).toString(Character.MAX_RADIX);
	}
}
