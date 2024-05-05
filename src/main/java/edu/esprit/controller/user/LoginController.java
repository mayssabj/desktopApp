package edu.esprit.controller.user;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.esprit.entities.User;
import edu.esprit.entities.VerificationCode;
import edu.esprit.enums.Role;
import edu.esprit.services.MailService;
import edu.esprit.services.UserService;
import edu.esprit.utils.*;
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


import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    @FXML
    private TextField forgetEmailField;
    @FXML
    private Label forgetMessageLabel;

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
                System.out.println(user);
                // Set the current user in the session
                Session.getInstance().setCurrentUser(user);
                if(user.isVerified()){
                    if(user.getRoles().contains(Role.ROLE_ADMIN)){
                        NavigationUtil.redirectTo("/Dashboard.fxml", event);
                    }else{
//                        NavigationUtil.redirectTo("/user/updateUser.fxml", event);
                        NavigationUtil.redirectTo("/dashb.fxml", event);
                    }
                    // Proceed with login (e.g., navigate to another page)
//                    NavigationUtil.redirectTo("/user/updateUser.fxml", event);
                }else{
                    NavigationUtil.redirectTo("/user/verificationCode.fxml", event);
                }

            } else {
                loginMessageLabel.setText("Email or password incorrect");
                loginMessageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            }
            loginButton.setText("Log in"); // Reset button text
        }
    }


    public User checkCredentials(String email, String password) {
        Connection con = mydb.getInstance().getCon();
        String query = "SELECT id, password FROM user WHERE email = ?";

        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (BCrypt.checkpw(password, storedPassword)) {
                    UserService userService = new UserService();
                    // If password matches, fetch the complete user details using the dedicated method
                    return userService.getUserById(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // User not found or password mismatch
    }

    // Helper method to parse roles JSON from database into List<Role>
    private List<Role> parseRoles(String jsonRoles) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Role>>(){}.getType();
        return gson.fromJson(jsonRoles, type);
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



    @FXML
    public void openForgotPasswordPage(MouseEvent event) {
        try {
            // Load the forgot password form FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/forgetPassword.fxml"));
            Parent root = loader.load();

            // Get the current stage from the event
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene to the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (optional: show an error dialog)
            showAlert(Alert.AlertType.ERROR, (Stage) ((Node)event.getSource()).getScene().getWindow(), "Load Error", "Cannot load the forgot password page.");
        }
    }



    @FXML
    public void handleForgotPassword(ActionEvent event) {
        String email = forgetEmailField.getText();
        List<String> emailErrors = ValidationUtils.validateEmail(email);
        if (!emailErrors.isEmpty()) {
            displayErrors(emailErrorsContainer, emailErrors);
            return;
        }

        UserService userService = new UserService();
        User user = userService.findUserByEmail(email);
        System.out.println(user+ "🟥🟥🟥🟥🟥🟥");
        if (user != null) {
            // Generate a verification code instead of a new password
            String verificationCode = VerificationCodeUtil.generateVerificationCode();
            // Create a verification code object and associate it with the user
            VerificationCode code = new VerificationCode(user, verificationCode, 30); // Valid for 30 minutes

            if (userService.updateVerificationCode(user.getId(), code)) {
                MailService mailService = new MailService();
                Session.getInstance().setResetPasswordEmail(email);
                // Send the verification code instead of the password
                mailService.sendEmail(user.getEmail(), "Password Reset", "Your password reset code is: " + verificationCode);
                forgetMessageLabel.setText("Password reset code sent to your email.");
                forgetMessageLabel.setStyle("-fx-text-fill: green;");
                NavigationUtil.redirectTo("/user/verificationCodePassword.fxml", event);
            } else {
                forgetMessageLabel.setText("Failed to update password reset code. Try again later.");
                forgetMessageLabel.setStyle("-fx-text-fill: red;");
            }
        } else {
            forgetMessageLabel.setText("No account found with that email.");
            forgetMessageLabel.setStyle("-fx-text-fill: red;");
        }
    }



}
