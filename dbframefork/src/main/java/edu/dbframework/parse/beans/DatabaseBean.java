package edu.dbframework.parse.beans;

import edu.dbframework.parse.beans.items.ColumnItem;
import edu.dbframework.parse.beans.items.TableItem;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DatabaseBean {

	private List<TableItem> tables = new ArrayList<TableItem>();

	public DatabaseBean() {
	}

	public List<TableItem> getTables() {
		return tables;
	}

	public void setTables(List<TableItem> tables) {
		this.tables = tables;
	}
	
	public void addTable(TableItem bean) {
		tables.add(bean);
	}

    public Map<String, TableItem> createTablesMap() {
        HashMap<String, TableItem> tablesMap = new HashMap<String, TableItem>();
        for (TableItem tableItem : tables) {
            tablesMap.put(tableItem.getName(), tableItem);
        }
        return tablesMap;
    }

    public TableItem getTableByName(String name) {
        return createTablesMap().get(name);
    }

    public List<String> tablesAsStringList() {
        ArrayList<String> tables = new ArrayList<String>();
        for (TableItem item : getTables()) {
            tables.add(item.getName());
        }
        return tables;
    }

    public Map<String, ColumnItem> internalRelationsForTable(String tableName) {
        Map<String, ColumnItem> extReferenceTables = new HashMap<String, ColumnItem>();
        for (TableItem tableItem: getTables()) {
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(tables)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof DatabaseBean))
            return false;
        DatabaseBean bean = (DatabaseBean) obj;
        return new EqualsBuilder()
                .append(this.tables, bean.tables)
                .isEquals();
    }
}
