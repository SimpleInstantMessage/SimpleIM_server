package org.simpleim.server.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public final class Preference {
	public static final String KEY_PORT_NUMBER = "port";
	public static final String KEY_AMOUNT_OF_USER = "amountOfUser";
	private static final String FILE_PATH = "preference/preference.xml";
	private static final String KEY_PORT="databasePort";
    private static final String KEY_IP="databaseIP";
    private static final String KEY_DB_USER_NAME="databaseUserName";
    private static final String KEY_DB_PASSWORD="databasePassword";
    private static final String KEY_DB_DRIVER="databaseDriver";
    private static final String KEY_DB="DataBase";

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
	
	public static void preferenceInit(){
		@SuppressWarnings("resource")
		Scanner scanner=new Scanner(System.in);
		System.out.println("--------第一次使用SimpleIM，首先需要配置一下基本信息--------");
		System.out.print("SimpleIm的服务端口号(1024~65535):");
		properties.setProperty(KEY_PORT_NUMBER, scanner.next());
		System.out.print("数据库所在的IP地址:");
		properties.setProperty(KEY_IP,scanner.next());
		System.out.print("数据库的端口号(1024~65535):");
		properties.setProperty(KEY_PORT, scanner.next());
		System.out.println("数据库类型<输入你使用的数据库序列号>");
		System.out.println("	1.oracle");
		System.out.println("	2.mysql");
		System.out.println("	3.sqlserver");
		System.out.println("	4.PostgreSQL ");
		System.out.print("选择数据库(目前仅支持mysql):");
		switch(scanner.next()){
		      case "1":properties.setProperty(KEY_DB_DRIVER, null);
		               properties.setProperty(KEY_DB, null);
		               break;
		      case "2":properties.setProperty(KEY_DB_DRIVER, "com.mysql.jdbc.Driver");
                       properties.setProperty(KEY_DB, "mysql");
                       break;
		      case "3":properties.setProperty(KEY_DB_DRIVER, null);
                       properties.setProperty(KEY_DB, null);
                       break;
		      case "4":properties.setProperty(KEY_DB_DRIVER, null);
                       properties.setProperty(KEY_DB, null);
                       break;
		}
		System.out.print("数据库用户名:");
		properties.setProperty(KEY_DB_USER_NAME, scanner.next());
		System.out.print("数据库密码:");
		properties.setProperty(KEY_DB_PASSWORD, scanner.next());
		properties.setProperty("status", "initialized");
		try(FileOutputStream fos= new FileOutputStream(FILE_PATH) ){
			properties.storeToXML(fos, "Server");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static boolean isFirstUse(){
		if(properties.getProperty("status").equals("initialized"))
			return false;
		else 
			return true;
	}

	public static boolean store() {
		//TODO Can't guarantee success
		for (int i = 1; i <= 5; i++) {
			try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
				properties.storeToXML(fos, "Server");
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
