package com.pic.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ConnectionFactory {
	private static Connection connection = null;
	
	public static Connection getConnection(){
		Properties properties = new Properties();
		try {
			properties.load(ConnectionFactory.class.getResourceAsStream("jdbc.properties"));
			String username = properties.getProperty("username");
			String password = properties.getProperty("password");
			String url = properties.getProperty("url");
			String driver = properties.getProperty("driver");
			Class.forName(driver);
			connection = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	public static void close(ResultSet result, Statement statement, Connection connection){
		try {
			if(result != null) {
				result.close();
			}
			if(statement != null) {
				statement.close();
			}
			if(connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Statement statement, Connection connection){
		try {
			if(statement != null) {
				statement.close();
			}
			if(connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println(getConnection());
	}
}
