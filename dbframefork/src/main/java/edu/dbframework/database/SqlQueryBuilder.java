package edu.dbframework.database;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.CustomSql;
import com.healthmarketscience.sqlbuilder.FunctionCall;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import edu.dbframework.parse.beans.items.ColumnItem;
import edu.dbframework.parse.beans.items.TableItem;
import edu.dbframework.parse.helpers.DatabaseBeanHelper;

import java.util.List;
import java.util.Map;

public class SqlQueryBuilder {

    private SelectQuery query;
    private MetadataDao metadataDao;

    public SqlQueryBuilder() {
        metadataDao = new MetadataDao();
    }

    public String buildQueryForTableItem(TableItem tableItem) {
        performSelectQuery(tableItem);
        return query.validate().toString();
    }

    public String buildQueryByRows(TableItem tableItem, List<String> rows, ColumnItem bindingColumn, String relTable) {
        performSelectQuery(tableItem);

        query.addCustomJoin(SelectQuery.JoinType.LEFT_OUTER,tableItem.getName(), relTable,
                BinaryCondition.equalTo(new CustomSql(relTable + "." + bindingColumn.getName()),
                        new CustomSql(tableItem.getName() + "." + bindingColumn.getName())));

        query.addCondition(new InCondition(new CustomSql(bindingColumn.getRelationTableName() + "." + bindingColumn.getRelationColumnName()), rows));

        return query.toString();
    }

    public String buildQueryByRelationColumn(TableItem tableItem, String primaryKey, String indexColumn) {
        performSelectQuery(tableItem);

        query.addCondition(BinaryCondition.equalTo(new CustomSql(tableItem.getName() + "." + indexColumn),
                new CustomSql(primaryKey)));

        return query.toString();
    }

    private void performSelectQuery(TableItem tableItem) {
        query = new SelectQuery();
        Map<String, ColumnItem> extRefs = new DatabaseBeanHelper().getExternalReferencesForTable(tableItem.getName());

        /*-------Columns------*/
        for (ColumnItem item : tableItem.getColumns()) {
            if (item.getAlias() != null && !item.getAlias().equals("")) {
                query.addCustomColumns(new CustomSql(tableItem.getName() + "." + item.getName() + " as " + item.getAlias()));
            } else {
                query.addCustomColumns(new CustomSql(tableItem.getName() + "." + item.getName()));
            }
        }
        for (String relColumn : tableItem.relationColumnsAsStringArray()) {
            query.addCustomColumns(new CustomSql(relColumn));
        }
        if (extRefs.size() > 0) {
            for (String table : extRefs.keySet()) {
                query.addCustomColumns(FunctionCall.count().addCustomParams(new CustomSql( table + "." + extRefs.get(table).getName())));
            }
        }
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
        }
        /*-------Group by------*/
        query.addCustomGroupings(new CustomSql(tableItem.getName() + "." + tableItem.getColumns().get(0).getName()));
    }
}