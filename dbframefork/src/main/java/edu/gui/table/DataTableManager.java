package edu.gui.table;


import edu.dbframework.parse.beans.ColumnItem;
import edu.dbframework.parse.beans.TableItem;
import edu.dbframework.database.Service;
import edu.gui.Main;

import java.util.List;

public class DataTableManager {

    private Service service = (Service) Main.context.getBean("service");

    public DataTableManager() {
    }

    public DataTableModel getTableItemDataModel(TableItem tableItem) {
        return new DataTableModel(tableItem, service.getDataForTableItem(tableItem));
    }

    public DataTableModel getOutgoingRelationDataModel(TableItem tableItem, List<String> links, ColumnItem column) {
        return new DataTableModel(tableItem, service.getDataByRows(tableItem, links, column));
    }

    public DataTableModel getInternalRelationDataModel(TableItem tableItem, List<String> primaryKeys, String indexColumn) {
        return new DataTableModel(tableItem, service.getDataByRelationColumn(tableItem, primaryKeys, indexColumn));
    }

/*    public DataTableModel getDataModelBySqlAuery(String query) {
        return new DataTableModel()
    }*/
}
