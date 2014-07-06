package edu.dbframework.parse.parsers;

import edu.dbframework.parse.beans.DatabaseXMLBean;

/**
 * Created with IntelliJ IDEA.
 * User: GreenPoser
 * Date: 13.06.13
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseBeanParser extends AbstractParser {

    public DatabaseBeanParser(String fileName) {
        super(fileName);
    }

    @Override
    public DatabaseXMLBean getBeanFromXML() {
        return (DatabaseXMLBean) super.getBeanFromXML(DatabaseXMLBean.class);
    }
}
