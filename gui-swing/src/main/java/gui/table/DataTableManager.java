package gui.table;


import edu.dbframework.parse.beans.items.ColumnItem;
import edu.dbframework.parse.beans.items.TableItem;
import edu.dbframework.service.Service;
import gui.Main;

import java.util.List;

public class DataTableManager {

    private Service service = (Service) Main.context.getBean("service");

    public DataTableManager() {
    }

    public DataTableModel getTableItemDataModel(TableItem tableItem) {
        return new DataTableModel(tableItem, service.getDataForTableItem(tableItem));
    }

    public DataTableModel getExternalRelationDataModel(TableItem tableItem, List<String> links, ColumnItem column, String referTable) {
        return new DataTableModel(tableItem, service.getDataByRows(tableItem, links, column, referTable));
    }

    public DataTableModel getInternalRelationDataModel(TableItem tableItem, String primaryKey, String indexColumn) {
        return new DataTableModel(tableItem, service.getDataByRelationColumn(tableItem, primaryKey, indexColumn));
    }
}
