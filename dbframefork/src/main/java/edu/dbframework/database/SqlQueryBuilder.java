package edu.dbframework.database;

import edu.dbframework.parse.beans.items.ColumnItem;
import edu.dbframework.parse.beans.items.TableItem;
import edu.dbframework.parse.helpers.DatabaseBeanHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlQueryBuilder {

    public SqlQueryBuilder() {
    }

    public String createQuery(TableItem tableItem) {
        StringBuffer query = new StringBuffer();

        ArrayList<String> fromTables = new ArrayList<String>();
        ArrayList<String> whereColumns = new ArrayList<String>();

        query.append("SELECT ");
        for (ColumnItem columnItem : tableItem.getColumns()) {
            if (columnItem.getRelationTableName() != null && !columnItem.getRelationTableName().equals("")
                    && columnItem.getRelationColumnName() != null && !columnItem.getRelationColumnName().equals("")) {
                query.append(columnItem.getRelationTableName());
                query.append(".");
                query.append(columnItem.getRelationColumnName());
                query.append(" as " + columnItem.getName());
                query.append(", ");

                fromTables.add(columnItem.getRelationTableName());
                MetadataDao utils = new MetadataDao();
                whereColumns.add(tableItem.getName() + "."
                        + columnItem.getName() + "="
                        + columnItem.getRelationTableName() + "."
                        + utils.getPrimaryKeyColumns(columnItem.getRelationTableName()).get(0));
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
        whereOperator.append(column.getRelationTableName() + "." + column.getRelationColumnName());
        whereOperator.append(" IN(");
        for (String link : links) {
            whereOperator.append("'" + link + "',");
        }
        whereOperator.deleteCharAt(whereOperator.lastIndexOf(","));
        whereOperator.append(")");
        whereOperator.append(" GROUP BY " + column.getRelationTableName() + "." + column.getRelationColumnName());

        StringBuffer query = new StringBuffer(createQuery(tableItem));

        query.insert(query.indexOf("FROM") - 1, ", COUNT(" + referTable + "." + column.getName() + ") AS count_" + referTable);
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
        } else if (query.indexOf("GROUP BY") != -1) {
            query.insert(query.indexOf("GROUP BY"), " AND " + whereOperator + " ");
        } else {
            query.append(" AND " + whereOperator);
        }
        return query.toString();
    }

    public String createQuery(TableItem tableItem, Map<String, ColumnItem> extRefTables) {
        MetadataDao metadataUtils = new MetadataDao();

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
        query.insert(query.indexOf("FROM") - 1, countOperator + " ");

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