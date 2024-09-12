package Maks1mov.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import Maks1mov.telegram.utils.BotSpam;

public class Database {
	
	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	private Connection dbConnection;

   public Database() {
      connect();
   }

   public void connect() {
	   
      try {
    	  dbConnection = DriverManager.getConnection("JDBC:mysql://"
         		+ "localhost"
         		+ ":3306/"
         		+ "" // имя юзера
         		+ "?wait_timeout=30&autoReconnect=true&useUnicode=true&characterEncoding=utf-8", 
         		"", // название бд
         		"123"); // пароль
         
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }
   
   public void setNewDatabaseParameter(String test, String test2, String test3) {
	   
	   executeUpdate("INSERT INTO testTable (test, test2, test3) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE "
	   		+ "test=VALUES(test), "
	   		+ "test2=VALUES(test2), "
	   		+ "test3=VALUES(test3)", test, test2, test3);
   }
   
   public boolean tgIdExist(String tgid) {
	   
	  ResultSet rs = executeQuery("SELECT * FROM testTable WHERE test = ?", tgid);

      try {
    	  
         if (rs.next()) {
        	 
        	 rs.close();
             return true;
             
         } else {
        	 
             rs.close();
             return false;
         }
         
      } catch (Exception ex) {
         ex.printStackTrace();
      }

      return false;
   }
   
   public String getTest(String test) {

	   ResultSet rs = this.executeQuery("SELECT * FROM `testTable` WHERE `test`= ?", test);

	   try {

		   if (rs.next()) {

			   String appreciatedUsers = rs.getString("appreciatedUsers");
			   return appreciatedUsers;
		   }

		   rs.close();

	   } catch (SQLException ex) {
		   ex.printStackTrace();
	   }

	   return "NULL";
   }
   
   public void setTest(String test, String test2) {
	     executeUpdate("UPDATE `testTable` SET `test` = ? WHERE `test2` = ?", test, test2);
   }
   
   public void startTgSpam(int idMsg) {

       ResultSet rs = executeQuery("SELECT * FROM testTable");
       
       try {
    	   
           while (rs.next()) {
        	   
        	   String tgId = rs.getString("test");
               String premium = rs.getString("test2");
               
               if (!premium.equals("false"))
            	   return;
            	   
                   new Thread(() -> {

						try {
							
							BotSpam.sendTelegramSpamMessage(idMsg, tgId);
							
						} catch (Exception e) {}
						
					}).start();
           }
           
           rs.close();
           
       } catch (SQLException ex) {}
   }
   
	private Connection getConnection() throws SQLException {

		if (dbConnection == null || dbConnection.isClosed())
			connect();

		return dbConnection;
	}

	public ResultSet executeQuery(String s, Object... objs) {

		Callable<ResultSet> eq = () -> {

			PreparedStatement ps = getConnection().prepareStatement(s);

			for (int i = 0; i < objs.length; ++i) {
				ps.setObject(i + 1, objs[i]);
			}

			return ps.executeQuery();
		};

		try {
			return eq.call();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private void executeUpdate(String s, Object... objs) {

		executorService.execute(() -> {

			try {
				PreparedStatement ps = getConnection().prepareStatement(s);

				for (int i = 0; i < objs.length; ++i) {
					ps.setObject(i + 1, objs[i]);
				}

				ps.executeUpdate();
				ps.close();

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		});
	}
}