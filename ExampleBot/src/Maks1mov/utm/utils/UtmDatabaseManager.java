package Maks1mov.utm.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import Maks1mov.utm.UtmStorage;
import Maks1mov.utm.objects.UtmTag;

public class UtmDatabaseManager {

	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	private Connection dbConnection;
	private UtmStorage storage;
	
	public UtmDatabaseManager(UtmStorage storage) {

		this.storage = storage;
		connectToDatabase();
	}
	
	private void connectToDatabase() {

		  try {
			  dbConnection = DriverManager.getConnection("JDBC:mysql://"
	         		+ "localhost"
	         		+ ":3306/"
	         		+ "" // имя юзера
	         		+ "?wait_timeout=30&autoReconnect=true&useUnicode=true&characterEncoding=utf-8", 
	         		"", // название бд
	         		"123"); // пароль
	         
			// создаем таблицу если надо
			  if(!doesUtmStorageTableExist()) {

			      System.out.println("Creating table...");

			      executeUpdate("CREATE TABLE `utmStorage` (\r\n"
			              + "  `id` int(11) NOT NULL,\r\n"
			              + "  `trafficId` int(11) NOT NULL,\r\n"
			              + "  `trafficName` text NOT NULL,\r\n"
			              + "  `forToday` int(11) NOT NULL,\r\n"
			              + "  `forYesterday` int(11) NOT NULL,\r\n"
			              + "  `forWeek` int(11) NOT NULL,\r\n"
			              + "  `forMonth` int(11) NOT NULL,\r\n"
			              + "  `total` int(11) NOT NULL,\r\n"
			              + "  `payments` int(11) NOT NULL\r\n"
			              + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;");

			      executeUpdate("INSERT INTO `utmStorage` (`id`, `trafficId`, `trafficName`, `forToday`, `forYesterday`, `forWeek`, `forMonth`, `total`, `payments`) VALUES\r\n"
			              + "(1, 777, 'totalJoins', 0, 0, 0, 0, 0, 0);");

			      executeUpdate("ALTER TABLE `utmStorage`\r\n"
			              + "  ADD PRIMARY KEY (`id`);");

			      executeUpdate("ALTER TABLE `utmStorage`\r\n"
			              + "  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;");
			  }
			  
			  loadAllUtms(false);
	        
	      } catch (Exception ex) {
	        ex.printStackTrace();
	      }
	}
	
    public boolean doesUtmStorageTableExist() {
    	
        try {
            DatabaseMetaData metaData = dbConnection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, "utmStorage", null);
            
            return resultSet.next();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
	
	void loadAllUtms(boolean clearUtmTagsInArraylist) {
		
		if(clearUtmTagsInArraylist)
			storage.getAllUtmTags().clear();
		
	    String query = "SELECT trafficId, trafficName, forToday, forYesterday, forWeek, forMonth, total, payments FROM utmStorage";
	    
	    try {
	        ResultSet rs = executeQuery(query);
	        
	        while (rs.next()) {
	        	
	        	storage.getAllUtmTags().add(new UtmTag(
	        			
	                rs.getInt("trafficId"),
	                rs.getString("trafficName"),
	                rs.getInt("forToday"),
	                rs.getInt("forYesterday"),
	                rs.getInt("forWeek"),
	                rs.getInt("forMonth"),
	                rs.getInt("total"),
	                rs.getInt("payments")
	            ));
	        }

	        rs.close();

	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}

	public void addNewJoinToUTM(int trafficId, String trafficName) {
		
		if(utmTagExist(trafficId)) {
			 
			setUtmTraffic(trafficId, trafficName);
			
			setUtmJoinsForToday(trafficId, getUtmJoinsforToday(trafficId) + 1);
			setUtmJoinsForWeek(trafficId, getUtmJoinsforWeek(trafficId) + 1);
			setUtmJoinsForMonth(trafficId, getUtmJoinsforMonth(trafficId) + 1);
			setUtmJoinsTotal(trafficId, getUtmJoinsTotal(trafficId) + 1);
		}
		
		else setNewUTMtagToDatabase(trafficId, trafficName, 1, 0, 1, 1, 1, 0);
		
		setAllUtmJoinsTotal(getAllUtmJoinsTotal() + 1);
		setAllUtmJoinsForToday(getAllUtmJoinsforToday() + 1);
		setAllUtmJoinsForWeek(getAllUtmJoinsforWeek() + 1);
		setAllUtmJoinsForMonth(getAllUtmJoinsforMonth() + 1);
		
		loadAllUtms(true);
	}
	
	protected void setNewUTMtagToDatabase(int trafficId, String trafficName, int forToday, int forYesterday, int forWeek, int forMonth, int total, int payments) {
		
		executeUpdate("INSERT INTO utmStorage "
				+ "(trafficId, trafficName, forToday, forYesterday, forWeek, forMonth, total, payments)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE "
				+ "trafficId=VALUES(trafficId), "
				+ "trafficName=VALUES(trafficName), "
				+ "forToday=VALUES(forToday), "
				+ "forYesterday=VALUES(forYesterday), "
				+ "forWeek=VALUES(forWeek), "
				+ "forMonth=VALUES(forMonth), " 
				+ "total=VALUES(total), "
				+ "payments=VALUES(payments);", 
				trafficId, trafficName, forToday, forYesterday, forWeek, forMonth, total, payments);
		
		loadAllUtms(true);
	}
	
	public void deleteUtmTagById(int id) {
	    String query = "DELETE FROM utmStorage WHERE trafficId = ?";
	    executeUpdate(query, id);
	    
	    loadAllUtms(true);
	}
	
	protected void setUtmTraffic(int id, String value) {
		executeUpdate("UPDATE `utmStorage` SET `trafficName` = ? WHERE `trafficId` = ?", value, id);
		loadAllUtms(true);
	}
	
	protected String getUtmTraffic(int id) {

		ResultSet rs = executeQuery("SELECT * FROM `utmStorage` WHERE `trafficId`= ?", id);

		try {
			if (rs.next())
				return rs.getString("trafficName");

			rs.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return null;
	}
	
	protected void setAllUtmJoinsForMonth(int value) {
		executeUpdate("UPDATE `utmStorage` SET `forMonth` = ? WHERE `trafficId` = 777", value);
		loadAllUtms(true);
	}
	
	protected int getAllUtmJoinsforMonth() {

		ResultSet rs = executeQuery("SELECT * FROM `utmStorage` WHERE `trafficId`= ?", 777);

		try {
			if (rs.next())
				return rs.getInt("forMonth");

			rs.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return 0;
	}
	
	protected void setAllUtmPayments(int value) {
		executeUpdate("UPDATE `utmStorage` SET `payments` = ? WHERE `trafficId` = ?", value, 777);
		loadAllUtms(true);
	}
	
	protected int getAllUtmPayments() {

		ResultSet rs = executeQuery("SELECT * FROM `utmStorage` WHERE `trafficId`= ?", 777);

		try {
			if (rs.next())
				return rs.getInt("payments");

			rs.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return 0;
	}
	
	protected void setAllUtmJoinsTotal(int value) {
		executeUpdate("UPDATE `utmStorage` SET `total` = ? WHERE `trafficId` = ?", value, 777);
		loadAllUtms(true);
	}
	
	protected int getAllUtmJoinsTotal() {

		ResultSet rs = executeQuery("SELECT * FROM `utmStorage` WHERE `trafficId`= ?", 777);

		try {
			if (rs.next())
				return rs.getInt("total");

			rs.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return 0;
	}
	
	protected void setAllUtmJoinsForWeek(int value) {
		executeUpdate("UPDATE `utmStorage` SET `forWeek` = ? WHERE `trafficId` = ?", value, 777);
		loadAllUtms(true);
	}
	
	protected int getAllUtmJoinsforWeek() {

		ResultSet rs = executeQuery("SELECT * FROM `utmStorage` WHERE `trafficId`= ?", 777);

		try {
			if (rs.next())
				return rs.getInt("forWeek");

			rs.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return 0;
	}
	
	protected void setAllUtmJoinsForToday(int value) {
		executeUpdate("UPDATE `utmStorage` SET `forToday` = ? WHERE `trafficId` = ?", value, 777);
		loadAllUtms(true);
	}
	
	protected int getAllUtmJoinsforToday() {

		ResultSet rs = executeQuery("SELECT * FROM `utmStorage` WHERE `trafficId`= ?", 777);

		try {
			if (rs.next())
				return rs.getInt("forToday");

			rs.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return 0;
	}
	
	protected void setAllUtmJoinsForYesterday(int value) {
		executeUpdate("UPDATE `utmStorage` SET `forYesterday` = ? WHERE `trafficId` = ?", value, 777);
		loadAllUtms(true);
	}
	
	protected int getAllUtmJoinsforYesterday() {

		ResultSet rs = executeQuery("SELECT * FROM `utmStorage` WHERE `trafficId`= ?", 777);

		try {
			if (rs.next())
				return rs.getInt("forYesterday");

			rs.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return 0;
	}
	
	protected void setUtmPayments(int id, int value) {
		executeUpdate("UPDATE `utmStorage` SET `payments` = ? WHERE `trafficId` = ?", value, id);
		loadAllUtms(true);
	}
	
	protected int getUtmPayments(int id) {

		ResultSet rs = executeQuery("SELECT * FROM `utmStorage` WHERE `trafficId`= ?", id);

		try {
			if (rs.next())
				return rs.getInt("payments");

			rs.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return 0;
	}
	
	protected void setUtmJoinsForMonth(int id, int value) {
		executeUpdate("UPDATE `utmStorage` SET `forMonth` = ? WHERE `trafficId` = ?", value, id);
		loadAllUtms(true);
	}
	
	protected int getUtmJoinsforMonth(int id) {

		ResultSet rs = executeQuery("SELECT * FROM `utmStorage` WHERE `trafficId`= ?", id);

		try {
			if (rs.next())
				return rs.getInt("forMonth");

			rs.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return 0;
	}
	
	protected void setUtmJoinsForWeek(int id, int value) {
		executeUpdate("UPDATE `utmStorage` SET `forWeek` = ? WHERE `trafficId` = ?", value, id);
		loadAllUtms(true);
	}
	
	protected int getUtmJoinsforWeek(int id) {

		ResultSet rs = executeQuery("SELECT * FROM `utmStorage` WHERE `trafficId`= ?", id);

		try {
			if (rs.next())
				return rs.getInt("forWeek");

			rs.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return 0;
	}
	
	protected void setUtmJoinsForToday(int id, int value) {
		executeUpdate("UPDATE `utmStorage` SET `forToday` = ? WHERE `trafficId` = ?", value, id);
		loadAllUtms(true);
	}
	
	protected int getUtmJoinsforToday(int id) {

		ResultSet rs = executeQuery("SELECT * FROM `utmStorage` WHERE `trafficId`= ?", id);

		try {
			if (rs.next())
				return rs.getInt("forToday");

			rs.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return 0;
	}
	
	protected void setUtmJoinsForYesterday(int id, int value) {
		executeUpdate("UPDATE `utmStorage` SET `forYesterday` = ? WHERE `trafficId` = ?", value, id);
		loadAllUtms(true);
	}
	
	protected int getUtmJoinsforYesterday(int id) {

		ResultSet rs = executeQuery("SELECT * FROM `utmStorage` WHERE `trafficId`= ?", id);

		try {
			if (rs.next())
				return rs.getInt("forYesterday");

			rs.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return 0;
	}
	
	protected void setUtmJoinsTotal(int id, int value) {
		executeUpdate("UPDATE `utmStorage` SET `total` = ? WHERE `trafficId` = ?", value, id);
		loadAllUtms(true);
	}
	
	protected int getUtmJoinsTotal(int id) {

		ResultSet rs = executeQuery("SELECT * FROM `utmStorage` WHERE `trafficId`= ?", id);

		try {
			if (rs.next())
				return rs.getInt("total");

			rs.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return 0;
	}
	
	private boolean utmTagExist(int utmId) {

		ResultSet rs = executeQuery("SELECT * FROM utmStorage WHERE trafficId = ?", utmId);

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

	private Connection getConnection() throws SQLException {

		if (dbConnection == null || dbConnection.isClosed())
			connectToDatabase();

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
