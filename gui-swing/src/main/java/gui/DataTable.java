package gui;

import edu.dbframework.parse.beans.items.ColumnItem;
import edu.dbframework.parse.beans.items.TableItem;
import edu.dbframework.parse.helpers.DatabaseBeanHelper;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: GreenPoser
 * Date: 02.06.13
 * Time: 16:28
 * To change this template use File | Settings | File Templates.
 */
public class DataTable extends JTable {

    public DataTable() {
        super();
        this.setPreferredScrollableViewportSize(new Dimension(800, 600));
        this.setFillsViewportHeight(true);
        this.setBorder(BorderFactory.createLineBorder(Color.gray));
        renderListeners();
    }

    public DataTable(DataTableModel tableModel) {
        super(tableModel);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        this.setPreferredScrollableViewportSize(new Dimension(800, 600));
        this.setFillsViewportHeight(true);
        this.setBorder(BorderFactory.createLineBorder(Color.gray));
        renderListeners();
    }

    public void renderListeners() {
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setBackground(Color.LIGHT_GRAY);
        final DatabaseBeanHelper databaseBeanHelper = new DatabaseBeanHelper();

        final DataTableModel model = (DataTableModel) this.getModel();
        final HashMap<Integer, ColumnItem> indexColumns = new HashMap<Integer, ColumnItem>();
        final TableItem tableItem = databaseBeanHelper.getTableItemByName(model.getTableName());

        for (int i = 0; i <tableItem.getColumns().size(); i++) {
            List<ColumnItem> columns = tableItem.getColumns();
            ColumnItem columnItem = columns.get(i);
            if (columnItem.getRelationTableName() != null && !columnItem.getRelationTableName().equals("")
                    && columnItem.getRelationColumnName() != null && !columnItem.getRelationColumnName().equals("")) {
                getColumnModel().getColumn(i).setHeaderRenderer(cellRenderer);
                indexColumns.put(i, columnItem);
            }
        }

        this.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                int index = convertColumnIndexToModel(columnAtPoint(mouseEvent.getPoint()));
                if (indexColumns.containsKey(index)) {
                    int[] selectedRows = DataTable.this.getSelectedRows();
                    ColumnItem columnItem = indexColumns.get(index);
                    TableItem creatingTableItem = databaseBeanHelper.getTableItemByName(columnItem.getRelationTableName());
                    if (selectedRows.length >= 1) {
                        List<String> links = new ArrayList<String>();
                        for (int i = 0; i < selectedRows.length; i++) {
                            links.add((String) getModel().getValueAt(selectedRows[i], index));
                        }
                        DataTable table = new DataTable(new DataTableModel(creatingTableItem, links, columnItem, model.getTableName()));
                        Main.drawTable(table);
                    } else {
                        DataTable table = new DataTable(new DataTableModel(creatingTableItem));
                        Main.drawTable(table);
                    }
                    System.out.println("Clicked on column " + index + " " + DataTable.this.getSelectedRow());
                }
            }
        });

        if (this.getModel().getColumnCount() > tableItem.getColumns().size()) {
            this.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) {
                        int selectedColumn = getColumnModel().getSelectionModel().getLeadSelectionIndex();
                        int k = tableItem.getColumns().size();
                        while(k != DataTable.this.getModel().getColumnCount()) {
                            if (getColumnModel().getSelectionModel().getLeadSelectionIndex() == k) {
                                String columnName = DataTable.this.getColumnName(selectedColumn);
                                String table = columnName.substring(columnName.lastIndexOf("_") + 1);
                                TableItem tableItem1 = databaseBeanHelper.getTableItemByName(table);
                                String indexColumn = null;
                                for (ColumnItem columnItem : tableItem1.getColumns()) {
                                    if (columnItem.getRelationTableName() != null) {
                                        if (columnItem.getRelationTableName().equals(tableItem.getName()))
                                            indexColumn = columnItem.getName();
                                    }
                                }

                                String primaryKey = null;
                                ColumnItem primaryKeyColumn = null;
                                for (ColumnItem columnItem : tableItem.getColumns()) {
                                    if (columnItem.getPrimaryKey()) {
                                        primaryKeyColumn = columnItem;
                                        break;
                                    }
                                }
                                for (int i = 0; i < DataTable.this.getModel().getColumnCount(); i++) {
                                    if (DataTable.this.getModel().getColumnName(i).equals(primaryKeyColumn.getName())) {
                                        primaryKey = (String) DataTable.this.getModel().getValueAt(
                                                DataTable.this.getSelectionModel().getLeadSelectionIndex(), i);
                                    }
                                }

                                DataTable dataTable = new DataTable(new DataTableModel(tableItem1, primaryKey, indexColumn));
                                Main.drawTable(dataTable);
                                System.out.println(table + " " + getSelectionModel().getLeadSelectionIndex());
                            }
                            k++;
                        }
                    }
                }
            });
        }
    }
}
