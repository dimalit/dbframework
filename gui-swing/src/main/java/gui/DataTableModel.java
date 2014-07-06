package gui;

import edu.dbframework.database.DataUtils;
import edu.dbframework.parse.beans.items.ColumnItem;
import edu.dbframework.parse.beans.items.TableItem;
import edu.dbframework.service.DatabaseService;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class DataTableModel extends AbstractTableModel {
	
	private Map<String, List<String>> data;
	private List<String> columnNames;

    private String tableName;
	
	public DataTableModel(String table) {
        this.tableName = table;
		DataUtils dataUtils = new DataUtils();
		this.data = dataUtils.getData(table);
		this.columnNames = new ArrayList<String>();
		this.columnNames.addAll(data.keySet());
	}
	
	public DataTableModel(TableItem item) {
        this.tableName = item.getName();
        DatabaseService databaseService = new DatabaseService();
        this.data = databaseService.getDataForTableItem(item);
		this.columnNames = new ArrayList<String>();
		for (ColumnItem columnItem : item.getColumns()) {
			if (columnItem.getAlias() != null) {
				if (!columnItem.getAlias().equals(""))
					this.columnNames.add(columnItem.getAlias());
			} else {
				this.columnNames.add(columnItem.getName());
			}
		}
	}

    public DataTableModel(TableItem tableItem, List<String> links, ColumnItem column, String referTable) {
        this.tableName = tableItem.getName();
        DataUtils dataUtils = new DataUtils();
        this.data = dataUtils.getData(tableItem, links, column, referTable);
        this.columnNames = new ArrayList<String>();
        for (ColumnItem columnItem : tableItem.getColumns()) {
            if (columnItem.getAlias() != null) {
                if (!columnItem.getAlias().equals(""))
                    this.columnNames.add(columnItem.getAlias());
            } else {
                this.columnNames.add(columnItem.getName());
            }
        }
    }

    public DataTableModel(TableItem tableItem, String primaryKey, String indexColumn) {
        this.tableName = tableItem.getName();
        DataUtils dataUtils = new DataUtils();
        this.data = dataUtils.getData(tableItem, primaryKey, indexColumn);
        this.columnNames = new ArrayList<String>();
        for (ColumnItem columnItem : tableItem.getColumns()) {
            if (columnItem.getAlias() != null) {
                if (!columnItem.getAlias().equals(""))
                    this.columnNames.add(columnItem.getAlias());
            } else {
                this.columnNames.add(columnItem.getName());
            }
        }
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

    public String getTableName() {
        return tableName;
    }
}
