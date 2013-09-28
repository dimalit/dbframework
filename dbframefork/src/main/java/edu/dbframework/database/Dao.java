package edu.dbframework.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.dbframework.parse.beans.items.ColumnItem;
import edu.dbframework.parse.beans.items.TableItem;
import edu.dbframework.parse.helpers.DatabaseBeanHelper;


public class Dao {

    private final SqlQueryBuilder sqlQueryBuilder = new SqlQueryBuilder();

    public Dao() {
    }

    public Map<String, List<String>> getData(String table) {
        ResultSet resultSet = null;
        Statement statement = null;
        Connection connection = null;
        Map<String, List<String>> data = null;
        MetadataDao utils = new MetadataDao();
        List<String> columns = utils.getColumns(table);

        StringBuffer query = new StringBuffer();
        query.append("SELECT ");
        for (int i = 0; i < columns.size() - 1; i++) {
            query.append(columns.get(i));
            query.append(", ");
        }
        query.append(columns.get(columns.size() - 1) + " ");
        query.append("FROM " + table);

        try {
            connection = ConnectionUtils.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query.toString());
            data = new HashMap<String, List<String>>();
            for (String column : columns) {
                List<String> tempData = new ArrayList<String>();
                while (resultSet.next()) {
                    tempData.add(resultSet.getString(column));
                }
                data.put(column, tempData);
                resultSet.beforeFirst();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Exception in Dao.getData(table)",
                    e.getCause());
        } finally {
            try {
                statement.close();
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(
                        "Exception in Dao.getData(table)", e.getCause());
            }
            ConnectionUtils.closeConnection();
        }
        return data;
    }

    public Map<String, List<String>> getData(TableItem tableItem) {
        ResultSet resultSet = null;
        Statement statement = null;
        Connection connection = null;
        Map<String, List<String>> data = null;
        String query = sqlQueryBuilder.createQuery(tableItem);
        try {
            connection = ConnectionUtils.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query.toString());
            data = new HashMap<String, List<String>>();
            for (ColumnItem columnItem : tableItem.getColumns()) {
                List<String> tempData = new ArrayList<String>();
                while (resultSet.next()) {
                    tempData.add(resultSet.getString(columnItem.getName()));
                }
                if (columnItem.getAlias() != null && !columnItem.getAlias().equals(""))
                    data.put(columnItem.getAlias(), tempData);
                else
                    data.put(columnItem.getName(), tempData);
                resultSet.beforeFirst();
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Exception in Dao.getData(tableItem)", e.getCause());
        }
        return data;
    }

    public Map<String, List<String>> getData(TableItem tableItem, String primaryKey, String indexColumn) {
        ResultSet resultSet = null;
        Statement statement = null;
        Connection connection = null;
        Map<String, List<String>> data = null;
        String query = sqlQueryBuilder.createQuery(tableItem, primaryKey, indexColumn);
        try {
            connection = ConnectionUtils.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query.toString());
            data = new HashMap<String, List<String>>();

            List<ColumnItem> columnItems = tableItem.getColumns();

            DatabaseBeanHelper databaseBeanHelper = new DatabaseBeanHelper();
            Map<String, ColumnItem> extRefTables = databaseBeanHelper.getExternalReferencesForTable(tableItem.getName());

            if (extRefTables != null) {
                for (String table : extRefTables.keySet()) {
                    ColumnItem columnCount = new ColumnItem();
                    columnCount.setName("count_" + table);
                    columnItems.add(columnCount);
                }
            }

            for (ColumnItem columnItem : tableItem.getColumns()) {
                List<String> tempData = new ArrayList<String>();
                while (resultSet.next()) {
                    tempData.add(resultSet.getString(columnItem.getName()));
                }
                if (columnItem.getAlias() != null && !columnItem.getAlias().equals(""))
                    data.put(columnItem.getAlias(), tempData);
                else
                    data.put(columnItem.getName(), tempData);
                resultSet.beforeFirst();
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Exception in Dao.getData(tableItem)", e.getCause());
        }
        return data;
    }

    public Map<String, List<String>> getData(TableItem tableItem, List<String> links, ColumnItem column, String table) {
        ResultSet resultSet = null;
        Statement statement = null;
        Connection connection = null;
        Map<String, List<String>> data = null;
        String query = sqlQueryBuilder.createQuery(tableItem, links, column, table);
        try {
            connection = ConnectionUtils.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query.toString());
            data = new HashMap<String, List<String>>();
            List<ColumnItem> columnItems = tableItem.getColumns();
            ColumnItem columnCount = new ColumnItem();
            columnCount.setName("count_" + table);
            columnItems.add(columnCount);
            for (ColumnItem columnItem : columnItems) {
                List<String> tempData = new ArrayList<String>();
                while (resultSet.next()) {
                    tempData.add(resultSet.getString(columnItem.getName()));
                }
                if (columnItem.getAlias() != null && !columnItem.getAlias().equals(""))
                    data.put(columnItem.getAlias(), tempData);
                else
                    data.put(columnItem.getName(), tempData);
                resultSet.beforeFirst();
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Exception in Dao.getData(tableItem)", e.getCause());
        }
        return data;
    }

    public Map<String,List<String>> getData(TableItem tableItem, Map<String, ColumnItem> extRefTables) {
        ResultSet resultSet = null;
        Statement statement = null;
        Connection connection = null;
        Map<String, List<String>> data = null;
        String query = sqlQueryBuilder.createQuery(tableItem, extRefTables);
        try {
            connection = ConnectionUtils.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query.toString());
            data = new HashMap<String, List<String>>();
            List<ColumnItem> columnItems = tableItem.getColumns();

            for (String table : extRefTables.keySet()) {
                ColumnItem columnCount = new ColumnItem();
                columnCount.setName("count_" + table);
                columnItems.add(columnCount);
            }

            for (ColumnItem columnItem : columnItems) {
                List<String> tempData = new ArrayList<String>();
                while (resultSet.next()) {
                    tempData.add(resultSet.getString(columnItem.getName()));
                }
                if (columnItem.getAlias() != null && !columnItem.getAlias().equals(""))
                    data.put(columnItem.getAlias(), tempData);
                else
                    data.put(columnItem.getName(), tempData);
                resultSet.beforeFirst();
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Exception in Dao.getData(tableItem)", e.getCause());
        }
        return data;
    }

    public String createQuery(TableItem tableItem) {

        return sqlQueryBuilder.createQuery(tableItem);
    }

    public String createQuery(TableItem tableItem, List<String> links, ColumnItem column, String referTable) {

        return sqlQueryBuilder.createQuery(tableItem, links, column, referTable);
    }

    public String createQuery(TableItem tableItem, String primaryKey, String indexColumn) {

        return sqlQueryBuilder.createQuery(tableItem, primaryKey, indexColumn);
    }

    public String createQuery(TableItem tableItem, Map<String, ColumnItem> extRefTables) {

        return sqlQueryBuilder.createQuery(tableItem, extRefTables);
    }
}
