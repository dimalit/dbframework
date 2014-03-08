package edu.dbframework.service;

import edu.dbframework.database.Dao;
import edu.dbframework.database.SqlQueryBuilder;
import edu.dbframework.parse.beans.items.ColumnItem;
import edu.dbframework.parse.beans.items.TableItem;

import java.util.List;
import java.util.Map;

public class Service {

    private SqlQueryBuilder sqlQueryBuilder;
    private Dao dao;

    public Service() {
    }

    public Map<String, List<String>> getDataForTableItem(TableItem tableItem) {
        return dao.getData(sqlQueryBuilder.buildQueryForTableItem(tableItem));
    }

    public Map<String, List<String>> getDataByRows(TableItem tableItem, List<String> rows, ColumnItem bindingColumn) {
        return dao.getData(sqlQueryBuilder.buildQueryForOutgoingRelationByRows(tableItem, rows, bindingColumn));
    }

    public Map<String, List<String>> getDataByRelationColumn(TableItem tableItem, List<String> primaryKeys, String indexColumn) {
        return dao.getData(sqlQueryBuilder.buildQueryForIncomingRelationByColumn(tableItem, primaryKeys, indexColumn));
    }

    public SqlQueryBuilder getSqlQueryBuilder() {
        return sqlQueryBuilder;
    }

    public void setSqlQueryBuilder(SqlQueryBuilder sqlQueryBuilder) {
        this.sqlQueryBuilder = sqlQueryBuilder;
    }

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }
}
