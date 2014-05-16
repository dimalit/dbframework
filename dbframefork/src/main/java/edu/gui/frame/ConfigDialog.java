package edu.gui.frame;

import edu.dbframework.parse.beans.ColumnItem;
import edu.dbframework.parse.beans.DatabaseBean;
import edu.dbframework.parse.beans.TableItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ConfigDialog extends JDialog {

    DatabaseBean databaseBean;

    JPanel topPanel;
    JPanel centerPanel;
    JPanel bottomPanel;

    JComboBox tableComboBox;
    JCheckBox tableEnabledFl;

    ArrayList<JTextField> aliasFields;
    ArrayList<JTextField> relationTableFields;
    ArrayList<JTextField> relationColumnFields;
    ArrayList<JCheckBox> columnEnabledFlags;

    public ConfigDialog(DatabaseBean databaseBean) {
        this.databaseBean = databaseBean;
        this.getContentPane().setLayout(new BorderLayout());

        setTitle("Config database view");
        setBounds(150, 150, 640, 480);
        init();
    }

    private void init() {
        tableComboBox = new JComboBox(databaseBean.tablesAsStringList().toArray());
        tableComboBox.addItemListener(new TableComboBoxActionListener());

        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(tableComboBox);
        this.getContentPane().add(topPanel, BorderLayout.NORTH);

        centerPanel = new JPanel(new GridBagLayout());
        renderPanel(databaseBean.getTableByName((String) tableComboBox.getSelectedItem()));

        JButton applyButton = new JButton("Apply");
        applyButton.addMouseListener(new ApplyButtonMouseListener());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ConfigDialog.this.setVisible(false);
            }
        });

        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(applyButton);
        bottomPanel.add(cancelButton);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

    }

    private void renderPanel(TableItem tableItem) {

        BorderLayout layout = (BorderLayout) this.getContentPane().getLayout();
        if (layout.getLayoutComponent(BorderLayout.CENTER) != null) {
            this.getContentPane().remove(layout.getLayoutComponent(BorderLayout.CENTER));
        }

        centerPanel.removeAll();
        centerPanel.revalidate();

        JLabel header = new JLabel("Alias");
        centerPanel.add(header, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        header = new JLabel("Relation Table Name");
        centerPanel.add(header, new GridBagConstraints(2, 0, 1, 1, 1, 0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        header = new JLabel("Relation Column Name");
        centerPanel.add(header, new GridBagConstraints(3, 0, 1, 1, 1, 0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));


        aliasFields = new ArrayList<JTextField>(tableItem.getColumns().size());
        relationTableFields = new ArrayList<JTextField>(tableItem.getColumns().size());
        relationColumnFields = new ArrayList<JTextField>(tableItem.getColumns().size());

        for (int i = 0; i < tableItem.getColumns().size(); i++) {
            ColumnItem column = tableItem.getColumns().get(i);

            JTextField aliasField = new JTextField();
            JTextField relTableField = new JTextField();
            JTextField relColField = new JTextField();

            aliasField.setText(column.getAlias() != null ? column.getAlias() : "");
            relTableField.setText(column.getRelationTableName() != null ? column.getRelationTableName() : "");
            relColField.setText(column.getRelationColumnName() != null ? column.getRelationColumnName() : "");

            aliasFields.add(aliasField);
            relationTableFields.add(relTableField);
            relationColumnFields.add(relColField);

            JLabel columnNameLabel = new JLabel(column.getName());

            centerPanel.add(columnNameLabel, new GridBagConstraints(0, i + 1, 1, 1, 1, 0, GridBagConstraints.CENTER,
                    GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
            centerPanel.add(aliasField, new GridBagConstraints(1, i + 1, 1, 1, 1, 0, GridBagConstraints.CENTER,
                    GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
            centerPanel.add(relTableField, new GridBagConstraints(2, i + 1, 1, 1, 1, 0, GridBagConstraints.CENTER,
                    GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
            centerPanel.add(relColField, new GridBagConstraints(3, i + 1, 1, 1, 1, 0, GridBagConstraints.CENTER,
                    GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        }

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
        this.getContentPane().validate();
    }

    private class TableComboBoxActionListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                String tableName = (String) e.getItem();
                updateBean(tableName);
            } else if (e.getStateChange() == ItemEvent.SELECTED) {
                String tableName = (String) e.getItem();
                renderPanel(databaseBean.getTableByName(tableName));
            }
        }
    }

    private class ApplyButtonMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            String tableName = (String) tableComboBox.getSelectedItem();
            updateBean(tableName);
            ConfigDialog.this.setVisible(false);
        }
    }

    private void updateBean(String tableName) {
        TableItem tableItem = databaseBean.getTableByName(tableName);

        for (int i = 0; i < tableItem.getColumns().size(); i++) {
            ColumnItem columnItem = tableItem.getColumns().get(i);

            String aliasValue = aliasFields.get(i).getText();
            String relationTableName = relationTableFields.get(i).getText();
            String relationColumnName = relationColumnFields.get(i).getText();

            if (aliasValue != null && aliasValue.length() > 0) {
                columnItem.setAlias(aliasValue);
            }
            if (relationTableName != null && relationTableName.length() > 0) {
                columnItem.setRelationTableName(relationTableName);
            }
            if (relationColumnName != null && relationColumnName.length() > 0) {
                columnItem.setRelationColumnName(relationColumnName);
            }
        }
        MainFrame.databaseManager.setDatabaseBean(databaseBean);
    }

}
