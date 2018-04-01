package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class BaseDao {
	String URL;
	public BaseDao() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			URL = "jdbc:sqlserver://localhost:1433;DatabaseName=BankManagement";
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("cannot find driver\n");
			e.printStackTrace();
		}
	}
	public Connection getConnection() throws Exception {
		return DriverManager.getConnection(URL, "sa", "123");
	}
	
}
