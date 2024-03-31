package edu.esprit.services;

import edu.esprit.entities.User;
import edu.esprit.utils.mydb;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class UserService {
    public boolean registerUser(User user) {
        Connection con = mydb.getInstance().getCon();
        String query = "INSERT INTO users (email, password, phone, profile_picture, address, gender) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, user.getEmail());
            pst.setString(2, user.getPassword());
            pst.setString(3, user.getPhone());
            pst.setString(4, user.getProfilePicture());
            pst.setString(5, user.getAddress());
            pst.setString(6, user.getGender());

            int rowsInserted = pst.executeUpdate();
            return rowsInserted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
