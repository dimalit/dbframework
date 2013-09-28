package edu.dbframework.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.dbframework.parse.beans.DatabaseXMLBean;
import edu.dbframework.parse.beans.items.ColumnItem;
import edu.dbframework.parse.beans.items.TableItem;


/**
 * 
 * @author GreenPoser
 * 
 */
public class MetadataDao {

	public final static String TABLE_TYPE = "TABLE";
	public final static String VIEW_TYPE = "VIEW";
	public final static String COLUMN = "COLUMN_NAME";

	public MetadataDao() {
	}

	public List<String> getTables() {
		ResultSet resultSet = null;
		List<String> tables = null;
		Connection connection = null;

		try {
			connection = ConnectionUtils.getConnection();
			String[] types = new String[1];
			types[0] = TABLE_TYPE;
			resultSet = connection.getMetaData().getTables(null, null, null,
					types);
			tables = new ArrayList<String>();
			while (resultSet.next()) {
				tables.add(resultSet.getString("TABLE_NAME"));
			}
		} catch (Exception e) {
			throw new RuntimeException("Exception in MetadataDao.getTables",
					e.getCause());
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) {
				throw new RuntimeException(
						"Exception in MetadataDao.getTables", e.getCause());
			}
			ConnectionUtils.closeConnection();
		}

		return tables;
	}

	public List<String> getViews() {
		ResultSet resultSet = null;
		List<String> views = null;
		Connection connection = null;

		try {
			connection = ConnectionUtils.getConnection();
			String[] types = new String[1];
			types[0] = VIEW_TYPE;
			resultSet = connection.getMetaData().getTables(null, null, null,
					types);
			views = new ArrayList<String>();
			while (resultSet.next()) {
				views.add(resultSet.getString("TABLE_NAME"));
			}
		} catch (Exception e) {
			throw new RuntimeException("Exception in MetadataDao.getViews", e.getCause());
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) {
				throw new RuntimeException(
						"Exception in MetadataDao.getViews", e.getCause());
			}
			ConnectionUtils.closeConnection();
		}
		return views;
	}

	public List<String> getColumns(String table) {
		ResultSet resultSet = null;
		List<String> columns = null;
		Connection connection = null;

		try {
			connection = ConnectionUtils.getConnection();
			resultSet = connection.getMetaData().getColumns(null, null, table,
					null);
			columns = new ArrayList<String>();
			while (resultSet.next()) {
				columns.add(resultSet.getString(COLUMN));
			}
		} catch (SQLException e) {
			throw new RuntimeException("Exception in MetadataDao.getViews", e.getCause());
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) {
				throw new RuntimeException(
						"Exception in MetadataDao.getColumns", e.getCause());
			}
			ConnectionUtils.closeConnection();
		}
		return columns;
	}
	
	public DatabaseXMLBean createTablesXMLBean() {
		DatabaseXMLBean xmlBean = new DatabaseXMLBean();
		List<TableItem> tables = new ArrayList<TableItem>();
		for (String table : getTables()) {
            List<String> primaryKeys = this.getPrimaryKeyColumns(table);
			TableItem item = new TableItem();
			item.setName(table);
			List<ColumnItem> columns = new ArrayList<ColumnItem>();
			for (String column : getColumns(table)) {
				ColumnItem columnItem = new ColumnItem();
				columnItem.setName(column);
                for (String primaryKey : primaryKeys) {
                    if (column.equals(primaryKey))
                        columnItem.setPrimaryKey(true);
                }
				columns.add(columnItem);
			}
			item.setColumns(columns);
			tables.add(item);
		}
		xmlBean.setTables(tables);
		return xmlBean;
	}
	
	public List<String> getPrimaryKeyColumns(String table) {
		ResultSet resultSet = null;
		List<String> primaryKeyColumns = null;
		Connection connection = null;
		try {
			connection = ConnectionUtils.getConnection();
			resultSet = connection.getMetaData().getPrimaryKeys(null, null,
					table);
			primaryKeyColumns = new ArrayList<String>();
			while (resultSet.next()) {
				primaryKeyColumns.add(resultSet.getString(COLUMN));
			}
		} catch (SQLException e) {
			throw new RuntimeException(
					"Exception in MetadataDao.getPrimaryKeyColumns", e.getCause());
		} finally {
			ConnectionUtils.closeConnection();
			try {
				resultSet.close();
			} catch (SQLException e) {
				throw new RuntimeException(
						"Exception in MetadataDao.getPrimaryKeyColumns", e.getCause());
			}
		}
		return primaryKeyColumns;
	}
}
