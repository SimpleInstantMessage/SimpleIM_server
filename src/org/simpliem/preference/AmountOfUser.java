package org.simpliem.preference;

import java.util.*;
import java.io.*;

public class AmountOfUser {
  private String StrAmountOfUser;//AmountOfNumber of String type
  private Properties prop=new Properties();
  private FileInputStream fis=null;
  private FileOutputStream fos=null;
  public AmountOfUser(){
	  super();
  }
  public Long getAmountOfUser() throws Exception{
	 try{
	  fis=new FileInputStream("preference/preference.xml");
	  prop.load(fis);
	  StrAmountOfUser=prop.getProperty("amountOfUser");
	  return Long.parseLong(StrAmountOfUser);
	}
	 finally{
		fis.close(); 
	 }
  }
  public void setAmountOfUser(Long AmountOfUser) throws Exception{
	  fos=new FileOutputStream("preference/preference.xml");
	  prop.setProperty("amountOfUser", (AmountOfUser++).toString());
	  prop.store(fos, "Server");
	  fos.close();
  }
}
