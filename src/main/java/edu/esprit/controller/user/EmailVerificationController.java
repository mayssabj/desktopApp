package edu.esprit.controller.user;

import edu.esprit.entities.User;
import edu.esprit.entities.VerificationCode;
import edu.esprit.services.MailService;
import edu.esprit.services.UserService;
import edu.esprit.utils.NavigationUtil;
import edu.esprit.utils.Session;
import edu.esprit.utils.VerificationCodeUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.w3c.dom.events.MouseEvent;

public class EmailVerificationController {

    @FXML
    public Label verificationMessageLabel;

    @FXML
    private TextField codeField1;
    @FXML
    private TextField codeField2;
    @FXML
    private TextField codeField3;
    @FXML
    private TextField codeField4;

    // Initialize your controller
    public void initialize() {
        // Set initial label style and message
        verificationMessageLabel.setText("Account verification pending. Check your email for the code.");
        verificationMessageLabel.setStyle("-fx-text-fill: orange;"); // Orange color for warning
        verificationMessageLabel.setFont(new Font("Arial", 12)); // Set font and size
    }

    public void verifyCode(ActionEvent event) {
        String inputCode = codeField1.getText() + codeField2.getText() + codeField3.getText() + codeField4.getText();

        if (inputCode.length() != 4) {
            verificationMessageLabel.setText("Please enter a valid 4-digit code.");
            verificationMessageLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        UserService userService = new UserService();
        User currentUser = userService.findUserByEmail(Session.getInstance().getVerificationEmail());
        if (currentUser != null && currentUser.getVerificationCode() != null) {
            int result = VerificationCodeUtil.validateVerificationCode(currentUser.getVerificationCode(), inputCode);
            handleVerificationResult(result, currentUser, event, userService);
        } else {
            verificationMessageLabel.setText("No code found. Click Re-send for a new code.");
            verificationMessageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void handleVerificationResult(int result, User currentUser, ActionEvent event, UserService userService) {
        switch (result) {
            case 1: // Code valid
                verificationMessageLabel.setText("Account verified successfully!");
                verificationMessageLabel.setStyle("-fx-text-fill: green;");
                currentUser.setVerified(true);
                if (userService.updateUserVerificationStatus(currentUser.getId(), true)) {
                    // Redirect to the main application page or refresh the user session
                    NavigationUtil.redirectTo("/user/login.fxml",event);
                } else {
                    verificationMessageLabel.setText("Failed to update verification status in database.");
                    verificationMessageLabel.setStyle("-fx-text-fill: red;");
                }
                break;
            case 0: // Code expired
                verificationMessageLabel.setText("Verification code has expired. Click Re-send for a new code.");
                verificationMessageLabel.setStyle("-fx-text-fill: red;");
                break;
            case 2: // Code invalid
                verificationMessageLabel.setText("Invalid verification code. Please try again.");
                verificationMessageLabel.setStyle("-fx-text-fill: red;");
                break;
            default:
                verificationMessageLabel.setText("No code found. Click Re-send for a new code.");
                verificationMessageLabel.setStyle("-fx-text-fill: red;");
                break;
        }
    }

    @FXML
    public void resendVerificationCode(javafx.scene.input.MouseEvent mouseEvent) {
        User currentUser = Session.getInstance().getCurrentUser();
        if (currentUser != null) {
            UserService userService = new UserService();
            String newVerificationCode = VerificationCodeUtil.generateVerificationCode();
            VerificationCode newCode = new VerificationCode(currentUser, newVerificationCode, 30); // Set expiry time as needed

            if (userService.updateVerificationCode(currentUser.getId(), newCode)) {
                MailService mailService = new MailService();
                mailService.sendEmail(currentUser.getEmail(), "Email Verification", "Your new verification code is: " + newVerificationCode);
                verificationMessageLabel.setText("A new verification code has been sent to your email.");
                verificationMessageLabel.setStyle("-fx-text-fill: blue;"); // Set text color to indicate information
            } else {
                verificationMessageLabel.setText("Failed to resend verification code. Please try again.");
                verificationMessageLabel.setStyle("-fx-text-fill: red;");
            }
        } else {
            verificationMessageLabel.setText("No user session found. Please login again.");
            verificationMessageLabel.setStyle("-fx-text-fill: red;");
        }
    }
}
