package edu.dbframework.parse.parsers;

import edu.dbframework.parse.beans.ConnectionXMLBean;

/**
 * Created with IntelliJ IDEA.
 * User: GreenPoser
 * Date: 13.06.13
 * Time: 22:30
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionBeanParser extends AbstractParser {

    public ConnectionBeanParser(String fileName) {
        super(fileName);
    }

    @Override
    public ConnectionXMLBean getBeanFromXML() {
        return (ConnectionXMLBean) super.getBeanFromXML(ConnectionXMLBean.class);
    }
}
