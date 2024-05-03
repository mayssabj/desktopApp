package edu.esprit.controller.user;

import edu.esprit.entities.User;
import edu.esprit.entities.VerificationCode;
import edu.esprit.services.MailService;
import edu.esprit.services.UserService;
import edu.esprit.utils.NavigationUtil;
import edu.esprit.utils.Session;
import edu.esprit.utils.VerificationCodeUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class PasswordVerificationController {
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


    public void initialize() {
        // Set initial label style and message
        verificationMessageLabel.setFont(new Font("Arial", 12)); // Set font and size
    }

    @FXML
    public void verifyPasswordCode(ActionEvent event) {
        String inputCode = codeField1.getText() + codeField2.getText() + codeField3.getText() + codeField4.getText();

        if (inputCode.length() != 4) {
            verificationMessageLabel.setText("Please enter a valid 4-digit code.");
            verificationMessageLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        UserService userService = new UserService();
        User currentUser = userService.findUserByEmail(Session.getInstance().getResetPasswordEmail());
        System.out.println(Session.getInstance().getResetPasswordEmail());
        System.out.println(currentUser);
        if (currentUser != null && currentUser.getVerificationCode() != null) {
            int result = VerificationCodeUtil.validateVerificationCode(currentUser.getVerificationCode(), inputCode);
            handleVerificationResult(result,currentUser,event,userService);
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
                NavigationUtil.redirectTo("/user/resetPassword.fxml", event);
                break;
            case 0: // Code expired
                verificationMessageLabel.setText("Verification code has expired.");
                verificationMessageLabel.setStyle("-fx-text-fill: red;");
                break;
            case 2: // Code invalid
                verificationMessageLabel.setText("Invalid verification code. Please try again.");
                verificationMessageLabel.setStyle("-fx-text-fill: red;");
                break;
            default:
                verificationMessageLabel.setText("No code found.");
                verificationMessageLabel.setStyle("-fx-text-fill: red;");
                break;
        }
    }


}
