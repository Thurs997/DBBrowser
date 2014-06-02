package pl.edu.pw.ii.DBBrowser;

import java.sql.*;

public class DBConnectionManager {

    //database drivers - supported
    final static String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
    final static String MYSQL_DRIVER = "com.mysql.jdbc.Driver";


    //data needed to connect with database
    static String databaseDriver;
    static String databaseUrl; //full url - specific for database type
    static String userName;
    static String userPassword;

    static Connection conn;
    static PreparedStatement stmt;

    public DBConnectionManager(String dbType, String dbUrl, String dbUserName, String dbUserPwd) {
        conn = null;

        if (dbType.equals("mysql"))
            databaseDriver = MYSQL_DRIVER;
        else if (dbType.equals("oracle"))
            databaseDriver = ORACLE_DRIVER;

        databaseUrl = dbUrl;
        userName = dbUserName;
        userPassword = dbUserPwd;

        try {
            Class.forName(databaseDriver).newInstance();

            conn = DriverManager.getConnection(databaseUrl, userName, userPassword);
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

    public static ResultSet executeQuery(String sql) {
        ResultSet rs = null;

        stmt = null;

        try {
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
