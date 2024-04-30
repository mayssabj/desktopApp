package edu.esprit.controller.user;

import edu.esprit.entities.User;
import edu.esprit.services.UserService;
import edu.esprit.utils.NavigationUtil;
import edu.esprit.utils.Session;
import edu.esprit.utils.ValidationUtils;
import edu.esprit.utils.mydb;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class LoginController implements Initializable {

    @FXML
    public Label loginMessageLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private Button loginButton;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private VBox emailErrorsContainer;
    @FXML
    private VBox passwordErrorsContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize your controller here if needed
    }

    @FXML
    public void login(ActionEvent event) {
        boolean isValid = true;
        emailErrorsContainer.getChildren().clear();
        passwordErrorsContainer.getChildren().clear();

        loginMessageLabel.setText(""); // Clear the login error message

        // Validate email field
        List<String> emailErrors = ValidationUtils.validateEmail(emailField.getText());
        if (!emailErrors.isEmpty()) {
            isValid = false;
            displayErrors(emailErrorsContainer, emailErrors);
        }

        // Validate password field
        List<String> passwordErrors = new ArrayList<>();
        if (passwordField.getText().isEmpty()) {
            passwordErrors.add("• Password field is required.");
        }
        if (!passwordErrors.isEmpty()) {
            isValid = false;
            displayErrors(passwordErrorsContainer, passwordErrors);
        }

        if (isValid) {
            loginButton.setText("Checking...");
            User user = checkCredentials(emailField.getText(), passwordField.getText());
            if (user != null) {
                System.out.println("Login successful");
                // Set the current user in the session
                Session.getInstance().setCurrentUser(user);
                // Proceed with login (e.g., navigate to another page)
                NavigationUtil.redirectTo("/dashb.fxml", event);
            } else {
                loginMessageLabel.setText("Email or password incorrect");
                loginMessageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            }
            loginButton.setText("Log in"); // Reset button text
        }
    }


    private User checkCredentials(String email, String password) {
        Connection con = mydb.getInstance().getCon();
        String query = "SELECT * FROM users WHERE email = ?";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, email);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (BCrypt.checkpw(password, hashedPassword)) { // Check the hashed password
                    // Create and return the User object
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(hashedPassword);
                    user.setPhone(rs.getString("phone"));
                    user.setProfilePicture(rs.getString("profile_picture"));
                    user.setAddress(rs.getString("address"));
                    UserService userservice=new UserService();
                    userservice.setCurrentLoggedInUser(user);
                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // User not found or error
    }




    private void displayErrors(VBox container, List<String> errors) {
        for (String error : errors) {
            Label errorLabel = new Label(error);
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;"); // Increase font size to 14px
            container.getChildren().add(errorLabel);
        }
    }

    public void openRegistration(MouseEvent event) {
        try {
            // Load the registration form FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/registration.fxml"));
            Parent root = loader.load();

            // Get the current stage from the event
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene to the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void showAlert(Alert.AlertType alertType, Stage owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }


    public void logout(ActionEvent event) {
        // Clear the current user from the session
        Session.getInstance().setCurrentUser(null);

        try {
            // Load the login form FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/login.fxml"));
            Parent root = loader.load();

            // Get the current stage from the event
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene to the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
