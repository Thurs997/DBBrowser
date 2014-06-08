package pl.edu.pw.ii.DBBrowser.RequestProcessor;

import pl.edu.pw.ii.DBBrowser.Configuration;

import java.sql.*;
import java.util.*;

final public class DBConnectionManager {

    //database drivers - supported
    private static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

    private Driver driver = null;
    private Connection connection = null;
    private String dbType;
    private String dbUrl;
    private String dbUser;

    public String getUrl() {
        return dbUrl;
    }

    public String getUser(){
        return dbUser;
    }

    public boolean connect(String userName, String password) {
        try {
            if(connection != null && !connection.isClosed())
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbType = Configuration.getInstance().getProperty("dbType");
        dbUrl = Configuration.getInstance().getProperty("dbUrl");

        String databaseDriverClass;
        if (dbType.equals("mysql"))
            databaseDriverClass = MYSQL_DRIVER;
        else if (dbType.equals("oracle"))
            databaseDriverClass = ORACLE_DRIVER;
        else
            return false;

        try {
            driver = (Driver) Class.forName(databaseDriverClass).newInstance();

            Properties dbProperties = new Properties();
            dbProperties.put("user", userName);
            dbProperties.put("password", password);
            connection = driver.connect(dbUrl, dbProperties);
            this.dbUser = userName;
            return true;
        } catch (ClassNotFoundException e) {
            System.out.println("Unable to load driver class");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println("Access problem while loading");
            e.printStackTrace();
        } catch (InstantiationException e) {
            System.out.println("Unable to instantiate driver");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public List<String> executeListDatabases() throws SQLException {
        List<String> databasesList = new ArrayList();
        ResultSet rs = null;
        if(dbType.equals("mysql"))
            rs = connection.getMetaData().getCatalogs();
        else
            rs = connection.getMetaData().getSchemas();

        while(rs.next()){
            String db = rs.getString(1);
            databasesList.add(db);
        }

        return databasesList;
    }

    public List<String> executeListTables(String databaseName) throws SQLException {
        List<String> tablesList = new ArrayList();
        ResultSet rs = null;

        if(dbType.equals("mysql"))
            rs = connection.getMetaData().getTables(databaseName, null, "%", null);
        else
            rs = connection.getMetaData().getTables(null, databaseName, "%", null);


        while(rs.next())
        {
            String table = rs.getString(3);
            tablesList.add(table);
        }

        return tablesList;
    }

    public String[][] executeListTableContent(String dbName, String tableName) throws SQLException {
        if(dbType.equals("mysql"))
            connection.setCatalog(dbName);
        else
            connection.setSchema(dbName);

        //execute actual query
        PreparedStatement sql = connection.prepareStatement("select * from " + tableName);
        ResultSet rs = sql.executeQuery();

        //get columns and columns count
        int columnsCount = rs.getMetaData().getColumnCount();

        //table with results: first row contains columns names, rest - query results
        List<String[]> result = new ArrayList<String[]>();

        result.add(new String[columnsCount]);
        for (int i=0;i<columnsCount;i++)
        {
            String columnName = rs.getMetaData().getColumnName(i + 1);
            result.get(0)[i] = columnName;
        }

        int row = 0;
        while(rs.next())
        {
            row++;
            result.add(new String[columnsCount]);
            for (int i=0;i<columnsCount;i++)
            {
                String value = rs.getString(i+1); //getString() retrieves any basic SQL type - we'll stick with that for now
                result.get(row)[i] = value;
            }
        }

        String[][] out = new String[result.size()][columnsCount];
        int i = 0;
        for(String[] resultRow : result){
            out[i] = resultRow;
            ++i;
        }
        return out;
    }

    public void close() {

        try {
            if (connection!=null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
