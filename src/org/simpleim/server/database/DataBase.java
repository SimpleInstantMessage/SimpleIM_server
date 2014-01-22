package org.simpleim.server.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;
import java.sql.*;
/**
 * 我这里设想的是有两个数据库表
 * 一个数据库表存放所有注册人的账号密码
 * 另一个表存在线人的账号，这样就可以实现设想的功能
 * 注册人账号存放表提供插入和检索功能
 * 在线人数表则会提供插入检索和删除功能
 * */
public final class DataBase {
    private static final String KEY_PORT="databasePort";
    private static final String KEY_IP="databaseIP";
    private static final String KEY_DB_USER_NAME="databaseUserName";
    private static final String KEY_DB_PASSWORD="databasePassword";
    private static final String KEY_DB_DRIVER="databaseDriver";
    private static final String FILE_PATH = "preference/preference.xml";
    private static final String KEY_DB_NAME="databaseName";
    private static final String KEY_TABLE_NAME="databaseTableNumberName";
    
    private static final Properties prop = new Properties();
    private static Connection conn;
    static{
    	try(FileInputStream fis=new FileInputStream(FILE_PATH)){
    		prop.loadFromXML(fis);
    		Class.forName(prop.getProperty(KEY_DB_DRIVER));
    		/**
    		 * @参数 url，数据库用户名，数据库密码
    		 */
    		conn=DriverManager.getConnection("jdbc:mysql://"+prop.getProperty(KEY_IP)+":"+prop.getProperty(KEY_PORT)+"/"+prop.getProperty(KEY_DB_NAME), 
    				                         prop.getProperty(KEY_DB_USER_NAME), 
    				                         prop.getProperty(KEY_DB_PASSWORD));
    		if(!conn.isClosed())
               System.out.println("Succeeded connecting to the Database!");
    	} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    private DataBase(){}
    
    public static void InsertNumberRow(String id,String password) throws SQLException{
    	Statement statement=conn.createStatement();
    	String sql="INSERT INTO "+prop.getProperty(KEY_TABLE_NAME)
    			   +" VALUES ('"+id+"','"+password+"')";
    	statement.executeUpdate(sql);
    }
    
    public static String selectNumerRow(String id) throws SQLException{
    	String sql="select password from "+prop.getProperty(KEY_TABLE_NAME)+" where id='"+id+"'";
    	String password = null;
    	Statement statement=conn.createStatement();
    	ResultSet rs=statement.executeQuery(sql);
    	if(rs.next()){
    	   password=rs.getString("password");
    	   rs.close();
    	   return password;
    	}
    	else{
    		System.err.println("用户名不存在");
    		rs.close();
    		return null;
    	}
    	
    }
     
    public static LinkedList<String> selectOnlineNumber(){
    	LinkedList<String> id = null;
    	return id;
    }
    public static void deleteOfflineNumber(){
    	
    }
    
    public static void insertOnlineNumber(String id){
    	
    }
    public static void CloseDataBase() throws SQLException{
    	conn.close();
    }
}
