package edu.gui.frame;

import edu.dbframework.database.MetadataDao;
import edu.dbframework.database.SqlQueryBuilder;
import edu.dbframework.database.TableHistoryBean;
import edu.dbframework.parse.beans.DatabaseBean;
import edu.dbframework.parse.beans.TableItem;
import edu.dbframework.parse.helpers.DatabaseManager;
import edu.gui.Main;
import edu.gui.table.DataTable;
import edu.gui.table.DataTableManager;

import javax.sql.DataSource;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class MainFrame extends JFrame {

    static JMenuBar menuBar;
    static JButton loadTablesButton;
    static JList historyList;
    static JList tablesList;
    static DataTable table;

    static JPanel centerTablePanel;

    static DatabaseManager databaseManager = new DatabaseManager();
    static DataTableManager dataTableManager = new DataTableManager();
    static SqlQueryBuilder sqlBuilder = (SqlQueryBuilder) Main.context.getBean("sqlBuilder");

    public MainFrame() {
        super();
        setTitle("DBFramework");
        setBounds(new Rectangle(100, 100, 800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new GridBagLayout());
        initialize();
    }

    private void initialize() {
        renderMenuBar();
        renderLists();
        renderTable();
    }

    private void renderMenuBar() {
        menuBar = new JMenuBar();

        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu);

        JMenuItem menuItem = new JMenuItem("New Database Config", KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                createTablesXMLFile();
                openTablesXMLFile();
            }

            private void createTablesXMLFile() {
                MetadataDao metadataDao = (MetadataDao) Main.context.getBean("metadataDao");
                DatabaseBean xmlBean = metadataDao.createTablesXMLBean();
                databaseManager.setDatabaseBean(xmlBean);
            }

            private void openTablesXMLFile() {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.open(new File(Main.TABLES_XML));
                } catch (IOException e1) {
                    // exception-label
                }
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Open Database Config", KeyEvent.VK_M);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
                fc.setFileFilter(xmlfilter);
                int returnVal = fc.showOpenDialog(MainFrame.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.open(file);
                    } catch (IOException e1) {
                        // exception-label
                    }
                }

            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("New Connection", KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConnectionDialog connectionDialog = new ConnectionDialog();
                connectionDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                connectionDialog.setVisible(true);
            }
        });
        menu.add(menuItem);

        this.setJMenuBar(menuBar);
    }

    private void renderLists() {
        loadTablesButton = new JButton("Load Tables");
        loadTablesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (databaseManager.getDatabaseBean() != null)
                    tablesList.setListData(databaseManager.getDatabaseBean().tablesAsStringList().toArray());
            }
        });
        this.getContentPane().add(loadTablesButton, new GridBagConstraints(1, 0, 1, 1, 3, 0,  GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));

        // history list
        JLabel historyLabel = new JLabel("History");
        this.getContentPane().add(historyLabel, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));

        historyList = new JList();
        historyList.setBorder(BorderFactory.createLineBorder(Color.gray));
        historyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        historyList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && historyList.getSelectedValue() != null) {
                    String value = (String) historyList.getSelectedValue();
                    TableHistoryBean bean = sqlBuilder.getQueryMap().get(value);
                    table.setDataTableModel(dataTableManager.getDataModelBySqlQuery(bean.getTableItem(), bean.getQuery()));
                    drawTable(table);
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(historyList);
        this.getContentPane().add(scrollPane, new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 2, 2, 4, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));

        // tables list
        JLabel tablesListLabel = new JLabel("Tables");
        this.getContentPane().add(tablesListLabel, new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));

        tablesList = new JList();
        tablesList.setBorder(BorderFactory.createLineBorder(Color.gray));
        tablesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablesList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedTable = (String) tablesList.getSelectedValue();
                    if (databaseManager.getDatabaseBean() != null) {
                        TableItem tableItem = databaseManager.getDatabaseBean().createTablesMap().get((selectedTable));
                        table.setDataTableModel(dataTableManager.getTableItemDataModel(tableItem));
                        drawTable(table);
                    }
                }
            }
        });

        scrollPane = new JScrollPane(tablesList);
        this.getContentPane().add(scrollPane, new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 2, 2, 6, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));
    }

    private void renderTable() {
        table = new DataTable() {
            @Override
            public void setDataTableModel(TableModel dataModel) {
                super.setDataTableModel(dataModel);
                MainFrame.this.historyList.removeAll();
                MainFrame.this.historyList.setListData(sqlBuilder.getQueryMap().keySet().toArray());
            }
        };
        centerTablePanel = new JPanel();
        this.getContentPane().add(centerTablePanel, new GridBagConstraints(1, 1, 1, 6, 10, 10, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    }

    private void drawTable(JTable table) {
        centerTablePanel.removeAll();
        centerTablePanel.revalidate();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(centerTablePanel.getWidth(), centerTablePanel.getHeight()));
        centerTablePanel.add(scrollPane);
    }

    private static boolean isActiveConnection() {
        DataSource dataSource = (DataSource) Main.context.getBean("dataSource");
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            return false;
        }
        return connection != null;
    }

/*    public static void refreshNorthPanel() {
        northButtonPanel.removeAll();
        if (isActiveConnection()) {
            loadTablesButton = new JButton("Load Tables");
            loadTablesButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (databaseManager.getDatabaseBean() != null)
                        tablesList.setListData(databaseManager.getDatabaseBean().tablesAsStringList().toArray());
                }
            });
            northButtonPanel.add(loadTablesButton);
        } else {
            JLabel label = new JLabel();
            label.setText("There is no active connection. Please, configure it in file menu.");
            northButtonPanel.add(label);
        }
    }*/
}
