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
import netscape.javascript.JSObject;

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

    private MapDataExtractor mapDataExtractor;

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

    public class JavaConnector {

        public void getLocationFromMap(String location) {
            System.out.println("Location received from map: " + location);
            // Handle the received location data here
        }
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

    @FXML
    private void initialize() {
        // Load the HTML content into the WebView
        WebEngine webEngine = mapView.getEngine();
        webEngine.loadContent("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Map</title>\n" +
                "    <script>\n" +
                "        function getLocationFromMap() {\n" +
                "            // Fonction d'implémentation\n" +
                "            // Récupérer les données de localisation depuis la carte et les renvoyer à Java\n" +
                "            var locationData = \"Données de localisation\"; // Remplacer par les données de localisation réelles\n" +
                "            javaConnector.getLocationFromMap(locationData);\n" +
                "        }\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <!-- Exemple de contenu de carte -->\n" +
                "    <div id=\"map\" style=\"width: 100%; height: 400px;\"></div>\n" +
                "\n" +
                "    <script>\n" +
                "        function initMap() {\n" +
                "            // Créer une nouvelle carte Google Maps\n" +
                "            var map = new google.maps.Map(document.getElementById('map'), {\n" +
                "                center: {lat: -34.397, lng: 150.644},\n" +
                "                zoom: 8\n" +
                "            });\n" +
                "\n" +
                "            // Ajouter un marqueur à la position spécifiée\n" +
                "            var marker = new google.maps.Marker({\n" +
                "                position: {lat: -34.397, lng: 150.644},\n" +
                "                map: map,\n" +
                "                title: 'Hello World!'\n" +
                "            });\n" +
                "        }\n" +
                "    </script>\n" +
                "\n" +
                "    <!-- Charger l'API Google Maps -->\n" +
                "    <script src=\"https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&callback=initMap\" async defer></script>\n" +
                "</body>\n" +
                "</html>\n");

        // Add a listener to inject the JavaConnector object once the page is loaded
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                // Once the page is loaded, inject a Java object into the JavaScript context
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaConnector", new JavaConnector());
            }
        });
    }
}
