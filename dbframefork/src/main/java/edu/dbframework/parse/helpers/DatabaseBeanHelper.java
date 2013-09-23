package edu.dbframework.parse.helpers;

import edu.dbframework.parse.beans.DatabaseXMLBean;
import edu.dbframework.parse.beans.items.ColumnItem;
import edu.dbframework.parse.beans.items.TableItem;
import edu.dbframework.parse.parsers.DatabaseBeanParser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: GreenPoser
 * Date: 13.06.13
 * Time: 23:02
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseBeanHelper {

    private File parsingFile;
    private DatabaseXMLBean databaseXMLBean;
    private DatabaseBeanParser databaseBeanParser;

    public DatabaseBeanHelper() {
        databaseBeanParser = new DatabaseBeanParser("database.xml");
        this.parsingFile = databaseBeanParser.getParsingFile();
        if (parsingFile.exists())
            this.databaseXMLBean = databaseBeanParser.getBeanFromXML();
    }

    public DatabaseBeanHelper(File parsingFile) {
        this.parsingFile = parsingFile;
    }

    public DatabaseXMLBean getDatabaseXMLBean() {
        if (!databaseBeanParser.getBeanFromXML().equals(this.databaseXMLBean))
            this.databaseXMLBean = databaseBeanParser.getBeanFromXML();
        return databaseXMLBean;
    }

    public void setDatabaseXMLBean(DatabaseXMLBean databaseXMLBean) {
        this.databaseBeanParser.addBeanToXML(databaseXMLBean);
        this.databaseXMLBean = databaseXMLBean;
    }

    public List<String> getTablesList() {
        ArrayList<String> tables = new ArrayList<String>();
        for (TableItem item : this.databaseXMLBean.getTables()) {
            tables.add(item.getName());
        }
        return tables;
    }

    public TableItem getTableItemByName(String tableName) {
        return databaseXMLBean.createTablesMap().get(tableName);
    }

    public Map<String, ColumnItem> getExternalReferencesForTable(String tableName) {
        Map<String, ColumnItem> extReferenceTables = new HashMap<String, ColumnItem>();
        for (TableItem tableItem: databaseXMLBean.getTables()) {
            for (ColumnItem columnItem : tableItem.getColumns()) {
                if (columnItem.getRelationTableName() != null) {
                    if (columnItem.getRelationTableName().equals(tableName)) {
                        extReferenceTables.put(tableItem.getName(), columnItem);
                        break;
                    }
                }
            }
        }
        return extReferenceTables;
    }
}
