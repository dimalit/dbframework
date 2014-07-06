package edu.dbframework.parse.beans.items;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *		
 * @author GreenPoser
 *
 */
public class TableItem {
	
	private String name;
	private List<ColumnItem> columns = new ArrayList<ColumnItem>();
	
	public List<ColumnItem> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnItem> columns) {
		this.columns = columns;
	}

	public TableItem() {
	}
	
	public void addColumn(ColumnItem column) {
		columns.add(column);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public Map<String, ColumnItem> createColumnsMap() {
        HashMap<String, ColumnItem> columnsMap = new HashMap<String, ColumnItem>();
        for (ColumnItem columnItem : columns) {
            columnsMap.put(columnItem.getName(), columnItem);
        }
        return columnsMap;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.name)
                .append(this.columns)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof TableItem))
            return false;
        TableItem tableItem = (TableItem) obj;
        return new EqualsBuilder()
                .append(this.name, tableItem.name)
                .append(this.columns, tableItem.columns)
                .isEquals();
    }
}
