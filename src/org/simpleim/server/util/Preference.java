package org.simpleim.server.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public final class Preference {
	public static final String KEY_PORT_NUMBER = "port";
	public static final String KEY_AMOUNT_OF_USER = "amountOfUser";
	private static final String FILE_PATH = "preference/preference.xml";

	private static final Properties properties = new Properties();
	static {
		try (FileInputStream fis = new FileInputStream(FILE_PATH)) {
			properties.loadFromXML(fis);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// 禁止实例化
	private Preference() {}

	public static int getPortNumber() {
		String Strport = properties.getProperty(KEY_PORT_NUMBER);
		return Integer.parseInt(Strport);
	}

	public static long getAmountOfUser() {
		String StrAmountOfUser = properties.getProperty(KEY_AMOUNT_OF_USER);
		return Long.parseLong(StrAmountOfUser);
	}
	/**
	 * @param amountOfUser
	 * @return the previous value of the specified key in this property list, or null if it did not have one.
	 */
	public static Object setAmountOfUser(long amountOfUser) {
		return properties.setProperty(KEY_AMOUNT_OF_USER, String.valueOf(amountOfUser));
	}

	public static boolean store() {
		//TODO Can't guarantee success
		for (int i = 1; i <= 5; i++) {
			try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
				properties.storeToXML(fos, "Server");
//				properties.store(fos, "Server");
				return true;
			} catch (IOException e) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e1) {
					e.addSuppressed(e1);
				}
				e.printStackTrace();
				System.err.println("Fail to set amount of user " + i
						+ " times. Try again");
			}
		}
		return false;
	}
}
