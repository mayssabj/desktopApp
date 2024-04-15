package Controller;

import entite.Post;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.PostCRUD;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

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
    private TextField placeField;

    @FXML
    private Button buttonAjouter;

    @FXML
    private Text errorTitre;

    @FXML
    private Text errorPlace;

    @FXML
    private Text errorType;

    @FXML
    private Text errorImage;




    @FXML
    private Text errorDescription;
    private File selectedImageFile;




    @FXML
    public void ajouterPost() throws SQLException {
        if (isInputValid()) {
            String titre = titreField.getText();
            String description = descriptionArea.getText();
            String image = (selectedImageFile != null) ? selectedImageFile.getPath() : "";
            Post.Type type = Post.Type.valueOf(typeComboBox.getValue());
            String place = placeField.getText();

            Post post = new Post(titre, description, image, type, place);
            PostCRUD service = new PostCRUD();
            service.ajouter(post);

            titreField.clear();
            descriptionArea.clear();
            imageView.setImage(null);
            selectedImageFile = null;
            typeComboBox.setValue(null);
            placeField.clear();


            returnToPostsPage();
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

        if (placeField.getText().isEmpty()) {
            errorPlace.setText("Place is required");
            isValid = false;
        } else {
            errorPlace.setText("");
        }

        if (typeComboBox.getValue() == null || typeComboBox.getValue().trim().isEmpty()) {
            errorType.setText("Type is required");
            isValid = false;
        } else {
            errorType.setText("");
        }

        if (selectedImageFile == null || !selectedImageFile.exists()) {
            errorImage.setText("Image is required");
            isValid = false;
        } else {
            errorImage.setText("");
        }


        return isValid;
    }

    private void returnToPostsPage() {
        try {
            Node postPage = FXMLLoader.load(getClass().getResource("/post.fxml"));
            Scene scene = new Scene((Parent) postPage);
            Stage stage = (Stage) titreField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void returnToPostsPage(ActionEvent event) {
        try {
            Parent postPage = FXMLLoader.load(getClass().getResource("/post.fxml"));
            Scene scene = new Scene(postPage);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private WebView mapWebView;


    public void initialize(URL location, ResourceBundle resources) {
        WebEngine webEngine = mapWebView.getEngine();
        webEngine.load("https://maps.google.com"); // Load Google Maps URL
    }


}
