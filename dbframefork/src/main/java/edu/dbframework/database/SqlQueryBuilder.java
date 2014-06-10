package edu.dbframework.database;

import com.healthmarketscience.sqlbuilder.*;
import edu.dbframework.parse.beans.ColumnItem;
import edu.dbframework.parse.beans.TableItem;
import edu.dbframework.parse.helpers.DatabaseManager;
import edu.gui.Main;
import edu.gui.frame.MainFrame;

import java.util.*;

public class SqlQueryBuilder {

    private SelectQuery query;
    private MetadataDao metadataDao;

    private Map<String, TableHistoryBean> queryMap = new LinkedHashMap<String, TableHistoryBean>();

    public SqlQueryBuilder() {
    }

    public String buildQueryForTableItem(TableItem tableItem) {
        performSelectQuery(tableItem);
        TableHistoryBean bean = new TableHistoryBean(tableItem, tableItem.getName(), query.validate().toString());
        queryMap.clear();
        queryMap.put(bean.getName(), bean);
        return query.validate().toString();
    }

    public String buildQueryForOutgoingRelationByRows(TableItem tableItem, List<String> rows) {
        performSelectQuery(tableItem);
        query.addCondition(new InCondition(new CustomSql(tableItem.getName() + "." + tableItem.getPrimaryKey().getName()), rows));
        TableHistoryBean bean = new TableHistoryBean(tableItem, tableItem.getName() + " (outgoing rows)", query.toString());
        queryMap.put(bean.getName(), bean);
        return query.toString();
    }

    public String buildQueryForIncomingRelationByColumn(TableItem tableItem, List<String> primaryKeys, String relationColumn) {
        performSelectQuery(tableItem);
        query.addCondition(new InCondition(new CustomSql(tableItem.getName() + "." + relationColumn), primaryKeys));
        TableHistoryBean bean = new TableHistoryBean(tableItem, tableItem.getName() + " (incoming rows)", query.toString());
        queryMap.put(bean.getName(), bean);
        return query.toString();
    }

    private void performSelectQuery(TableItem tableItem) {
        query = new SelectQuery();
        Map<String, ColumnItem> extRefs = Main.databaseManager.getDatabaseBean().internalRelationsForTable(tableItem.getName());

        /*-------Columns------*/
        for (ColumnItem item : tableItem.getColumns()) {
        	// compute alias separately:
        	String alias = item.getName();
            if (item.getAlias() != null && item.getAlias().length() > 0)
            	alias = item.getAlias();
            
            // now decide if relations or not:
            if (item.getRelationTableName() != null && item.getRelationColumnName() != null
                    && item.getRelationTableName().length() > 0 && item.getRelationColumnName().length() > 0) {
                query.addCustomColumns(new CustomSql(item.getRelationTableName() + "." + item.getRelationColumnName()
                        + " as " + alias));
            } else {
                query.addCustomColumns(new CustomSql(tableItem.getName() + "." + item.getName()));
            }
            if (item.getPredicate() != null && item.getPredicate().length() > 0) {
                query.addCondition(new CustomCondition(tableItem.getName() + "." + item.getName() + " " + item.getPredicate()));
            }
        }

        if (extRefs.size() > 0) {
            for (String table : extRefs.keySet()) {
                query.addCustomColumns(new CustomSql(
                        FunctionCall.count().addCustomParams(new CustomSql(table + "." + extRefs.get(table).getName())) + " as " + table));
            }
        }
        query.addCustomFromTable(new CustomSql(tableItem.getName()));

        /*-------Joins------*/
        for (ColumnItem column : tableItem.columnsWithRelationsAsList()) {
            String relTableName = column.getRelationTableName();
            query.addCustomJoin(SelectQuery.JoinType.LEFT_OUTER, tableItem.getName(), relTableName,
                    BinaryCondition.equalTo(new CustomSql(tableItem.getName() + "." + column.getName()),
                            new CustomSql(relTableName + "." + metadataDao.getPrimaryKeyColumns(relTableName).get(0))));
        }
        if (extRefs.size() > 0) {
            for (String table : extRefs.keySet()) {
                query.addCustomJoin(SelectQuery.JoinType.LEFT_OUTER, tableItem.getName(), table,
                        BinaryCondition.equalTo(new CustomSql(table + "." + extRefs.get(table).getName()),
                                new CustomSql(tableItem.getName() + "." + metadataDao.getPrimaryKeyColumns(tableItem.getName()).get(0))));
            }
            /*-------Group by------*/
            query.addCustomGroupings(new CustomSql(tableItem.getName() + "." + tableItem.getColumns().get(0).getName()));
        }

        /*-------Predicates------*/
        //for (ColumnItem column : tableItem.getColumns())
    }

    public void setMetadataDao(MetadataDao metadataDao) {
        this.metadataDao = metadataDao;
    }

    public MetadataDao getMetadataDao() {
        return metadataDao;
    }

    public Map<String, TableHistoryBean> getQueryMap() {
        return queryMap;
    }
}