package edu.dbframework.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import edu.dbframework.parse.beans.ConnectionXMLBean;
import edu.dbframework.parse.parsers.AbstractParser;


public class ConnectionUtils {

	private static Connection connection;

	public static Connection getConnection() {

		AbstractParser parser = new AbstractParser("connectionConfig.xml");
		ConnectionXMLBean bean = (ConnectionXMLBean) parser.getBeanFromXML(ConnectionXMLBean.class);
		
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
