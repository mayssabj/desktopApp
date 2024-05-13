package edu.esprit.controller.user;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import edu.esprit.entities.User;
import edu.esprit.entities.VerificationCode;
import edu.esprit.services.MailService;
import edu.esprit.entities.VerificationCode;
import edu.esprit.services.MailService;
import edu.esprit.services.UserService;
import edu.esprit.utils.*;
import edu.esprit.utils.VerificationCodeUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public class RegistrationController implements Initializable {
    @FXML
    public Label registerMessageLabel;
    @FXML
    public TextField emailField;
    @FXML
    public VBox emailErrorsContainer;
    @FXML
    public PasswordField passwordField;
    @FXML
    public VBox passwordErrorsContainer;
    @FXML
    public Button registerButton;
    @FXML
    public VBox genderErrorsContainer;
    @FXML
    private TextField confirmPasswordField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;
    @FXML
    private VBox confirmPasswordErrorsContainer;
    @FXML
    private VBox phoneErrorsContainer;
    @FXML
    private VBox addressErrorsContainer;
    @FXML
    private Button uploadImageButton;

    @FXML
    private ImageView profileImageView;

    ObservableList<String> genderList = FXCollections.observableArrayList("Male","Female","Prefer Not To Say");
    @FXML
    private ChoiceBox genderChoiceBox;

    private Cloudinary cloudinary;

    private File selectedImageFile;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image defaultImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/default_user.png")));
        profileImageView.setImage(defaultImage);
        // Additional initialization...
        // Create a circular clip
        // Set the size of the ImageView
        profileImageView.setFitWidth(100);
        profileImageView.setFitHeight(100);

        // Do not preserve the aspect ratio to allow the image to fill the circle
        profileImageView.setPreserveRatio(false);
        Circle clip = new Circle(50, 50, 50); // Assuming the ImageView is 100x100
        profileImageView.setClip(clip);

        // Apply CSS styling for a gray border
        profileImageView.setStyle("-fx-border-color: red;-fx-background-size: cover;-fx-background-position: center ; -fx-border-width: 2; -fx-border-radius: 50;");


        genderChoiceBox.setValue("Prefer Not To Say");
        genderChoiceBox.setItems(genderList);
    }

    public void register(ActionEvent event) {
        boolean isValid = true;
        emailErrorsContainer.getChildren().clear();
        passwordErrorsContainer.getChildren().clear();
        confirmPasswordErrorsContainer.getChildren().clear();
        phoneErrorsContainer.getChildren().clear();
        addressErrorsContainer.getChildren().clear();

        registerMessageLabel.setText("");

        List<String> emailErrors = ValidationUtils.validateEmail(emailField.getText());
        if (!emailErrors.isEmpty()) {
            isValid = false;
            displayErrors(emailErrorsContainer, emailErrors);
        }
        List<String> passwordErrors = ValidationUtils.validatePassword(passwordField.getText());
        if (!passwordErrors.isEmpty()) {
            isValid = false;
            displayErrors(passwordErrorsContainer, passwordErrors);
        }
        List<String> confirmPasswordErrors = ValidationUtils.validateConfirmPassword(passwordField.getText(), confirmPasswordField.getText());
        if (!confirmPasswordErrors.isEmpty()) {
            isValid = false;
            displayErrors(confirmPasswordErrorsContainer, confirmPasswordErrors);
        }
        List<String> phoneErrors = ValidationUtils.validatePhone(phoneField.getText());
        if (!phoneErrors.isEmpty()) {
            isValid = false;
            displayErrors(phoneErrorsContainer, phoneErrors);
        }

        if (isValid) {
            registerButton.setText("Checking...");
            User user = new User();
            user.setEmail(emailField.getText());
            // Hash the password before setting it
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(passwordField.getText());

            user.setPassword(hashedPassword);
            user.setPhone(phoneField.getText());
//            user.setPhoto(getImagePath(profileImageView));
            if(selectedImageFile != null){
                String imageUrl = CloudinaryUtil.uploadImage(selectedImageFile);
                user.setPhoto(imageUrl);
            }
            user.setAddress(addressField.getText());
            user.setGender(genderChoiceBox.getValue().toString());
            user.setUsername(user.getEmail().split("@")[0]);



            UserService userService = new UserService();
            String verificationCode = VerificationCodeUtil.generateVerificationCode();
            VerificationCode code = new VerificationCode(user, verificationCode, 30); // Expiry time in minutes
            boolean registrationSuccessful = userService.registerUser(user, code);

            if (registrationSuccessful) {
                MailService mailService = new MailService();
                mailService.sendEmail(user.getEmail(), "Email verification", "Your verification code is: " + code.getCode());
                registerMessageLabel.setText("Registration successful, Please verify your email.");
                registerMessageLabel.setStyle("-fx-text-fill: green; -fx-font-size: 14px;"); // Set text color to green
                registerMessageLabel.setVisible(true);
                Session.getInstance().setVerificationEmail(user.getEmail());
                NavigationUtil.redirectTo("/user/verificationCode.fxml",event);
            } else {
                registerMessageLabel.setText("Registration failed. Please try again.");
                registerMessageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;"); // Set text color to red
                registerMessageLabel.setVisible(true);
            }
            registerButton.setText("Register");
        }
    }

    public void displayErrors(VBox container, List<String> errors) {
        for (String error : errors) {
            Label errorLabel = new Label(error);
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12;"); // Increase font size to 14px
            container.getChildren().add(errorLabel);
        }
    }



    public void openLogin(MouseEvent event) {
        NavigationUtil.redirectTo("/user/login.fxml", event);
    }


    public void openFileChooser() {
        List<File> selectedFiles = FileChooserUtil.openFileChooser(false); // false for single file selection

        if (!selectedFiles.isEmpty()) {
            selectedImageFile = selectedFiles.get(0);
            Image image = new Image(selectedImageFile.toURI().toString());
            profileImageView.setImage(image);
        } else {
            System.out.println("File selection cancelled.");
        }
    }

    // Method to get the image path from the ImageView
    private String getImagePath(ImageView imageView) {
        if (imageView.getImage() == null || imageView.getImage().getUrl() == null) {
            return null; // No image selected or no URL for the image
        }
        return imageView.getImage().getUrl().replace("file:", "");
    }


}
