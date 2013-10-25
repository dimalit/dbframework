package edu.dbframework.database;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Dao {

    private DataSource dataSource;

    public Dao() {
    }

    public Map<String, List<String>> getData(final String query) {

        final ResultSetExtractor<Map<String, List<String>>> rse = new ResultSetExtractor<Map<String, List<String>>>() {
            @Override
            public Map<String, List<String>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                Map<String, List<String>> data = new HashMap<String, List<String>>();

                ResultSetMetaData rsmd = resultSet.getMetaData();
                int columnsCount = rsmd.getColumnCount();

                for (int i = 1; i <= columnsCount; i++) {
                    ArrayList<String> colData = new ArrayList<String>();
                    if (resultSet.first()) {
                        while (resultSet.next()) {
                            colData.add(resultSet.getString(i));
                        }
                        data.put(rsmd.getColumnLabel(i), colData);
                        resultSet.beforeFirst();
                    }
                }
                return data;
            }
        };

        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                return connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            }
        };

        PreparedStatementCallback<Map<String, List<String>>> pscb = new PreparedStatementCallback<Map<String, List<String>>>() {
            @Override
            public Map<String, List<String>> doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                return rse.extractData(preparedStatement.executeQuery());
            }
        };

        return new JdbcTemplate(dataSource).execute(psc, pscb);
    }
    /*public Map<String, List<String>> getData(String query) {
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
    }*/

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
