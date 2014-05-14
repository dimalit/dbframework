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
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

public class MainFrame extends JFrame {

    static JMenuBar menuBar;
    static JLabel messageLabel;
    static JButton addPredicatesButton;
    static JList historyList;
    static JList tablesList;
    static DataTable table;

    static JPanel centerTablePanel;

    static DataTableManager dataTableManager = new DataTableManager();
    static DatabaseManager databaseManager = (DatabaseManager) Main.context.getBean("databaseManager");
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

        JMenuItem menuItem = new JMenuItem("Configure current database", KeyEvent.VK_T);
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ConfigDialog cd = new ConfigDialog(databaseManager.getDatabaseBean());
                cd.setVisible(true);
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Load config from file", KeyEvent.VK_M);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
                fc.setFileFilter(xmlfilter);
                int returnVal = fc.showOpenDialog(MainFrame.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    databaseManager.setParsingFile(file);
                    ConfigDialog cd = new ConfigDialog(databaseManager.getDatabaseBean());
                    cd.setVisible(true);
                }
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Save config", KeyEvent.CTRL_DOWN_MASK);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
                fc.setFileFilter(xmlfilter);
                fc.setCurrentDirectory(databaseManager.getParsingFile());
                int returnVal = fc.showSaveDialog(MainFrame.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        File savingFile = databaseManager.getParsingFile();

                        InputStream in = new FileInputStream(savingFile);
                        OutputStream out = new FileOutputStream(fc.getSelectedFile() + ".xml");
                        int len;
                        while ((len = in.read()) > 0) {
                            out.write(len);
                        }
                        in.close();
                        out.close();

                    } catch (FileNotFoundException e1) {
                        messageLabel.setText("Error with saving");
                    } catch (IOException e1) {
                        messageLabel.setText("Error with saving");
                    }
                }
            }
        });
        menu.add(menuItem);

        this.setJMenuBar(menuBar);
    }

    private void renderLists() {
        addPredicatesButton = new JButton("Add Predicates");
        addPredicatesButton.setEnabled(false);
        addPredicatesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PredicatesDialog predicatesDialog = new PredicatesDialog();
                predicatesDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                predicatesDialog.setVisible(true);
            }
        });
        this.getContentPane().add(addPredicatesButton, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.SOUTH,
                GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));

        messageLabel = new JLabel();
        this.getContentPane().add(messageLabel, new GridBagConstraints(1, 0, 0, 1, 1, 0, GridBagConstraints.CENTER,
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

        if (isActiveConnection()) {
            if (databaseManager.getDatabaseBean() == null) {
                MetadataDao metadataDao = (MetadataDao) Main.context.getBean("metadataDao");
                DatabaseBean xmlBean = metadataDao.createTablesXMLBean();
                databaseManager.setDatabaseBean(xmlBean);
            }
            tablesList.setListData(databaseManager.getDatabaseBean().tablesAsStringList().toArray());
        } else {
            messageLabel.setText("There is no connection. Please, configure connection properties and restart application.");
        }
    }

    private void renderTable() {
        table = new DataTable() {
            @Override
            public void setDataTableModel(TableModel dataModel) {
                super.setDataTableModel(dataModel);
                historyList.removeAll();
                historyList.setListData(sqlBuilder.getQueryMap().keySet().toArray());
                addPredicatesButton.setEnabled(true);
            }
        };
        centerTablePanel = new JPanel();
        centerTablePanel.setPreferredSize(new Dimension(800, 600));
        this.getContentPane().add(centerTablePanel, new GridBagConstraints(1, 1, 1, 6, 10, 10, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    }

    private void drawTable(JTable table) {
        centerTablePanel.removeAll();
        centerTablePanel.revalidate();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(centerTablePanel.getSize());
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
