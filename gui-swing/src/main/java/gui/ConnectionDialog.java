package gui;

import edu.dbframework.parse.beans.ConnectionXMLBean;
import edu.dbframework.parse.parsers.ConnectionBeanParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ConnectionDialog extends JDialog {
	
	private static final String CONNECTION_CONFIG_XML = "connectionConfig.xml";
	private JTextField userTextField;
	private JTextField passwordTextField;
	private JTextField driverTextField;
	private JTextField urlTextField;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			ConnectionDialog dialog = new ConnectionDialog();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public ConnectionDialog() {
		setTitle("Connection Config Modal");
		setBounds(100, 100, 350, 250);
		getContentPane().setLayout(new BorderLayout());

		createCenterPanel();
		createBottomPanel();
	}

	private void createBottomPanel() {
		JPanel southButtonPane = new JPanel();	
		southButtonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(southButtonPane, BorderLayout.SOUTH);

		final JLabel messageLabel = new JLabel();
		southButtonPane.add(messageLabel, FlowLayout.LEFT);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				messageLabel.setText("");
				String user = userTextField.getText();
				String password = passwordTextField.getText();
				String driver = driverTextField.getText();
				String url = urlTextField.getText();
				if ((user == null || user.equals(""))
						|| (password == null ||password.equals(""))
						|| (driver == null || driver.equals(""))
						|| (url == null || url.equals(""))) {
					messageLabel.setText("Required Field Missed");
				} else {
					createConnectionConfig(user, password, driver, url);
					ConnectionDialog.this.setVisible(false);
				}
			}

			private void createConnectionConfig(String user, String password, String driver, String url) {
				ConnectionXMLBean xmlBean = new ConnectionXMLBean(driver, url, user, password);
				ConnectionBeanParser parser = new ConnectionBeanParser(CONNECTION_CONFIG_XML);
				parser.addBeanToXML(xmlBean);
			}
		});
		
		okButton.setActionCommand("OK");
		southButtonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConnectionDialog.this.setVisible(false);
			}
		});
		cancelButton.setActionCommand("Cancel");
		southButtonPane.add(cancelButton);
	}

	private void createCenterPanel() {
		JPanel centerPanel = new JPanel();
		GridLayout gl_centerPanel = new GridLayout(0, 2, 20, 29);
		gl_centerPanel.setVgap(30);
		centerPanel.setLayout(gl_centerPanel);
		getContentPane().add(centerPanel, BorderLayout.CENTER);
		
		ConnectionXMLBean xmlBean = null;
        ConnectionBeanParser parser = new ConnectionBeanParser(CONNECTION_CONFIG_XML);
		if (new File(CONNECTION_CONFIG_XML).exists())
			xmlBean = parser.getBeanFromXML();
		
		JLabel lblUser = new JLabel("User");
		lblUser.setSize(new Dimension(10, 50));
		lblUser.setHorizontalAlignment(SwingConstants.CENTER);
		centerPanel.add(lblUser);
		
		userTextField = new JTextField();
		userTextField.setHorizontalAlignment(SwingConstants.LEFT);
		userTextField.setText(xmlBean != null ? xmlBean.getUser() : "");
		centerPanel.add(userTextField);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		centerPanel.add(lblPassword);
		
		passwordTextField = new JTextField();
		passwordTextField.setText(xmlBean!= null ? xmlBean.getPassword() : "");
		centerPanel.add(passwordTextField);
		passwordTextField.setColumns(10);
		
		JLabel lblDriver = new JLabel("Driver");
		lblDriver.setHorizontalAlignment(SwingConstants.CENTER);
		centerPanel.add(lblDriver);
		
		driverTextField = new JTextField();
		driverTextField.setText(xmlBean != null ? xmlBean.getDriver() : "");
		centerPanel.add(driverTextField);
		driverTextField.setColumns(10);
		
		JLabel lblUrl = new JLabel("URL");
		lblUrl.setHorizontalAlignment(SwingConstants.CENTER);
		centerPanel.add(lblUrl);
		
		urlTextField = new JTextField();
		urlTextField.setText(xmlBean != null ? xmlBean.getUrl() : "");
		centerPanel.add(urlTextField);
		urlTextField.setColumns(10);
	}
}
