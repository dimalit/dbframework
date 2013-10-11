package edu.dbframework;

import edu.dbframework.database.ConnectionUtils;
import edu.dbframework.database.Dao;
import edu.dbframework.database.SqlQueryBuilder;
import edu.dbframework.parse.beans.DatabaseXMLBean;
import edu.dbframework.parse.beans.items.TableItem;
import edu.dbframework.parse.parsers.AbstractParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bondarets
 * Date: 20.09.13
 * Time: 17:22
 * To change this template use File | Settings | File Templates.
 */
public class WorkMain {
    public static void main(String[] args) throws SQLException {

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

        SqlQueryBuilder sqb = new SqlQueryBuilder();


        // --------- test query for table item
        //System.out.print(sqb.buildQueryForTableItem(ti));


        // --------- test query by rows
        /*List<String> rows = new ArrayList<String>();
        rows.add("lol");
        rows.add("ppwwppw");

        System.out.print(sqb.buildQueryByRows(ti, rows, ti.getColumns().get(1), "relTable"));*/

        Dao dao = new Dao();
        System.out.print(dao.getData(sqb.buildQueryForTableItem(ti)));
    }
}

