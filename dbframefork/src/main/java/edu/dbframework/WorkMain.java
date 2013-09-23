package edu.dbframework;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import edu.dbframework.parse.beans.DatabaseXMLBean;
import edu.dbframework.parse.beans.items.TableItem;
import edu.dbframework.parse.parsers.AbstractParser;

/**
 * Created with IntelliJ IDEA.
 * User: bondarets
 * Date: 20.09.13
 * Time: 17:22
 * To change this template use File | Settings | File Templates.
 */
public class WorkMain {
    public static void main(String[] args) {

        /*ConnectionXMLBean bean = new ConnectionXMLBean();
        bean.setDriver("org.postgresql.Driver");
        bean.setUser("postgres");
        bean.setPassword("postgres123");
        bean.setUrl("jdbc:postgresql://localhost:5432/gatv");

        AbstractParser ap = new AbstractParser("connectionConfig.xml");
        ap.addBeanToXML(bean);

        DatabaseXMLBean dbBean = new MetadataDao().createTablesXMLBean();
        TableItem ti = dbBean.getTables().get(1);*/

        AbstractParser ap = new AbstractParser("database.xml");
        DatabaseXMLBean xmlBean = (DatabaseXMLBean)ap.getBeanFromXML(DatabaseXMLBean.class);
        TableItem ti = xmlBean.getTables().get(0);

        SelectQuery query = new SelectQuery().addCustomColumns(ti.columnsAsArray());

        for (String fromTable : ti.getRelationTables()) {
            query.addCustomFromTable(fromTable);
        }

        System.out.print(query);

    }
}

