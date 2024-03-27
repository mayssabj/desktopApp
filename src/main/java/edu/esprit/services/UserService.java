package edu.esprit.services;

import edu.esprit.entities.User;
import edu.esprit.utils.mydb;

import java.sql.*;

public class UserService {
    private Connection con ;
    Statement ste ;
    public UserService (){
        con = mydb.getInstance().getCon();
    }


}
