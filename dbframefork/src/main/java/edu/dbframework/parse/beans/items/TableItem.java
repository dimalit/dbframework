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

    private ColumnItem primaryKey;

    private String name;
    private List<ColumnItem> columns = new ArrayList<ColumnItem>();
    public TableItem() {
    }

	public List<ColumnItem> getColumns() {
		return columns;
	}

    public ColumnItem getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(ColumnItem primaryKey) {
        this.primaryKey = primaryKey;
    }

	public void setColumns(List<ColumnItem> columns) {
		this.columns = columns;
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

    public String[] relationColumnsAsStringArray() {
        List<String> relColumns = new ArrayList<String>();
        for (ColumnItem columnItem : columns) {
            if (columnItem.getRelationTableName() != null && columnItem.getRelationColumnName() != null) {
                relColumns.add(columnItem.getRelationTableName() + "." + columnItem.getRelationColumnName());
            }
        }
        return relColumns.toArray(new String[relColumns.size()]);
    }

    public List<ColumnItem> columnsWithRelationsAsList() {
        List<ColumnItem> relColumns = new ArrayList<ColumnItem>();
        for (ColumnItem columnItem : columns) {
            if (columnItem.getRelationTableName() != null && columnItem.getRelationColumnName() != null) {
                relColumns.add(columnItem);
            }
        }
        return relColumns;
    }

    public Map<String, String> relationColumnsAsMap() {
        HashMap<String, String> columns = new HashMap<String, String>();
        for (ColumnItem columnItem : this.columns) {
            if (columnItem.getRelationTableName() != null && columnItem.getRelationColumnName() != null) {
                columns.put(columnItem.getRelationTableName(), columnItem.getRelationColumnName());
            }
        }
        return columns;
    }

    public String[] columnsAsStringArray() {
        String[] colArray = new String[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            colArray[i] = this.getName() + "." + columns.get(i).getName();
        }
        return colArray;
    }

    public Map<String, ColumnItem> columnsAsMap() {
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
