package edu.dbframework.parse.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.dbframework.parse.beans.items.TableItem;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


public class DatabaseXMLBean {

	private List<TableItem> tables = new ArrayList<TableItem>();

	public DatabaseXMLBean() {
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
        if (!(obj instanceof DatabaseXMLBean))
            return false;
        DatabaseXMLBean bean = (DatabaseXMLBean) obj;
        return new EqualsBuilder()
                .append(this.tables, bean.tables)
                .isEquals();
    }
}
