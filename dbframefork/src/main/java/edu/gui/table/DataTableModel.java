package edu.gui.table;

import edu.dbframework.parse.beans.items.TableItem;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
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

}
