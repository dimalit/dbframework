package edu.dbframework;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import edu.dbframework.database.MetadataDao;
import edu.dbframework.parse.beans.ConnectionXMLBean;
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
        bean.setDriver("com.mysql.jdbc.Driver");
        bean.setUser("root");
        bean.setPassword("root");
        bean.setUrl("jdbc:mysql://localhost:3306/sakila");

        AbstractParser ap = new AbstractParser("connectionConfig.xml");
        ap.addBeanToXML(bean);

        ap = new AbstractParser("database.xml");
        DatabaseXMLBean dbBean = new MetadataDao().createTablesXMLBean();
        ap.addBeanToXML(dbBean);
        TableItem ti = dbBean.getTables().get(1);*/

        AbstractParser ap = new AbstractParser("database.xml");
        DatabaseXMLBean xmlBean = (DatabaseXMLBean)ap.getBeanFromXML(DatabaseXMLBean.class);
        TableItem ti = xmlBean.getTables().get(1);

        SelectQuery query = new SelectQuery().addCustomColumns(ti.columnsAsStringArray());
        query.addCustomColumns(ti.relationColumnsAsStringArray());

        for (String fromTable : ti.relationTablesAsList()) {
            query.addCustomFromTable(fromTable);
        }

        query.addCustomJoin(SelectQuery.JoinType.INNER, ti.getName(), ti.getName(), BinaryCondition.equalTo(ti.getColumns().get(0), ti.getColumns().get(1)));
        System.out.print(query);

    }
}

