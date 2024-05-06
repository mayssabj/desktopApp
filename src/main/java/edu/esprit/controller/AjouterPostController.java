package edu.esprit.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import edu.esprit.entities.User;
import edu.esprit.utils.NavigationUtil;
import edu.esprit.utils.Session;
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
import edu.esprit.services.PostCRUD;
import edu.esprit.services.UserService;
import netscape.javascript.JSObject;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class AjouterPostController {

    @FXML
    private TextField titreField;

    @FXML
    private TextArea descriptionArea;

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

    @FXML
    private ImageView imageView;

    private File selectedImageFile;

    private MapDataExtractor mapDataExtractor;

    private UserService userService; // Inject UserService

    private Cloudinary cloudinary;

    @FXML
    private WebView mapWebView;

    public AjouterPostController() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dx5cteclw",
                "api_key", "489998819632647",
                "api_secret", "b_TAcmWLYB6jDor9fK9KZ3KalXQ"));
    }

    public void ajouterPost() throws SQLException, IOException {
        if (isInputValid()) {
            UserService userService = new UserService();
            User u1 = Session.getInstance().getCurrentUser();

            if (u1 != null) {
                String titre = titreField.getText();
                String description = descriptionArea.getText();
                String imageUrl = ""; // Initialize imageUrl variable
                Post.Type type = Post.Type.valueOf(typeComboBox.getValue());
                String place = ""; // Initialize place variable

                if (mapDataExtractor != null) {
                    place = mapDataExtractor.getSelectedLocation(mapView.getEngine()); // Get the selected location
                }

                if (selectedImageFile != null) {
                    // Upload image to Cloudinary
                    Map uploadResult = cloudinary.uploader().upload(selectedImageFile, ObjectUtils.emptyMap());
                    imageUrl = (String) uploadResult.get("url");
                }

                Post post = new Post(titre, description, imageUrl, type, place, u1.getId());
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
            NavigationUtil.redirectTo("/dashb.fxml", event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        // Load the HTML content into the WebView
        loadMap(0, 0);
    }

    private void loadMap(float latitude, float longitude) {
        WebEngine webEngine = mapWebView.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                // Inject JavaScript code to retrieve user's location
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaBridge", new JavaBridge()); // JavaBridge is a class defined below

                // Call JavaScript function to get user's location
                webEngine.executeScript("navigator.geolocation.getCurrentPosition(function(position) {" +
                        "javaBridge.updateLocation(position.coords.latitude, position.coords.longitude);" +
                        "});");
            }
        });

        String html = "<html>"
                + "<head>"
                + "<link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet/dist/leaflet.css\" />"
                + "<script src=\"https://unpkg.com/leaflet/dist/leaflet.js\"></script>"
                + "</head>"
                + "<body>"
                + "<div id=\"map\" style=\"height: 400px;\"></div>"
                + "<script>"
                + "var map = L.map('map').setView([" + latitude + ", " + longitude + "], 13);"
                + "L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {"
                + "attribution: '© OpenStreetMap contributors'"
                + "}).addTo(map);"
                + "map.on('click', function(e) {" +
                "javaBridge.onMapClick(e.latlng.lat, e.latlng.lng);" +
                "});" +
                "</script>"
                + "</body>"
                + "</html>";
        webEngine.loadContent(html);
        System.out.println("Initial location: " + latitude + ", " + longitude);
    }

    // Define a Java class to bridge between Java and JavaScript
    public class JavaBridge {
        public void onMapClick(double latitude, double longitude) {
            System.out.println("Clicked location: " + latitude + ", " + longitude);
            // You can handle the clicked location here
        }
    }



    /*private void loadMap(float latitude, float longitude) {
        WebEngine webEngine = mapWebView.getEngine();
        String html = "<html>"
                + "<head>"
                + "<link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet/dist/leaflet.css\" />"
                + "<script src=\"https://unpkg.com/leaflet/dist/leaflet.js\"></script>"
                + "</head>"
                + "<body>"
                + "<div id=\"map\" style=\"height: 400px;\"></div>"
                + "<script>"
                + "var map = L.map('map').setView([" + latitude + ", " + longitude + "], 13);"
                + "L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {"
                + "attribution: '© OpenStreetMap contributors'"
                + "}).addTo(map);"
                + "L.marker([" + latitude + ", " + longitude + "]).addTo(map)"
                + ".bindPopup('Your event').openPopup();"
                + "</script>"
                + "</body>"
                + "</html>";
        webEngine.loadContent(html);
    }*/

}
