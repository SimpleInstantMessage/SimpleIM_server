package org.simpleim.server;

import java.sql.SQLException;

import org.simpleim.server.database.DataBase;
import org.simpleim.server.server.Server;
import org.simpleim.server.util.AccountGenerator;
import org.simpleim.server.util.Preference;

public class Main {
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		if(Preference.isFirstUse())
			Preference.preferenceInit();
		Runtime.getRuntime().addShutdownHook(new ExitHandler());
		new Server(Preference.getPortNumber()).run();
	}

	private static class ExitHandler extends Thread {
		public ExitHandler() {
			super("Exit Handler");
		}

		public void run() {
			System.out.println("exiting");
			Preference.setAmountOfUser(AccountGenerator.currentCounter());
			if(Preference.store())
				System.out.println("exit successfully");
			else
				System.out.println("exit unsuccessfully");
			try {
				DataBase.CloseDataBase();
			} catch (SQLException e) {
				System.err.println("Fail to close DataBase");
				e.printStackTrace();
			}
			System.out.println("exited");
		}
	}
}
