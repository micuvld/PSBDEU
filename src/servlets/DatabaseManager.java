package servlets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
	final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	final String DB_URL = "jdbc:oracle:thin:@localhost:1521/orcl";

	// Database credentials
	final String USER = "c##proiect";
	final String PASS = "psbd";

	private static DatabaseManager instance = null;
	private DatabaseManager() {
		initialize();
	}
	
	private Connection connection;

	private void initialize() {
		try {
			Class.forName(JDBC_DRIVER);

			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public static DatabaseManager getInstance() {
		if (instance == null) {
			instance = new DatabaseManager();
		}
		
		return instance;
	}
}
