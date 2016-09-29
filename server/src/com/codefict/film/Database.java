package com.codefict.film;

import java.sql.*;

public class Database {
	private Connection conn = null;
	 
	public Database(String url, String user_name, String password) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
 
			this.conn = DriverManager.getConnection(url, user_name, password);
			
			// AutoCommitEnable
			this.conn.setAutoCommit(true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
 
	public Connection getConnection() {
		return this.conn;
	}
 
	public ResultSet runSql(String sql) throws SQLException {
		Statement sta = conn.createStatement();
		return sta.executeQuery(sql);
	}
}
