package edu.esprit.services;

import edu.esprit.entities.User;
import edu.esprit.utils.mydb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {
    private static User currentUser;
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

    public User updateUserInfo(String phone, String address, int userId) {
        Connection con = mydb.getInstance().getCon();
        String query = "UPDATE users SET phone = ?, address = ? WHERE id = ?";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, phone);
            pst.setString(2, address);
            pst.setInt(3, userId);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                // Return the updated user object
                return getUserById(userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User updateUserEmail(String newEmail, int userId) {
        Connection con = mydb.getInstance().getCon();
        String query = "UPDATE users SET email = ? WHERE id = ?";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, newEmail);
            pst.setInt(2, userId);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                // Return the updated user object
                return getUserById(userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User updateUserPassword(String newPassword, int userId) {
        Connection con = mydb.getInstance().getCon();
        String query = "UPDATE users SET password = ? WHERE id = ?";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, newPassword);
            pst.setInt(2, userId);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                // Return the updated user object
                return getUserById(userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserById(int userId) {
        Connection con = mydb.getInstance().getCon();
        String query = "SELECT * FROM users WHERE id = ?";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, userId);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setEmail(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                user.setProfilePicture(rs.getString("profile_picture"));
                user.setAddress(rs.getString("address"));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean isEmailUsed(String email) {
        Connection con = mydb.getInstance().getCon();
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Check if count is greater than 0
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUserProfilePicture(String profilePicture, int userId) {
        Connection con = mydb.getInstance().getCon();
        String query = "UPDATE users SET profile_picture = ? WHERE id = ?";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, profilePicture);
            pst.setInt(2, userId);

            int rowsUpdated = pst.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(int userId) {
        Connection con = mydb.getInstance().getCon();
        String query = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, userId);

            int rowsDeleted = pst.executeUpdate();
            return rowsDeleted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void logout() {
        currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void setCurrentLoggedInUser(User user) {
        currentUser=user;
    }

    public User getCurrentLoggedInUser() {
        return currentUser;
    }

   //recuperer un utilisateur par son username
    public User getUserByUsername(String username) throws SQLException {
        Connection con = mydb.getInstance().getCon();
        User user = null;
        try (PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setPhone(resultSet.getString("phone"));
                user.setProfilePicture(resultSet.getString("profile_picture"));
                user.setAddress(resultSet.getString("address"));
                user.setGender(resultSet.getString("gender"));
                user.setAvertissements_count(resultSet.getInt("avertissements_count"));
            }
        }
        return user;
    }
    //incrementer le nombre d'avertissements d'un utilisateur
    public void incrementAvertissementCount(int userId) throws SQLException {
        Connection con = mydb.getInstance().getCon();
        try (PreparedStatement preparedStatement = con.prepareStatement("UPDATE users SET avertissements_count = avertissements_count + 1 WHERE id = ?")) {
            preparedStatement.setInt(1, userId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating avertissements_count failed, no rows affected.");
            }
        }
    }
}
