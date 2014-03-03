package edu.gui.table;

import edu.dbframework.parse.beans.items.ColumnItem;
import edu.dbframework.parse.beans.items.TableItem;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}
