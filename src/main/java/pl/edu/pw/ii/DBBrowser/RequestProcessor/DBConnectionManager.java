package pl.edu.pw.ii.DBBrowser.RequestProcessor;

import pl.edu.pw.ii.DBBrowser.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

final public class DBConnectionManager {

    //database drivers - supported
    private static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

    private PreparedStatement stmt;
    private Connection conn = null;


    public DBConnectionManager() {

    }

    public boolean connect(String dbUserName, String dbUserPwd) {
        String databaseDriver = null;
        String dbType = Configuration.getInstance().getProperty("dbType");
        String dbUrl = Configuration.getInstance().getProperty("dbUrl");
        try {
            if (conn!=null && conn.isValid(10))
            {
                System.out.println("Already connected");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (dbType.equals("mysql"))
            databaseDriver = MYSQL_DRIVER;
        else if (dbType.equals("oracle"))
            databaseDriver = ORACLE_DRIVER;

        try {
            Class.forName(databaseDriver).newInstance();

            conn = DriverManager.getConnection(dbUrl, dbUserName, dbUserPwd);
            conn.setAutoCommit(false);
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
        DatabaseMetaData md = conn.getMetaData();
        ResultSet rs = null;

        if(md.getDatabaseProductName().equals("Microsoft SQL Server")) //this query is different for mySQL
            rs = md.getCatalogs();
        else
            rs = md.getSchemas();

        while(rs.next())
        {
            String db = rs.getString(1);
            databasesList.add(db);
        }

        return databasesList;
    }

    public List<String> executeListTables(String databaseName) throws SQLException {
        List<String> tablesList = new ArrayList();
        DatabaseMetaData md = conn.getMetaData();
        ResultSet rs;

        if (databaseName == null)
            return tablesList;

        if(md.getDatabaseProductName().equals("Microsoft SQL Server")) //this query is different for mySQL
            rs = md.getTables(databaseName, null, "%", null);
        else
            rs = md.getTables(null, databaseName, "%", null);

        while(rs.next())
        {
            String table = rs.getString(3);
            tablesList.add(table);
        }

        return tablesList;
    }

    public String[][] executeListTableContent(String tableName) throws SQLException {
        DatabaseMetaData md = conn.getMetaData();
        ResultSet rs;

        if (tableName==null)
            return new String[0][0];

        //get rows count
        int rowsCount = 0;
        String sql = "select COUNT(*) from " + tableName;

        rs = executeQuery(sql);

        while(rs.next())
        {
            rowsCount = rs.getInt(1);
        }

        //execute actual query
        sql = "select * from" + tableName;

        rs = executeQuery(sql);

        ResultSetMetaData rsMD = rs.getMetaData();

        //get columns and columns count
        int columnsCount = rsMD.getColumnCount();

        //table with results: first row contains columns names, rest - query results
        String[][] tableContent = new String[rowsCount+1][columnsCount];

        for (int j=0;j<columnsCount;j++)
        {
            String db = rsMD.getColumnName(j+1);
            tableContent[0][j] = db;
        }

        for (int i=1;i<rowsCount+1;i++)
        {
            rs.next();
            for (int j=0;j<columnsCount;j++)
            {
                String db = rs.getString(j+1); //getString() retrieves any basic SQL type - we'll stick with that for now
                tableContent[i][j] = db;
            }
        }

        return tableContent;
    }


    ResultSet executeQuery(String sql) {
        ResultSet rs = null;
        stmt = null;

        try {
            if (conn!=null || !conn.isValid(10))
            {
                System.out.println("Not connected");
                return rs;
            }

            stmt.setQueryTimeout(10); //cancel query after 10 seconds
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            conn.commit();

        } catch (SQLException e) {
            try {
                if (conn!=null)
                    conn.rollback();
            } catch (SQLException se) { }
        } finally {
            try {
                if (stmt!=null)
                    stmt.close();
            } catch (SQLException e) { }
        }

        return rs;
    }

    public void close() {

        try {
            if (stmt!=null && !stmt.isClosed()) //if statement is not closed, close it - this will cancel its execution
                stmt.close();
            if (conn!=null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
