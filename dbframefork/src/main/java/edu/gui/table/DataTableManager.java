package edu.gui.table;

import edu.dbframework.parse.beans.ColumnItem;
import edu.dbframework.parse.beans.TableItem;
import edu.dbframework.database.Service;
import edu.dbframework.parse.helpers.DatabaseManager;
import edu.gui.Main;

import java.util.List;
import java.util.Map;

import com.healthmarketscience.sqlbuilder.CustomSql;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.SelectQuery;

public class DataTableManager {

    private Service service = (Service) Main.context.getBean("service");

    public DataTableManager() {
    }

    public DataTableModel getTableItemDataModel(TableItem tableItem) {
        return new DataTableModel(tableItem, service.getDataForTableItem(tableItem));
    }

    public DataTableModel getOutgoingRelationDataModel(TableItem fromTableItem, List<String> fromKeys, ColumnItem column) {
    	TableItem toTableItem = ((DatabaseManager)Main.context.getBean("databaseManager")).getDatabaseBean().getTableByName(column.getRelationTableName());
    	
    	// find corresponding keys in toTable
    	SelectQuery q = new SelectQuery();
    	q.addCustomColumns(new CustomSql(column.getName()));
    	q.addCustomFromTable(new CustomSql(fromTableItem.getName()));
    	q.addCondition(new InCondition(new CustomSql(fromTableItem.getPrimaryKey().getName()), fromKeys));
    	Map<String, List<String>> right_keys = service.getDataByQuery(q.toString());
    	
        return new DataTableModel(toTableItem, service.getDataByRows(toTableItem, right_keys.get(column.getName())));
    }

    public DataTableModel getInternalRelationDataModel(TableItem tableItem, List<String> primaryKeys, String indexColumn) {
        return new DataTableModel(tableItem, service.getDataByRelationColumn(tableItem, primaryKeys, indexColumn));
    }

    public DataTableModel getDataModelBySqlQuery(TableItem tableItem, String query) {
        return new DataTableModel(tableItem, service.getDataByQuery(query));
    }
}
