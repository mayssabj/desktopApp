package edu.esprit.controller;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import edu.esprit.entities.Post;
import edu.esprit.entities.User;
import edu.esprit.services.PostCRUD;
import edu.esprit.services.UserService;

import java.io.File;
import java.sql.SQLException;

public class AjouterPostController {

    @FXML
    private TextField titreField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button selectImage;

    @FXML
    private ImageView imageView;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private WebView mapView;

    @FXML
    private Text errorTitre;

    @FXML
    private Text errorDescription;

    @FXML
    private Text errorImage;

    @FXML
    private Text errorType;

    @FXML
    private Text errorPlace;

    private File selectedImageFile;

    private UserService userService; // Inject UserService

    @FXML
    public void ajouterPost() throws SQLException {
        if (isInputValid()) {
            UserService userService = new UserService();
            User u1 = userService.getCurrentLoggedInUser();

            if (u1 != null) {
                String titre = titreField.getText();
                String description = descriptionArea.getText();
                String image = (selectedImageFile != null) ? selectedImageFile.getPath() : "";
                Post.Type type = Post.Type.valueOf(typeComboBox.getValue());
                String place = getPlaceFromMap();

                Post post = new Post(titre, description, image, type, place, u1.getId());
                PostCRUD service = new PostCRUD();
                service.ajouter(post);

                titreField.clear();
                descriptionArea.clear();
                imageView.setImage(null);
                selectedImageFile = null;
                typeComboBox.setValue(null);
            } else {
                System.out.println("User not logged in");
            }
        }
    }

    private String getPlaceFromMap() {
        String place = (String) mapView.getEngine().executeScript("getLocationFromMap()");
        return place != null ? place : "";
    }

    @FXML
    void selectImageAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        selectedImageFile = fileChooser.showOpenDialog(null);

        if (selectedImageFile != null) {
            Image image = new Image(selectedImageFile.toURI().toString());
            imageView.setImage(image);
        }
    }

    private boolean isInputValid() {
        boolean isValid = true;

        if (titreField.getText().isEmpty()) {
            errorTitre.setText("Title is required");
            isValid = false;
        } else {
            errorTitre.setText("");
        }

        if (descriptionArea.getText().isEmpty()) {
            errorDescription.setText("Description is required");
            isValid = false;
        } else {
            errorDescription.setText("");
        }

        if (selectedImageFile == null || !selectedImageFile.exists()) {
            errorImage.setText("Image is required");
            isValid = false;
        } else {
            errorImage.setText("");
        }

        if (typeComboBox.getValue() == null || typeComboBox.getValue().trim().isEmpty()) {
            errorType.setText("Type is required");
            isValid = false;
        } else {
            errorType.setText("");
        }

        return isValid;
    }

    @FXML
    private void returnToPostsPage(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        WebEngine webEngine = mapView.getEngine();
        webEngine.load("https://www.openstreetmap.org/export/embed.html?bbox=-2.8785%2C53.2041%2C-2.5785%2C53.4041&layer=mapnik");

        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.FAILED) {
                System.out.println("Failed to load map: " + webEngine.getLocation());
            }
        });
    }
}
