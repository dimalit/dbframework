package edu.dbframework.service;

import edu.dbframework.database.DataUtils;
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
public class DatabaseService {

    private DataUtils dataUtils;

    public DatabaseService() {
        this.dataUtils = new DataUtils();
    }

    public Map<String, List<String>> getDataForTableItem(TableItem tableItem) {
        Map<String, List<String>> data = null;
        DatabaseBeanHelper databaseBeanHelper = new DatabaseBeanHelper();
        Map<String, ColumnItem> extRefTables = databaseBeanHelper.getExternalReferencesForTable(tableItem.getName());
        if (extRefTables.size() > 0)
            data = dataUtils.getData(tableItem, extRefTables);
        else
            data = dataUtils.getData(tableItem);
        return data;
    }
}
