package edu.gui.frame;

import edu.dbframework.parse.beans.ColumnItem;
import edu.dbframework.parse.helpers.DatabaseManager;
import edu.gui.table.DataTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class PredicatesDialog extends JDialog {

    DatabaseManager databaseManager = new DatabaseManager();

    static JPanel panel;
    static ArrayList<JTextField> predicates;

    public PredicatesDialog() {
        setTitle("Add predicates");
        setBounds(150, 150, 450, 350);
        init();
    }

    private void init() {
        panel = new JPanel(new GridBagLayout());
        predicates = new ArrayList<JTextField>(MainFrame.table.getColumnCount());
        DataTableModel model = (DataTableModel) MainFrame.table.getModel();
        List<ColumnItem> columns = model.getTableItem().getColumns();
        for (int i = 0; i < columns.size(); i++) {
            JLabel label = new JLabel(columns.get(i).getName());
            JTextField field = new JTextField();
            panel.add(label, new GridBagConstraints(0, i + 1, 1, 1, 0.3, 0, GridBagConstraints.EAST,
                    GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
            panel.add(field, new GridBagConstraints(1, i + 1, 1, 1, 0.5, 0, GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        }

        JButton applyButton = new JButton("Apply");
        panel.add(applyButton, new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 0.5, 0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PredicatesDialog.this.setVisible(false);
            }
        });
        panel.add(cancelButton, new GridBagConstraints(1, GridBagConstraints.RELATIVE, 1, 1, 0.5, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

        JScrollPane scrollPane = new JScrollPane(panel);
        this.setPreferredSize(scrollPane.getPreferredSize());
        this.add(scrollPane);
    }
}
