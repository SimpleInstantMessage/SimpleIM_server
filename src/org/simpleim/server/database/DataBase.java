package org.simpleim.server.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 我这里设想的是有两个数据库表 一个数据库表存放所有注册人的账号密码 另一个表存在线人的账号，这样就可以实现设想的功能 注册人账号存放表提供插入和检索功能
 * 在线人数表则会提供插入检索和删除功能
 */
public final class DataBase {
	private static final String KEY_PORT = "databasePort";
	private static final String KEY_IP = "databaseIP";
	private static final String KEY_DB_USER_NAME = "databaseUserName";
	private static final String KEY_DB_PASSWORD = "databasePassword";
	private static final String KEY_DB_DRIVER = "databaseDriver";
	private static final String FILE_PATH = "preference/preference.xml";
	private static final String KEY_DB = "DataBase";

	private static final Properties prop = new Properties();
	private static Connection conn;
	static {
		try (FileInputStream fis = new FileInputStream(FILE_PATH)) {
			prop.loadFromXML(fis);
			Class.forName(prop.getProperty(KEY_DB_DRIVER));
			//参数： URL，数据库用户名，数据库密码
			conn = DriverManager.getConnection("jdbc:" + prop.getProperty(KEY_DB) + "://"
					+ prop.getProperty(KEY_IP) + ":" + prop.getProperty(KEY_PORT) + "/" + "sim",

			prop.getProperty(KEY_DB_USER_NAME),

			prop.getProperty(KEY_DB_PASSWORD));
			if (!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
		} catch (FileNotFoundException e) {
			System.err.println("Fail to open preference.xml,please check your file path");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("InputStream error");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("Fail to find MySql driver:com.mysql.jdbc.Driver,check your Mysql Library");
			e.printStackTrace();
		} catch (SQLException e) {
			// 如果连接数据库异常，则视为数据库不存在的情况 系统自动配置数据库基本信息 创建数据库和数据表
			try {
				conn = DriverManager.getConnection("jdbc:" + prop.getProperty(KEY_DB) + "://" 
						+ prop.getProperty(KEY_IP) + ":" + prop.getProperty(KEY_PORT)
						+ "/?user=" + prop.getProperty(KEY_DB_USER_NAME) + "&password=" + prop.getProperty(KEY_DB_PASSWORD));
				if (!conn.isClosed())
					System.out.println("Succeeded connecting to the Database!");
				Statement sm = conn.createStatement();
				sm.executeUpdate("CREATE DATABASE sim");
				sm.executeUpdate("USE sim");
				// 如果数据库不存在的话，数据库表就不可能存在，接着创建数据库表
				String createDB = "CREATE TABLE number(id VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, PRIMARY KEY(id))";
				sm.executeUpdate(createDB);
				sm.close();
			} catch (SQLException e1) {
				System.err.println("Fail to connect to Mysql or create Table");
				//TODO 或者直接输出异常？
				//System.err.println(e);
				e1.printStackTrace();
			}
		}
	}

	private DataBase() {}

	/**
	 * 插入一个已经创建的用户信息
	 */
	public static void InsertNumberRow(String id, String password) throws SQLException {
		Statement statement = conn.createStatement();
		String sql = "INSERT INTO number VALUES ('" + id + "','" + password + "')";
		statement.executeUpdate(sql);
		statement.close();
	}

	/**
	 * query the password of the Account on the server
	 * @param id the id of the Account
	 * @return the password of the Account if Account exists, null if doesn't exist
	 * @throws SQLException
	 */
	public static String selectNumerRow(String id) throws SQLException {
		String sql = "select password from number where id='" + id + "'";
		String password = null;
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		if (rs.next()) {
			password = rs.getString("password");
			statement.close();
			return password;
		} else {
			System.err.println("用户名不存在");
			statement.close();
			return null;
		}
	}

	/**
	 * 关闭数据库的连接
	 */
	public static void CloseDataBase() throws SQLException {
		conn.close();
	}
}
