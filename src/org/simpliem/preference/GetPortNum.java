package org.simpliem.preference;

import java.util.*;
import java.io.*;

public class GetPortNum {
   private String Strport;//port number of String
   private Properties prop=new Properties();
   FileInputStream fis = null;
   public GetPortNum(){
	   super();
   }
   public int getPortNubmer() throws Exception{
       try{
    	   fis=new FileInputStream("preference/preference.xml");
 		   prop.loadFromXML(fis);
 		   Strport=prop.getProperty("port");
 		   return Integer.parseInt(Strport);
       }
       finally{
    	   fis.close();
       }
   }
}
