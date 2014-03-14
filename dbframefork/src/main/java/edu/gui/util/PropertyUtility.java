package edu.gui.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: GreenPoser
 * Date: 29.01.14
 * Time: 21:25
 * To change this template use File | Settings | File Templates.
 */
public class PropertyUtility {

    private final static String propertyPath = "target/classes/jdbc.properties";

    public final static String userProp = "jdbc.username";
    public final static String passwordProp = "jdbc.password";
    public final static String driverProp = "jdbc.driverClassName";
    public final static String urlProp = "jdbc.url";

    private Properties properties = new Properties();

    private static PropertyUtility instance;

    private PropertyUtility() {
        InputStream input = null;
        try {
            input = new FileInputStream(propertyPath);
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PropertyUtility getInstance() {
        if (instance == null) {
            instance = new PropertyUtility();
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(propertyPath);
            properties.setProperty(key, value);
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
