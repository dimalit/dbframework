package gui;

import gui.frame.MainFrame;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.awt.*;

public class Main {

	public static final String TABLES_XML = "database.xml";

    public static ApplicationContext context = new ClassPathXmlApplicationContext("databaseContext.xml");
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
                    UIManager.setLookAndFeel(
                            UIManager.getSystemLookAndFeelClassName());
                    MainFrame window = new MainFrame();
                    window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

    public static Object getBeanFromContext(String name) {
        return context.getBean(name);
    }
}
