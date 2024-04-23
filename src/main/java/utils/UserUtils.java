package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserUtils {
    private Connection cnx;
    private String url = "jdbc:mysql://localhost:3306/pidev";
    private String login = "root";
    private String pwd = "";
    private static UserUtils instance;

    private UserUtils() {
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

    public static UserUtils getInstance() {
        if (instance == null)
            instance = new UserUtils();
        return instance;
    }

    public String getUsername(int userId) {
        String username = null;
        try {
            PreparedStatement pstmt = cnx.prepareStatement("SELECT username FROM user WHERE id = ?");
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                username = rs.getString("username");
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return username;
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
