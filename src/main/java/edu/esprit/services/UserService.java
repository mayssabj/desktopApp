package edu.esprit.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.esprit.entities.User;
import edu.esprit.entities.VerificationCode;
import edu.esprit.enums.Role;
import edu.esprit.utils.FileChooserUtil;
import edu.esprit.utils.mydb;

import java.io.File;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {


    public User findUserBy(String identifier, Object value) {
        String column = identifier.equals("email") ? "email" : "id";
        String query = "SELECT u.*, vc.id as vc_id, vc.user_id, vc.code, vc.expiry_date " +
                "FROM user u LEFT JOIN verification_code vc ON u.id = vc.user_id " +
                "WHERE u." + column + " = ? LIMIT 1";
        try (Connection con = mydb.getInstance().getCon();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setObject(1, value);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhone(rs.getString("phone"));
        user.setPhoto(rs.getString("photo"));
        user.setAddress(rs.getString("address"));
        user.setGender(rs.getString("gender"));
        user.setRoles(parseRoles(rs.getString("roles")));
        user.setUsername(rs.getString("username"));
        user.setEnabled(rs.getBoolean("is_enabled"));
        user.setEmailVerificationToken(rs.getString("email_verification_token"));
        user.setVerified(rs.getBoolean("is_verified"));
        user.setResetToken(rs.getString("reset_token"));
        user.setAvertissementsCount(rs.getInt("avertissements_count"));
        user.setReputation(rs.getObject("reputation") != null ? rs.getInt("reputation") : null);

        if (rs.getString("code") != null) {
            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setId(rs.getInt("vc_id"));
            verificationCode.setCode(rs.getString("code"));
            verificationCode.setExpiryDate(rs.getTimestamp("expiry_date").toLocalDateTime());
            user.setVerificationCode(verificationCode);
        }
        return user;
    }

    public User getUserById(int userId) {
        return findUserBy("id", userId);
    }

    public User findUserByEmail(String email) {
        return findUserBy("email", email);
    }
    public boolean registerUser(User user, VerificationCode verificationCode) {
        Connection con = mydb.getInstance().getCon();
        try {
            con.setAutoCommit(false);  // Start transaction

            // Insert user
            String userQuery = "INSERT INTO user (email, password, phone, photo, address, gender, roles, username) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pst = con.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, user.getEmail());
                pst.setString(2, user.getPassword());
                pst.setString(3, user.getPhone());
                pst.setString(4, user.getPhoto());
                pst.setString(5, user.getAddress());
                pst.setString(6, user.getGender());
                pst.setString(7, user.getRolesAsString());  // Use serialized roles
                pst.setString(8, user.getUsername());
                int affectedRows = pst.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));  // Set user ID from generated keys
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            }

            // Insert verification code
            String codeQuery = "INSERT INTO verification_code (user_id, code, expiry_date) VALUES (?, ?, ?)";
            try (PreparedStatement pst = con.prepareStatement(codeQuery)) {
                pst.setInt(1, user.getId());
                pst.setString(2, verificationCode.getCode());
                pst.setTimestamp(3, Timestamp.valueOf(verificationCode.getExpiryDate()));
                pst.executeUpdate();
            }

            con.commit();  // Commit transaction
            return true;
        } catch (SQLException ex) {
            try {
                con.rollback();  // Rollback on error
            } catch (SQLException e) {
                System.out.println("Rollback failed: " + e.getMessage());
            }
            ex.printStackTrace();
            return false;
        } finally {
            try {
                con.setAutoCommit(true);  // Reset auto-commit to true
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public User updateUserInfo(String phone, String address, int userId) {
        Connection con = mydb.getInstance().getCon();
        String query = "UPDATE user SET phone = ?, address = ? WHERE id = ?";

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
        String query = "UPDATE user SET email = ? WHERE id = ?";

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

    public boolean updateUserPassword(String newPassword, int userId) {
        Connection con = mydb.getInstance().getCon();
        String query = "UPDATE user SET password = ? WHERE id = ?";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, newPassword);
            pst.setInt(2, userId);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                // Return the updated user object
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }




    private List<Role> parseRoles(String jsonRoles) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Role>>(){}.getType();
        return gson.fromJson(jsonRoles, type);
    }
    private VerificationCode extractVerificationCode(ResultSet rs) {
        try {
            if (rs.getString("code") != null) {
                VerificationCode verificationCode = new VerificationCode();
                verificationCode.setId(rs.getInt("vc_id")); // Assuming there is a 'vc_id' field
                verificationCode.setCode(rs.getString("code"));
                verificationCode.setExpiryDate(rs.getTimestamp("expiry_date").toLocalDateTime());
                verificationCode.setUser(getUserById(rs.getInt("user_id"))); // Recursive call, be careful with performance
                return verificationCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public boolean isEmailUsed(String email) {
        Connection con = mydb.getInstance().getCon();
        String query = "SELECT COUNT(*) FROM user WHERE email = ?";

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
        String query = "UPDATE user SET photo = ? WHERE id = ?";

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
        String query = "DELETE FROM user WHERE id = ?";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, userId);

            int rowsDeleted = pst.executeUpdate();
            return rowsDeleted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public boolean updateUserVerificationStatus(int userId, boolean isVerified) {
        Connection con = mydb.getInstance().getCon();
        String query = "UPDATE user SET is_verified = ? WHERE id = ?";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setBoolean(1, isVerified);
            pst.setInt(2, userId);

            int rowsUpdated = pst.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user verification status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateVerificationCode(int userId, VerificationCode verificationCode) {
        Connection con = mydb.getInstance().getCon();
        String query = "UPDATE verification_code SET code = ?, expiry_date = ? WHERE user_id = ?";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, verificationCode.getCode());
            pst.setTimestamp(2, Timestamp.valueOf(verificationCode.getExpiryDate()));
            pst.setInt(3, userId);

            int rowsUpdated = pst.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error updating verification code: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT u.*, vc.code as verification_code " +
                "FROM user u LEFT JOIN verification_code vc ON u.id = vc.user_id";
        try (Connection con = mydb.getInstance().getCon();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                users.add(extractFullUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private User extractFullUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhone(rs.getString("phone"));
        user.setPhoto(rs.getString("photo"));
        user.setAddress(rs.getString("address"));
        user.setGender(rs.getString("gender"));
        user.setRoles(parseRoles(rs.getString("roles")));
        user.setUsername(rs.getString("username"));
        user.setEnabled(rs.getBoolean("is_enabled"));
        user.setAvertissementsCount(rs.getInt("avertissements_count"));
        user.setReputation(rs.getObject("reputation") != null ? rs.getInt("reputation") : null);
        user.setEmailVerificationToken(rs.getString("email_verification_token"));
        user.setVerified(rs.getBoolean("is_verified"));
        user.setResetToken(rs.getString("reset_token"));
        if (rs.getString("verification_code") != null) {
            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setCode(rs.getString("verification_code"));
            user.setVerificationCode(verificationCode);
        }
        return user;
    }

}