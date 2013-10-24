package edu.dbframework.database;

import edu.dbframework.parse.beans.DatabaseBean;
import edu.dbframework.parse.beans.items.ColumnItem;
import edu.dbframework.parse.beans.items.TableItem;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author GreenPoser
 * 
 */
public class MetadataDao {

    private DataSource dataSource;

    private final static String TABLE = "TABLE";
    private final static String VIEW = "VIEW";

    private final static String TABLE_NAME = "TABLE_NAME";
    private final static String COLUMN_NAME = "COLUMN_NAME";


	public MetadataDao() {
	}

	public List<String> getTables() {
        List<String> tables = null;
        try {
            tables = (List<String>) JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback() {
                @Override
                public Object processMetaData(DatabaseMetaData databaseMetaData) throws SQLException, MetaDataAccessException {
                    MetadataResultSetExtractor mrse = new MetadataResultSetExtractor(TABLE_NAME);
                    return mrse.extractData(databaseMetaData.getTables(null, null, null, new String[]{TABLE}));
                }
            });
        } catch (MetaDataAccessException e) {
            throw new RuntimeException("Exception in MetadataDao.getTables", e);
        }
        return tables;
	}

	public List<String> getViews() {
        List<String> views = null;
        try {
            views = (List<String>) JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback() {
                @Override
                public Object processMetaData(DatabaseMetaData databaseMetaData) throws SQLException, MetaDataAccessException {
                    MetadataResultSetExtractor mrse = new MetadataResultSetExtractor(TABLE_NAME);
                    return mrse.extractData(databaseMetaData.getTables(null, null, null, new String[]{VIEW}));
                }
            });
        } catch (MetaDataAccessException e) {
            throw new RuntimeException("Exception in MetadataDao.getViews", e);
        }
        return views;
	}

	public List<String> getColumns(final String table) {
        List<String> columns = null;
        try {
            columns = (List<String>) JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback() {
                @Override
                public Object processMetaData(DatabaseMetaData databaseMetaData) throws SQLException, MetaDataAccessException {
                    MetadataResultSetExtractor mrse = new MetadataResultSetExtractor(COLUMN_NAME);
                    return mrse.extractData(databaseMetaData.getColumns(null, null, table, null));
                }
            });
        } catch (MetaDataAccessException e) {
            throw new RuntimeException("Exception in MetadataDao.getColumns", e);
        }
        return columns;
	}

    public List<String> getPrimaryKeyColumns(final String table) {
        List<String> primaryKeyColumns = null;
        try {
            primaryKeyColumns = (List<String>) JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback() {
                @Override
                public Object processMetaData(DatabaseMetaData databaseMetaData) throws SQLException, MetaDataAccessException {
                    MetadataResultSetExtractor mrse = new MetadataResultSetExtractor(COLUMN_NAME);
                    return mrse.extractData(databaseMetaData.getPrimaryKeys(null, null, table));
                }
            });
        } catch (MetaDataAccessException e) {
            throw new RuntimeException("Exception in MetadataDao.getPrimaryKeyColumns", e);
        }
        return primaryKeyColumns;
    }

	public DatabaseBean createTablesXMLBean() {
		DatabaseBean xmlBean = new DatabaseBean();
		List<TableItem> tables = new ArrayList<TableItem>();
		for (String table : getTables()) {
            List<String> primaryKeys = this.getPrimaryKeyColumns(table);
			TableItem item = new TableItem();
			item.setName(table);
			List<ColumnItem> columns = new ArrayList<ColumnItem>();
			for (String column : getColumns(table)) {
				ColumnItem columnItem = new ColumnItem();
				columnItem.setName(column);
                for (String primaryKey : primaryKeys) {
                    if (column.equals(primaryKey))
                        columnItem.setPrimaryKey(true);
                }
				columns.add(columnItem);
			}
			item.setColumns(columns);
			tables.add(item);
		}
		xmlBean.setTables(tables);
		return xmlBean;
	}

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private class MetadataResultSetExtractor implements ResultSetExtractor<List<String>> {
        private String field;

        MetadataResultSetExtractor(String field) {
            this.field = field;
        }

        @Override
        public List<String> extractData(ResultSet resultSet)  {
            List<String> list = new ArrayList<String>();
            try {
                while (resultSet.next()) {
                    list.add(resultSet.getString(field));
                }
            } catch (SQLException e) {
                throw new RuntimeException("Exception in MetadataResultSetExtractor.extractData", e);
            }
            return list;
        }
    }
}
