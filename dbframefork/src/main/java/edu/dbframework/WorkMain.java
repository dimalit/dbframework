package edu.dbframework;

import edu.dbframework.database.SqlQueryBuilder;
import edu.dbframework.parse.beans.DatabaseBean;
import edu.dbframework.parse.beans.items.TableItem;
import edu.dbframework.parse.parsers.GenericParser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bondarets
 * Date: 20.09.13
 * Time: 17:22
 * To change this template use File | Settings | File Templates.
 */
public class WorkMain {
    public static void main(String[] args) throws SQLException, MetaDataAccessException {

        /*ConnectionBean bean = new ConnectionBean();
        bean.setDriver("com.mysql.jdbc.Driver");
        bean.setUser("root");
        bean.setPassword("root");
        bean.setUrl("jdbc:mysql://localhost:3306/sakila");

        GenericParser ap = new GenericParser("connectionConfig.xml");
        ap.addBeanToXML(bean);

        ap = new GenericParser("database.xml");
        DatabaseBean dbBean = new MetadataDao().createTablesXMLBean();
        ap.addBeanToXML(dbBean);
        TableItem ti = dbBean.getTa bles().get(1);*/

        GenericParser ap = new GenericParser("database.xml");
        DatabaseBean xmlBean = (DatabaseBean)ap.getBeanFromXML(DatabaseBean.class);
        TableItem ti = xmlBean.getTables().get(1);

        SqlQueryBuilder sqb = new SqlQueryBuilder();


//        // --------- test query for table item
//        System.out.print(sqb.buildQueryForTableItem(ti));


        // --------- test query by rows
/*        List<String> rows = new ArrayList<String>();
        rows.add("lol");
        rows.add("ppwwppw");

        System.out.print(sqb.buildQueryByRows(ti, rows, ti.getColumns().get(1), "relTable"));*/

        /*Dao dao = new Dao();
        System.out.print(dao.getData(sqb.buildQueryForTableItem(ti)));*/

        //----------------------------------Testing Spting------------------------------------------------------------

        ApplicationContext context =
                new ClassPathXmlApplicationContext("databaseContext.xml");

        DataSource dataSource = (DataSource) context.getBean("dataSource");
        Collection col = (Collection) JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback() {
            @Override
            public Object processMetaData(DatabaseMetaData databaseMetaData) throws SQLException, MetaDataAccessException {

                ResultSetExtractor rse = new ResultSetExtractor<List<String>>() {
                    @Override
                    public List<String> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                        List<String> list = new ArrayList<String>();
                        while (resultSet.next()) {
                            list.add(resultSet.getString("TABLE_NAME"));
                        }
                        return list;
                    }
                };
                return rse.extractData(databaseMetaData.getTables(null, null, null, new String[] {"TABLE"}));
            }
        });

        System.out.print(col);
    }
}

