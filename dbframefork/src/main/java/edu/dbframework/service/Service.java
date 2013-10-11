package edu.dbframework.service;

import edu.dbframework.database.Dao;
import edu.dbframework.database.SqlQueryBuilder;
import edu.dbframework.parse.beans.items.ColumnItem;
import edu.dbframework.parse.beans.items.TableItem;
import edu.dbframework.parse.helpers.DatabaseBeanHelper;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: GreenPoser
 * Date: 15.06.13
 * Time: 13:03
 * To change this template use File | Settings | File Templates.
 */
public class Service {

    private SqlQueryBuilder builder;
    private Dao dao;

    public Service() {
        builder = new SqlQueryBuilder();
        this.dao = new Dao();
    }

    public Map<String, List<String>> getDataForTableItem(TableItem tableItem) {
        return dao.getData(builder.buildQueryForTableItem(tableItem));
    }

    public Map<String, List<String>> getDataByRows(TableItem tableItem, List<String> rows, ColumnItem bindingColumn, String relTable) {
        return dao.getData(builder.buildQueryByRows(tableItem, rows, bindingColumn, relTable));
    }

    public Map<String, List<String>> getDataByRelationColumn(TableItem tableItem, String primaryKey, String indexColumn) {
        return dao.getData(builder.buildQueryByRelationColumn(tableItem, primaryKey, indexColumn));
    }
}
