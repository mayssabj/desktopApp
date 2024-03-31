package edu.esprit.controller.user;

import edu.esprit.entities.User;
import edu.esprit.services.UserService;
import edu.esprit.utils.FileChooserUtil;
import edu.esprit.utils.Session;
import edu.esprit.utils.ValidationUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    @FXML
    public TextField phoneField;
    @FXML
    public TextField addressField;
    @FXML
    public TextField newEmailField;
    @FXML
    public PasswordField passwordForEmailField;
    @FXML
    public PasswordField oldPasswordField;
    @FXML
    public PasswordField newPasswordField;
    @FXML
    public VBox phoneErrorsContainer;
    @FXML
    public VBox addressErrorsContainer;
    @FXML
    public VBox newEmailErrorsContainer;
    @FXML
    public VBox passwordForEmailErrorsContainer;
    @FXML
    public VBox oldPasswordErrorsContainer;
    @FXML
    public VBox newPasswordErrorsContainer;
    @FXML
    public Label infoUpdateMessageLabel;
    @FXML
    public Label emailUpdateMessageLabel;
    @FXML
    public Label passwordUpdateMessageLabel;
    @FXML
    public TextField currentEmailField;
    @FXML
    public ImageView profileImageView;

    private UserService userService = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateFields();
    }

    public void updateInformation(ActionEvent event) {
        boolean isValid = true;
        clearErrors();

        List<String> phoneErrors = ValidationUtils.validatePhone(phoneField.getText());
        List<String> addressErrors = ValidationUtils.validateAddress(addressField.getText());

        if (!phoneErrors.isEmpty()) {
            isValid = false;
            displayErrors(phoneErrorsContainer, phoneErrors);
        }

        if (!addressErrors.isEmpty()) {
            isValid = false;
            displayErrors(addressErrorsContainer, addressErrors);
        }

        if (isValid) {
            performUpdateInformation(phoneField.getText(), addressField.getText());
        }
    }

    public void changeEmail(ActionEvent event) {
        boolean isValid = true;
        clearErrors();

        List<String> emailErrors = ValidationUtils.validateEmail(newEmailField.getText());
        List<String> passwordErrors = ValidationUtils.validatePassword(passwordForEmailField.getText());

        if (!emailErrors.isEmpty()) {
            isValid = false;
            displayErrors(newEmailErrorsContainer, emailErrors);
        }

        if (!passwordErrors.isEmpty()) {
            isValid = false;
            displayErrors(passwordForEmailErrorsContainer, passwordErrors);
        }

        if (isValid) {
            performChangeEmail(newEmailField.getText(), passwordForEmailField.getText());
        }
    }

    public void changePassword(ActionEvent event) {
        boolean isValid = true;
        clearErrors();

        List<String> oldPasswordErrors = new ArrayList<>();
        List<String> newPasswordErrors = ValidationUtils.validatePassword(newPasswordField.getText());

        if(oldPasswordField.getText().isEmpty()){
            oldPasswordErrors.add("• Old password field is required.");
        }
        if (!oldPasswordErrors.isEmpty()) {
            isValid = false;
            displayErrors(oldPasswordErrorsContainer, oldPasswordErrors);
        }

        if (!newPasswordErrors.isEmpty()) {
            isValid = false;
            displayErrors(newPasswordErrorsContainer, newPasswordErrors);
        }

        if (isValid) {
            performChangePassword(oldPasswordField.getText(), newPasswordField.getText());
        }
    }

    private void performUpdateInformation(String phone, String address) {
        User currentUser = Session.getInstance().getCurrentUser();
        if (currentUser != null) {
            User updatedUser = userService.updateUserInfo(phone, address, currentUser.getId());
            if (updatedUser != null) {
                // Update the current user in the session
                Session.getInstance().setCurrentUser(updatedUser);
                displayInfoUpdateSuccess("Information updated successfully.");
            } else {
                displayInfoUpdateError("Failed to update information. Please try again.");
            }
        }
    }


    private void performChangeEmail(String newEmail, String password) {
        User currentUser = Session.getInstance().getCurrentUser();
        if (currentUser != null && BCrypt.checkpw(password, currentUser.getPassword())) {
            if (!userService.isEmailUsed(newEmail)) {
                User updatedUser = userService.updateUserEmail(newEmail, currentUser.getId());
                if (updatedUser != null) {
                    // Update the current user in the session
                    Session.getInstance().setCurrentUser(updatedUser);
                    displayEmailUpdateSuccess("Email changed successfully.");
                    newEmailField.setText(""); // Clear the new email field
                    passwordForEmailField.setText(""); // Clear the password field
                    currentEmailField.setText(updatedUser.getEmail());
                } else {
                    displayEmailUpdateError("Failed to change email. Please try again.");
                }
            } else {
                displayEmailUpdateError("Email is already in use.");
            }
        } else {
            displayEmailUpdateError("Incorrect password.");
        }
    }

    private void performChangePassword(String oldPassword, String newPassword) {
        User currentUser = Session.getInstance().getCurrentUser();
        if (currentUser != null && BCrypt.checkpw(oldPassword, currentUser.getPassword())) {
            String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            User updatedUser = userService.updateUserPassword(hashedNewPassword, currentUser.getId());
            if (updatedUser != null) {
                // Update the current user in the session
                Session.getInstance().setCurrentUser(updatedUser);
                displayPasswordUpdateSuccess("Password changed successfully.");
                oldPasswordField.setText(""); // Clear the old password field
                newPasswordField.setText(""); // Clear the new password field
            } else {
                displayPasswordUpdateError("Failed to change password. Please try again.");
            }
        } else {
            displayPasswordUpdateError("Incorrect old password.");
        }
    }






    private void displayErrors(VBox container, List<String> errors) {
        for (String error : errors) {
            Label errorLabel = new Label(error);
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12;");
            container.getChildren().add(errorLabel);
        }
    }

    private void clearErrors() {
        phoneErrorsContainer.getChildren().clear();
        addressErrorsContainer.getChildren().clear();
        newEmailErrorsContainer.getChildren().clear();
        passwordForEmailErrorsContainer.getChildren().clear();
        oldPasswordErrorsContainer.getChildren().clear();
        newPasswordErrorsContainer.getChildren().clear();
        infoUpdateMessageLabel.setText("");
        emailUpdateMessageLabel.setText("");
        passwordUpdateMessageLabel.setText("");
    }


    private void populateFields() {
        User currentUser = Session.getInstance().getCurrentUser();
        if (currentUser != null) {
            phoneField.setText(currentUser.getPhone());
            addressField.setText(currentUser.getAddress());
            currentEmailField.setText(currentUser.getEmail()); // Set the current email
            String profilePictureUrl = currentUser.getProfilePicture();
            if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                try {
                    Image profileImage = new Image(profilePictureUrl);
                    profileImageView.setImage(profileImage);
                } catch (IllegalArgumentException e) {
                    // Set default image if the URL is invalid
                    Image defaultImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/default_user.png")));
                    profileImageView.setImage(defaultImage);
                }
            } else {
                Image defaultImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/default_user.png")));
                profileImageView.setImage(defaultImage);
            }
        }
    }

    private void displayInfoUpdateSuccess(String message) {
        infoUpdateMessageLabel.setText(message);
        infoUpdateMessageLabel.setStyle("-fx-text-fill: green;");
    }

    private void displayInfoUpdateError(String message) {
        infoUpdateMessageLabel.setText(message);
        infoUpdateMessageLabel.setStyle("-fx-text-fill: red;");
    }

    private void displayEmailUpdateSuccess(String message) {
        emailUpdateMessageLabel.setText(message);
        emailUpdateMessageLabel.setStyle("-fx-text-fill: green;");
    }

    private void displayEmailUpdateError(String message) {
        emailUpdateMessageLabel.setText(message);
        emailUpdateMessageLabel.setStyle("-fx-text-fill: red;");
    }

    private void displayPasswordUpdateSuccess(String message) {
        passwordUpdateMessageLabel.setText(message);
        passwordUpdateMessageLabel.setStyle("-fx-text-fill: green;");
    }

    private void displayPasswordUpdateError(String message) {
        passwordUpdateMessageLabel.setText(message);
        passwordUpdateMessageLabel.setStyle("-fx-text-fill: red;");
    }

    public void openFileChooser() {
        List<File> selectedFiles = FileChooserUtil.openFileChooser(false); // false for single file selection

        if (!selectedFiles.isEmpty()) {
            File selectedFile = selectedFiles.get(0);
            Image image = new Image(selectedFile.toURI().toString());
            profileImageView.setImage(image);
        } else {
            System.out.println("File selection cancelled.");
        }
    }

}
