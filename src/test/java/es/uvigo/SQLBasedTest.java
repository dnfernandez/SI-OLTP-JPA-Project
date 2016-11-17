package es.uvigo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLBasedTest {
	protected static Connection jdbcConnection;
	private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static String DB_URL = "jdbc:mysql://localhost:3306/si";
	private static String USER = "siuser";
	private static String PASS = "sipass";

	static {
		try {
			jdbcConnection = createConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	private static Connection createConnection() throws ClassNotFoundException, SQLException {
		Class.forName(JDBC_DRIVER);
		return DriverManager.getConnection(DB_URL, USER, PASS);

	}
}
