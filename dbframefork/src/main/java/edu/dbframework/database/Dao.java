package edu.dbframework.database;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class Dao {

    private DataSource dataSource;

    public Dao() {
    }

    public Map<String, List<String>> getData(String query) {
        ResultSet rs = null;
        Statement stmt = null;
        Connection con = null;
        Map<String, List<String>> data = null;

        try {
            con = dataSource.getConnection();
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery(query.toString());
            data = new LinkedHashMap<String, List<String>>();

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsCount = rsmd.getColumnCount();

            for (int i = 1; i <= columnsCount; i++) {
                ArrayList<String> colData = new ArrayList<String>();
                if (rs.first()) {
                    while (rs.next()) {
                        colData.add(rs.getString(i));
                    }
                    data.put(rsmd.getColumnLabel(i), colData);
                    rs.beforeFirst();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Exception in Dao.getData(tableItem)", e);
        }
       return data;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}