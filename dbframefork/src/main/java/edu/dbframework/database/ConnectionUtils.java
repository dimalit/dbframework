package edu.dbframework.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import edu.dbframework.parse.beans.ConnectionBean;
import edu.dbframework.parse.parsers.GenericParser;


public class ConnectionUtils {

	private static Connection connection;

	public static Connection getConnection() {

		GenericParser parser = new GenericParser("connectionConfig.xml");
		ConnectionBean bean = (ConnectionBean) parser.getBeanFromXML(ConnectionBean.class);
		
		try {
			Class.forName(bean.getDriver());
			connection = DriverManager.getConnection(bean.getUrl(),
					bean.getUser(), bean.getPassword());
		} catch (Exception e) {
			throw new RuntimeException(
					"Exception in ConnectionUtils.getConnection", e.getCause());
		}

		return connection;
	}
	
	public static void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(
						"Exception in ConnectionUtils.closeConnection", e.getCause());
			}
		}
	}

}
