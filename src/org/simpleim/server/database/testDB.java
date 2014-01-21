package org.simpleim.server.database;

import java.sql.SQLException;

public class testDB {
   public static void main(String args[]) throws SQLException{
	   DataBase.InsertNumberRow("111","sss");
	   System.out.println(DataBase.selectNumerRow("111"));
   }
}
