package edu.esprit.utils;


import java.sql.*;

public class DBConnector {


    String url = "jdbc:mysql://localhost:3306/pidev";
    private String user = "root";
    private String pwd = "";
    static String driver = "com.mysql.jdbc.Driver";
    public static Connection getCon() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidev", "root", "");
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
            e.printStackTrace();
        }
        return con;
    }

}



