package edu.gui.table;

import edu.dbframework.parse.beans.ColumnItem;
import edu.dbframework.parse.beans.TableItem;

import javax.swing.table.AbstractTableModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("serial")
public class DataTableModel extends AbstractTableModel {

    private Map<String, List<String>> data;
	private List<String> columnNames;
    private TableItem tableItem;

	public DataTableModel(TableItem item, Map<String, List<String>> data) {
        this.tableItem = item;
        this.data = data;
		this.columnNames = new ArrayList<String>();
        prepareColumnNames();
    }

    private void prepareColumnNames() {
        columnNames.addAll(data.keySet());
    }

	@Override
	public int getRowCount() {
		return data.get(columnNames.get(0)).size();
	}

	@Override
	public int getColumnCount() {
		return this.columnNames.size();
	}
	
	@Override
	public String getColumnName(int col) {
        return columnNames.get(col);
    }

	public String getTableNameForColumn(int col){
		String needle = getColumnName(col);
				
		// 1st fin alias
		for(ColumnItem ci: tableItem.getColumns()){
			if(needle.equals(ci.getAlias()))
				return ci.getName();
		}// for
		
		// else fallback to name
		return needle;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String column = this.columnNames.get(columnIndex);
		return data.get(column).get(rowIndex);
	}

    public TableItem getTableItem() {
        return tableItem;
    }

    public Map<Integer, ColumnItem> getOutgoingColumnsByIndex() {
        Map<Integer, ColumnItem> result = new HashMap<Integer, ColumnItem>();
        for (int i = 0; i <tableItem.getColumns().size(); i++) {
            ColumnItem columnItem = tableItem.getColumns().get(i);
            if (columnItem.getRelationTableName() != null && !columnItem.getRelationTableName().equals("")
                    && columnItem.getRelationColumnName() != null && !columnItem.getRelationColumnName().equals("")) {
                result.put(i, columnItem);
            }
        }
        return result;
    }

    public List<Integer> getIncomingColumnIndexes() {
        List<Integer> result = new ArrayList<Integer>();
        if (data.size() > tableItem.getColumns().size()) {
            for (int i = tableItem.getColumns().size(); i < data.size(); i++) {
                result.add(i);
            }
        }
        return result;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public List<String> getColumnsWithNumbers(){
    	List<String> res = new ArrayList<String>();
    	Set<String> cols = tableItem.columnsAsMap().keySet();
    	
    	// add cols not present on table - they are incoming links:
    	for(String local_col: data.keySet()){
    		if(!cols.contains(local_col))
    			res.add(local_col);
    	}
    	
    	// add cols explicitly marked as able for chart
    	res.addAll(tableItem.columnsAbleForChartAsMap().keySet());
    	
    	return res;
    }
    
    public Map<String, List<String>> getData() {
        return data;
    }

}
