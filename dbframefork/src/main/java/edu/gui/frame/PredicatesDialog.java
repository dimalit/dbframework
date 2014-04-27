package edu.gui.frame;

import edu.dbframework.parse.beans.ColumnItem;
import edu.dbframework.parse.helpers.DatabaseManager;
import edu.gui.table.DataTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class PredicatesDialog extends JDialog {

    DatabaseManager databaseManager = new DatabaseManager();

    static JPanel panel;
    static ArrayList<JTextField> predicates;

    public PredicatesDialog() {
        setTitle("Create New Connection");
        setBounds(150, 150, 450, 350);
        init();
    }

    private void init() {
        panel = new JPanel(new GridBagLayout());
        predicates = new ArrayList<JTextField>(MainFrame.table.getColumnCount());
        JLabel label;
        JTextField field;
        DataTableModel model = (DataTableModel) MainFrame.table.getModel();
        List<ColumnItem> columns = model.getTableItem().getColumns();
        for (int i = 0; i < columns.size(); i++) {
            label = new JLabel(columns.get(i).getName());
            field = new JTextField();

            panel.add(label, new GridBagConstraints(0, i, 1, 1, 0.2, 0, GridBagConstraints.EAST,
                    GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
            panel.add(field, new GridBagConstraints(1, i, 1, 1, 0.5, 0, GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        }
        JScrollPane scrollPane = new JScrollPane(panel);
        this.add(scrollPane);
    }
}
