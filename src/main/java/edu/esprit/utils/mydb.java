package edu.esprit.utils;

import java.sql.*;
public class mydb {
    static mydb instance ;
    Connection con ;
    String url = "jdbc:mysql://root:@127.0.0.1:3306/pidevv";
    String user="root" ;
    String pwd="" ;
    private mydb() {
        try {
            this.con = DriverManager.getConnection(url,user,pwd);
            System.out.println("Connexion etablie.");
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

