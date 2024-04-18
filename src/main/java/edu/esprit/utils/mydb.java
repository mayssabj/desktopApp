package edu.esprit.utils;

<<<<<<< HEAD
import java.sql.*;
public class mydb {
    static mydb instance ;
    Connection con ;
    String url = "jdbc:mysql://root:@127.0.0.1:3306/pidevv";
=======
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class mydb {
    static mydb instance ;
    Connection con ;
    String url = "jdbc:mysql://127.0.0.1:3306/pijava";

>>>>>>> 560008013e3371a7c394d79c8393560c80ec93e7
    String user="root" ;
    String pwd="" ;
    private mydb() {
        try {
            this.con = DriverManager.getConnection(url,user,pwd);
<<<<<<< HEAD
            System.out.println("Connexion etablie.");
=======
            System.out.println("Connection established ✅.");
>>>>>>> 560008013e3371a7c394d79c8393560c80ec93e7
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static mydb getInstance() {
        if (instance == null){
            return new mydb();
        }
        return instance;
    }

    public Connection getCon() {
        return this.con;
    }

}

