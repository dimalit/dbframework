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


public class DataUtils {

    public DataUtils() {
    }

    public Map<String, List<String>> getData(String table) {
        ResultSet resultSet = null;
        Statement statement = null;
        Connection connection = null;
        Map<String, List<String>> data = null;
        MetadataUtils utils = new MetadataUtils();
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
            throw new RuntimeException("Exception in DataUtils.getData(table)",
                    e.getCause());
        } finally {
            try {
                statement.close();
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(
                        "Exception in DataUtils.getData(table)", e.getCause());
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
        String query = createQuery(tableItem);
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
                    "Exception in DataUtils.getData(tableItem)", e.getCause());
        }
        return data;
    }

    public Map<String, List<String>> getData(TableItem tableItem, String primaryKey, String indexColumn) {
        ResultSet resultSet = null;
        Statement statement = null;
        Connection connection = null;
        Map<String, List<String>> data = null;
        String query = createQuery(tableItem, primaryKey, indexColumn);
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
                    "Exception in DataUtils.getData(tableItem)", e.getCause());
        }
        return data;
    }

    public Map<String, List<String>> getData(TableItem tableItem, List<String> links, ColumnItem column, String table) {
        ResultSet resultSet = null;
        Statement statement = null;
        Connection connection = null;
        Map<String, List<String>> data = null;
        String query = createQuery(tableItem, links, column, table);
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
                    "Exception in DataUtils.getData(tableItem)", e.getCause());
        }
        return data;
    }

    public Map<String,List<String>> getData(TableItem tableItem, Map<String, ColumnItem> extRefTables) {
        ResultSet resultSet = null;
        Statement statement = null;
        Connection connection = null;
        Map<String, List<String>> data = null;
        String query = createQuery(tableItem, extRefTables);
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
                    "Exception in DataUtils.getData(tableItem)", e.getCause());
        }
        return data;
    }

    public String createQuery(TableItem tableItem) {
        StringBuffer query = new StringBuffer();

        ArrayList<String> fromTables = new ArrayList<String>();
        ArrayList<String> whereColumns = new ArrayList<String>();

        query.append("SELECT ");
        for (ColumnItem columnItem : tableItem.getColumns()) {
            if (columnItem.getIndexTableName() != null && !columnItem.getIndexTableName().equals("")
                    && columnItem.getIndexColumnName() != null && !columnItem.getIndexColumnName().equals("")) {
                query.append(columnItem.getIndexTableName());
                query.append(".");
                query.append(columnItem.getIndexColumnName());
                query.append(" as " + columnItem.getName());
                query.append(", ");

                fromTables.add(columnItem.getIndexTableName());
                MetadataUtils utils = new MetadataUtils();
                whereColumns.add(tableItem.getName() + "."
                        + columnItem.getName() + "="
                        + columnItem.getIndexTableName() + "."
                        + utils.getPrimaryKeyColumns(columnItem.getIndexTableName()).get(0));
            } else {
                query.append(tableItem.getName());
                query.append(".");
                query.append(columnItem.getName());
                query.append(", ");
            }
        }
        query.deleteCharAt(query.lastIndexOf(","));
        query.append("FROM ");
        if (fromTables.size() > 0) {
            for (String fromTable : fromTables) {
                query.append(fromTable);
                query.append(", ");
            }
            query.append(tableItem.getName() + " ");
            query.append("WHERE ");
            for (String whereColumn : whereColumns) {
                query.append(whereColumn);
                query.append(" AND ");
            }
            query.delete(query.lastIndexOf("AND"), query.length() - 1);
        } else {
            query.append(tableItem.getName());
        }
        return query.toString();
    }

    public String createQuery(TableItem tableItem, List<String> links, ColumnItem column, String referTable) {
        StringBuffer joinOperator = new StringBuffer();
        joinOperator.append("LEFT JOIN " + referTable);
        joinOperator.append(" ON " + referTable + "." + column.getName());
        joinOperator.append("=" + tableItem.getName() + "." + column.getName());

        StringBuffer whereOperator = new StringBuffer();
        whereOperator.append(column.getIndexTableName() + "." + column.getIndexColumnName());
        whereOperator.append(" IN(");
        for (String link : links) {
            whereOperator.append("'" + link + "',");
        }
        whereOperator.deleteCharAt(whereOperator.lastIndexOf(","));
        whereOperator.append(")");
        whereOperator.append(" GROUP BY " + column.getIndexTableName() + "." + column.getIndexColumnName());

        StringBuffer query = new StringBuffer(createQuery(tableItem));

        query.insert(query.indexOf("FROM")- 1, ", COUNT(" + referTable + "." + column.getName() + ") AS count_" + referTable);
        if (query.indexOf(" " + referTable + ",") != -1) {
            int index = query.indexOf(" " + referTable + ",");
            query.delete(index + 1, index + 2 + referTable.length());
        }
        if (query.indexOf(" " + referTable + " ") != -1) {
            int index = query.indexOf(" " + referTable + " ");
            query.delete(index + 1, index + 2 + referTable.length());
        }
        if (query.indexOf("WHERE") == -1) {
            query.append(" " + joinOperator + " WHERE " + whereOperator);
        } else {
            query.append(" AND " + whereOperator);
            query.insert(query.indexOf("WHERE"), joinOperator + " ");
        }
        return query.toString();
    }

    public String createQuery(TableItem tableItem, String primaryKey, String indexColumn) {
        StringBuffer whereOperator = new StringBuffer();
        whereOperator.append(tableItem.getName() + "." + indexColumn);
        whereOperator.append("=" + primaryKey);

        String query1 = null;

        DatabaseBeanHelper databaseBeanHelper = new DatabaseBeanHelper();
        Map<String, ColumnItem> extRefTables = databaseBeanHelper.getExternalReferencesForTable(tableItem.getName());
        if (extRefTables.size() > 0)
            query1 = createQuery(tableItem, extRefTables);
        else
            query1 = createQuery(tableItem);

        StringBuffer query = new StringBuffer(query1);
        if (query.indexOf("WHERE") == -1) {
            query.append(" WHERE " + whereOperator);
        } else if (query.indexOf("GROUP BY") != -1){
            query.insert(query.indexOf("GROUP BY"), " AND " + whereOperator + " ");
        } else {
            query.append(" AND " + whereOperator);
        }
        return query.toString();
    }

    public String createQuery(TableItem tableItem, Map<String, ColumnItem> extRefTables) {
        MetadataUtils metadataUtils = new MetadataUtils();

        StringBuffer countOperator = new StringBuffer();
        for (String table : extRefTables.keySet()) {
            countOperator.append(", COUNT(" + table + "." + extRefTables.get(table).getName() + ")");
            countOperator.append(" AS count_" + table);
        }

        StringBuffer joinOperator = new StringBuffer();
        for (String table : extRefTables.keySet()) {
            joinOperator.append(" LEFT JOIN " + table + " ON ");
            joinOperator.append(table + "." + extRefTables.get(table).getName());
            joinOperator.append("=" + tableItem.getName() + ".");
            joinOperator.append(metadataUtils.getPrimaryKeyColumns(tableItem.getName()).get(0));
        }

        StringBuffer query = new StringBuffer(createQuery(tableItem));
        query.insert(query.indexOf("FROM")- 1, countOperator + " ");

        for (String table : extRefTables.keySet()) {
            if (query.indexOf(" " + table + ",") != -1) {
                int index = query.indexOf(" " + table + ",");
                query.delete(index + 1, index + 2 + table.length());
            }
            if (query.indexOf(" " + table + " ") != -1) {
                int index = query.indexOf(" " + table + " ");
                query.delete(index + 1, index + 2 + table.length());
            }
        }

        if (query.indexOf("WHERE") == -1) {
            query.append(joinOperator);
        } else {
            query.insert(query.indexOf("WHERE"), joinOperator + " ");
        }

        query.append(" GROUP BY " + tableItem.getName() + "." + tableItem.getColumns().get(0).getName());
        return query.toString();
    }
}
