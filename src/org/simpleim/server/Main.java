package org.simpleim.server;

import org.simpleim.server.server.Server;
import org.simpliem.preference.GetPortNum;


public class Main {
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		int port;
		port=new GetPortNum().getPortNubmer();
		new Server(port).run();
		/**
		 * 两种句式任选一种
		 */
		//new Server(new GetPortNum().getPortNubmer()).run();
	
	}

}
