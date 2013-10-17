package gui;

import edu.dbframework.database.MetadataDao;
import edu.dbframework.parse.beans.DatabaseBean;
import edu.dbframework.parse.beans.items.TableItem;
import edu.dbframework.parse.helpers.DatabaseManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Main {

	public static final String TABLES_XML = "database.xml";

	private JFrame frame;
	private JList tablesList;
    static JPanel centerTablePanel;

	private final DatabaseManager databaseManager = new DatabaseManager();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
                    UIManager.setLookAndFeel(
                            UIManager.getSystemLookAndFeelClassName());
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("DBFramework");
		frame.setBounds(new Rectangle(100, 100, 900, 700));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		createNorthPanel();
        createWestPanel();
        createTablePanel();
	}

	private void createNorthPanel() {
        JButton connectionModalButton;
        JButton generateTablesXMLButton;
        JButton openTablesButton;
        JButton loadTablesButton;

		JPanel northButtonPanel = new JPanel();
		northButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		frame.getContentPane().add(northButtonPanel,
				BorderLayout.NORTH);

		connectionModalButton = new JButton("Enter Connection Config");
		connectionModalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConnectionDialog connectionDialog = new ConnectionDialog();
				connectionDialog
						.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				connectionDialog.setVisible(true);
			}
		});
		northButtonPanel.add(connectionModalButton);

		generateTablesXMLButton = new JButton("Generate XML Table File");
		generateTablesXMLButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				createTablesXMLFile();
				openTablesXMLFile();
			}

			private void openTablesXMLFile() {
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.open(new File(TABLES_XML));
				} catch (IOException e1) {
					// exception-label
				}
			}

			private void createTablesXMLFile() {
                MetadataDao metadataDao = new MetadataDao();
                DatabaseBean xmlBean = metadataDao.createTablesXMLBean();
				databaseManager.setDatabaseBean(xmlBean);
			}
		});
		northButtonPanel.add(generateTablesXMLButton);

		openTablesButton = new JButton("Open Tables file");
		openTablesButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				openTablesXMLFile();
			}

			private void openTablesXMLFile() {
				Desktop desktop = Desktop.getDesktop();
				try {
					File tablesXML = new File(TABLES_XML);
					if (tablesXML.exists())
						desktop.open(tablesXML);
				} catch (IOException e1) {
					// exception-label
				}
			}
		});
		northButtonPanel.add(openTablesButton);

		loadTablesButton = new JButton("Load Tables");
		loadTablesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                if (databaseManager.getDatabaseBean() != null)
					tablesList.setListData(databaseManager.getDatabaseBean().tablesAsStringList().toArray());
				}
		});
		northButtonPanel.add(loadTablesButton);
	}

	private void createWestPanel() {
		JPanel westListPanel = new JPanel();
		westListPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		frame.getContentPane().add(westListPanel, BorderLayout.WEST);
		createTablesList();
		westListPanel.add(tablesList);
	}

	private void createTablePanel() {
		centerTablePanel = new JPanel();
		centerTablePanel.setLayout(new FlowLayout());
		frame.getContentPane().add(centerTablePanel, BorderLayout.CENTER);
	}

	private void createTablesList() {
		tablesList = new JList();
		tablesList.setBorder(BorderFactory.createLineBorder(Color.gray));
		tablesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablesList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    String selectedTable = (String) tablesList.getSelectedValue();
                    if (databaseManager.getDatabaseBean() != null) {
                        TableItem tableItem = databaseManager.getDatabaseBean().createTablesMap().get((selectedTable));
                        DataTable table = new DataTable(new DataTableModel(tableItem));
                        drawTable(table);
                    }
                }
            }
        });
    }

    public static void drawTable(JTable table) {
        centerTablePanel.revalidate();
        centerTablePanel.removeAll();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(centerTablePanel.getWidth(), centerTablePanel.getHeight()));
        centerTablePanel.add(scrollPane);
        table.setVisible(true);
    }
}
