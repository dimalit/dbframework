package edu.dbframework.database;


import edu.dbframework.parse.beans.TableItem;

public class TableHistoryBean {

    private TableItem tableItem;
    private String name;
    private String query;

    public TableHistoryBean() {
    }

    public TableHistoryBean(TableItem tableItem, String name, String query) {
        this.tableItem = tableItem;
        this.name = name;
        this.query = query;
    }

    public TableItem getTableItem() {
        return tableItem;
    }

    public void setTableItem(TableItem tableItem) {
        this.tableItem = tableItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return name;
    }
}
