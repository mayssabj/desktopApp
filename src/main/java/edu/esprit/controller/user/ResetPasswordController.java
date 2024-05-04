package edu.esprit.controller.user;

import edu.esprit.entities.User;
import edu.esprit.entities.VerificationCode;
import edu.esprit.services.MailService;
import edu.esprit.services.UserService;
import edu.esprit.utils.NavigationUtil;
import edu.esprit.utils.Session;
import edu.esprit.utils.ValidationUtils;
import edu.esprit.utils.VerificationCodeUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class ResetPasswordController {
    @FXML
    public VBox passwordErrorsContainer;
    @FXML
    public Label resetPassMessageLabel;
    @FXML
    public VBox confirmPasswordErrorsContainer;
    @FXML
    public PasswordField confirmPasswordField;
    @FXML
    public PasswordField passwordField;

    @FXML
    public Button resetButton;

    @FXML
    public void handleResetPassword(ActionEvent event) {
        boolean isValid = true;
        passwordErrorsContainer.getChildren().clear();
        confirmPasswordErrorsContainer.getChildren().clear();

        resetPassMessageLabel.setText("");

        List<String> passwordErrors = ValidationUtils.validatePassword(passwordField.getText());
        RegistrationController registrationController = new RegistrationController();
        if (!passwordErrors.isEmpty()) {
            isValid = false;
            registrationController.displayErrors(passwordErrorsContainer, passwordErrors);
        }
        List<String> confirmPasswordErrors = ValidationUtils.validateConfirmPassword(passwordField.getText(), confirmPasswordField.getText());
        if (!confirmPasswordErrors.isEmpty()) {
            isValid = false;
            registrationController.displayErrors(confirmPasswordErrorsContainer, confirmPasswordErrors);
        }

        if (isValid) {
            resetButton.setText("Checking...");
            UserService userService = new UserService();
            User user = userService.findUserByEmail(Session.getInstance().getResetPasswordEmail()); // Assumes current user's email is stored in session
            if (user != null) {
                String hashedPassword = BCrypt.hashpw(passwordField.getText(), BCrypt.gensalt());
                boolean passwordUpdateSuccess = userService.updateUserPassword(hashedPassword, user.getId());
                if (passwordUpdateSuccess) {
                    resetPassMessageLabel.setText("Your password has been updated successfully.");
                    resetPassMessageLabel.setStyle("-fx-text-fill: green;");
                    NavigationUtil.redirectTo("/user/login.fxml", event);
                    Session.getInstance().setResetPasswordEmail(null);
                } else {
                    resetPassMessageLabel.setText("Failed to update your password. Please try again later.");
                    resetPassMessageLabel.setStyle("-fx-text-fill: red;");
                }
            } else {
                resetPassMessageLabel.setText("No account found for the provided email.");
                resetPassMessageLabel.setStyle("-fx-text-fill: red;");
            }
            resetButton.setText("Reset Password");
        }
    }
}
