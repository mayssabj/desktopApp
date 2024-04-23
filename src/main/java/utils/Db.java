package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db {
    private Connection cnx;
    private String url = "jdbc:mysql://localhost:3306/pidev";
    private String login = "root";
    private String pwd = "";
    private static Db instance;

    private Db() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            cnx = DriverManager.getConnection(url, login, pwd);
            System.out.println("Database connected successfully!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC driver not found", e);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    public static Db getInstance() {
        if (instance == null)
            instance = new Db();
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }

    public void close() {
        if (cnx != null) {
            try {
                cnx.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                throw new RuntimeException("Failed to close database connection", e);
            }
        }
    }
}
