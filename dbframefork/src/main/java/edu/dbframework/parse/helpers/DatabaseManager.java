package edu.dbframework.parse.helpers;

import edu.dbframework.parse.beans.DatabaseBean;
import edu.dbframework.parse.parsers.GenericParser;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: GreenPoser
 * Date: 13.06.13
 * Time: 23:02
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseManager {

    private File parsingFile;
    private DatabaseBean databaseBean;
    private GenericParser parser;

    public DatabaseManager() {
        parser = new GenericParser("database.xml");
        this.parsingFile = parser.getParsingFile();
        if (parsingFile.exists())
            this.databaseBean = (DatabaseBean) parser.getBeanFromXML(DatabaseBean.class);
    }

    public DatabaseManager(File parsingFile) {
        this.parsingFile = parsingFile;
        parser = new GenericParser("database.xml");
        if (parsingFile.exists())
            this.databaseBean = (DatabaseBean) parser.getBeanFromXML(DatabaseBean.class);
    }

    public DatabaseBean getDatabaseBean() {
        if (!parser.getBeanFromXML(DatabaseBean.class).equals(this.databaseBean))
            this.databaseBean = (DatabaseBean) parser.getBeanFromXML(DatabaseBean.class);
        return databaseBean;
    }

    public void setDatabaseBean(DatabaseBean databaseBean) {
        this.parser.addBeanToXML(databaseBean);
        this.databaseBean = databaseBean;
    }
}
