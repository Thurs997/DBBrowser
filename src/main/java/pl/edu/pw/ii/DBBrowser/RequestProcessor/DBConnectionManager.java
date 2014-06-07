package pl.edu.pw.ii.DBBrowser.RequestProcessor;

import pl.edu.pw.ii.DBBrowser.Configuration;

import java.sql.*;

final public class DBConnectionManager {

    //database drivers - supported
    private static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

    private PreparedStatement stmt;
    private Connection conn = null;


    public DBConnectionManager() {

    }

    public void connect(String dbUserName, String dbUserPwd) {
        String databaseDriver = null;
        String dbType = Configuration.getInstance().getProperty("dbType");
        String dbUrl = Configuration.getInstance().getProperty("dbUrl");
        try {
            if (conn!=null && conn.isValid(10))
            {
                System.out.println("Already connected");
                return;
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
    }

    public ResultSet executeQuery(String sql) {
        ResultSet rs = null;
        stmt = null;

        try {
            if (conn!=null || !conn.isValid(10))
            {
                System.out.println("Not connected");
                return rs;
            }

            stmt = conn.prepareStatement(sql);
            stmt.setQueryTimeout(10); //cancel query after 10 seconds
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
