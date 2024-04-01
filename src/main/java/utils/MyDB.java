package utils;

import java.sql.*;

public class MyDB {
    static MyDB instance;
    Connection coon;
    String url="jdbc:mysql://localhost:3306/pijava";
    String user="root";
    String pwd="";

    private MyDB() {
        try {
            this.coon = DriverManager.getConnection(url,user,pwd);
            System.out.println("Connexion etablie.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static MyDB getInstance() {
        if (instance == null){
            return new MyDB();
        }
        return instance;
    }

    public Connection getConn() {
        return this.coon;
    }
}
