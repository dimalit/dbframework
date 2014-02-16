package gui.frame;

import gui.util.PropertyUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectionDialog extends JDialog {

    JPanel centerPanel;

    JTextField userTextField;
    JTextField passwordTextField;
    JTextField driverTextField;
    JTextField urlTextField;

    private PropertyUtility propUtil = PropertyUtility.getInstance();

    public ConnectionDialog() {
		setTitle("Create New Connection");
		setBounds(100, 100, 350, 250);
		getContentPane().setLayout(new BorderLayout());

		createCenterPanel();
		createBottomPanel();
	}

	private void createCenterPanel() {
        centerPanel = new JPanel();
		GridLayout gridCenterPanel = new GridLayout(0, 2, 20, 29);
		gridCenterPanel.setVgap(30);
		centerPanel.setLayout(gridCenterPanel);
		getContentPane().add(centerPanel, BorderLayout.CENTER);

        String userProperty = propUtil.getProperty(PropertyUtility.userProp);
        String passwordProperty = propUtil.getProperty(PropertyUtility.passwordProp);
        String driverProperty = propUtil.getProperty(PropertyUtility.driverProp);
        String urlProperty = propUtil.getProperty(PropertyUtility.urlProp);

		JLabel lblUser = new JLabel("User");
		lblUser.setSize(new Dimension(10, 50));
		lblUser.setHorizontalAlignment(SwingConstants.CENTER);
		centerPanel.add(lblUser);
		
		userTextField = new JTextField();
		userTextField.setHorizontalAlignment(SwingConstants.LEFT);
		userTextField.setText(userProperty != null ? userProperty : "");
		centerPanel.add(userTextField);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		centerPanel.add(lblPassword);
		
		passwordTextField = new JTextField();
		passwordTextField.setText(passwordProperty != null ? passwordProperty : "");
		centerPanel.add(passwordTextField);
		passwordTextField.setColumns(10);
		
		JLabel lblDriver = new JLabel("Driver");
		lblDriver.setHorizontalAlignment(SwingConstants.CENTER);
		centerPanel.add(lblDriver);
		
		driverTextField = new JTextField();
		driverTextField.setText(driverProperty != null ? driverProperty : "");
		centerPanel.add(driverTextField);
		driverTextField.setColumns(10);
		
		JLabel lblUrl = new JLabel("URL");
		lblUrl.setHorizontalAlignment(SwingConstants.CENTER);
		centerPanel.add(lblUrl);
		
		urlTextField = new JTextField();
		urlTextField.setText(urlProperty != null ? urlProperty : "");
		centerPanel.add(urlTextField);
		urlTextField.setColumns(10);
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
                    setConnectionProperties(user, password, driver, url);
                    //MainFrame frame = (MainFrame) Main.getBeanFromContext("mainFrame");
                    //frame.refreshNorthPanel();
                    ConnectionDialog.this.setVisible(false);
                }
            }

            private void setConnectionProperties(String user, String password, String driver, String url) {
                propUtil.setProperty(PropertyUtility.userProp, user);
                propUtil.setProperty(PropertyUtility.passwordProp, password);
                propUtil.setProperty(PropertyUtility.driverProp, driver);
                propUtil.setProperty(PropertyUtility.urlProp, url);
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
}
