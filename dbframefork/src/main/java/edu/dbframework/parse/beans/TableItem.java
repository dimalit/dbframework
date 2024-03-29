package edu.dbframework.parse.beans;

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
    public TableItem() {
    }

	public List<ColumnItem> getColumns() {
		return columns;
	}

    public ColumnItem getPrimaryKey() {
        for (ColumnItem item : columns) {
            if (item.getPrimaryKey()) {
                return item;
            }
        }
        return null;
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
            if (columnItem.getRelationTableName() != null && columnItem.getRelationColumnName() != null
                    && columnItem.getRelationTableName().length() > 0 && columnItem.getRelationColumnName().length() > 0) {
                relColumns.add(columnItem.getRelationTableName() + "." + columnItem.getRelationColumnName());
            }
        }
        return relColumns.toArray(new String[relColumns.size()]);
    }

    public List<ColumnItem> columnsWithRelationsAsList() {
        List<ColumnItem> relColumns = new ArrayList<ColumnItem>();
        for (ColumnItem columnItem : columns) {
            if (columnItem.getRelationTableName() != null && columnItem.getRelationColumnName() != null
                    && columnItem.getRelationTableName().length() > 0 && columnItem.getRelationColumnName().length() > 0) {
                relColumns.add(columnItem);
            }
        }
        return relColumns;
    }

    public Map<String, String> relationColumnsAsMap() {
        HashMap<String, String> columns = new HashMap<String, String>();
        for (ColumnItem columnItem : this.columns) {
            if (columnItem.getRelationTableName() != null && columnItem.getRelationColumnName() != null
                    && columnItem.getRelationTableName().length() > 0 && columnItem.getRelationColumnName().length() > 0) {
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

    public Map<String, ColumnItem> columnsAbleForChartAsMap() {
        HashMap<String, ColumnItem> columns = new HashMap<String, ColumnItem>();
        for (ColumnItem columnItem : this.columns) {
            if (columnItem.getIsAbleForChart() != null && columnItem.getIsAbleForChart()) {
                columns.put(columnItem.getName(), columnItem);
            }
        }
        return columns;
    }


    public ColumnItem getColumnByName(String name) {
        return columnsAsMap().get(name);
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
