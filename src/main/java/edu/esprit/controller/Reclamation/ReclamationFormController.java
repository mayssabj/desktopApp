package edu.esprit.controller.Reclamation;

import edu.esprit.entities.Reclamation;
import edu.esprit.services.ServiceReclamation;
import edu.esprit.services.TranslationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.control.textfield.AutoCompletionBinding;

public class ReclamationFormController {

    @FXML
    private TextField subject;
    @FXML
    private TextArea description;
    @FXML
    private TextField repertodusername;
    @FXML
    private ChoiceBox<String> typereclamation;
    @FXML
    private Button screenshoot;
    @FXML
    private ImageView imageView;

    private ServiceReclamation serviceReclamation = new ServiceReclamation();

    @FXML
    public void initialize() {

        typereclamation.getItems().addAll("Violation", "Inappropriate Content", "Other");  // Populate the choice box with example items
        typereclamation.setValue("Other");  // Set a default value
    }

    public void saveReclamation() {
        // Validation checks
        if (subject.getText().isEmpty() || description.getText().isEmpty() || repertodusername.getText().isEmpty() || typereclamation.getValue() == null) {
            showAlert("Error", "All fields must be filled!", Alert.AlertType.ERROR);
            return;
        }

        // Translate both subject and description from English to French
        String translatedSubject = TranslationService.translateText(subject.getText(), "en", "fr");
        String translatedDescription = TranslationService.translateText(description.getText(), "en", "fr");

        // Check if translations are empty and handle it
        if (translatedSubject.isEmpty() || translatedDescription.isEmpty()) {
            showAlert("Error", "Translation failed or returned empty.", Alert.AlertType.ERROR);
            translatedSubject = subject.getText(); // Fallback to original text
            translatedDescription = description.getText(); // Fallback to original text
        }

        // Create a new reclamation object with translated data
        Reclamation reclamation = new Reclamation();
        reclamation.setSubjuct(translatedSubject);
        reclamation.setDescription(translatedDescription);
        reclamation.setReported_username(repertodusername.getText());
        reclamation.setType_reclamation(typereclamation.getValue());
        reclamation.setScreenshot(screenshoot.getText()); // Assuming the screenshot button holds the path temporarily

        try {
            serviceReclamation.ajouterReclamation(reclamation);
            showAlert("Success", "Reclamation saved successfully!", Alert.AlertType.INFORMATION);
            clear();  // Clear form after save

            // Close the current form
            Stage currentStage = (Stage) subject.getScene().getWindow();
            currentStage.close();

            // Redirect to the listReclamation view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/ReclamationList.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("List of Reclamations");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            showAlert("Error", "Failed to save reclamation: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    public void clear() {
        subject.clear();
        description.clear();
        repertodusername.clear();
        typereclamation.setValue("Other");
        imageView.setImage(null);
        screenshoot.setText("");  // Clear the screenshot path if used
    }

    @FXML
    public void ajoutScreenshoot() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Screenshot");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            screenshoot.setText(file.toURI().toString());  // Store the path in the screenshot button
        } else {
            showAlert("Error", "No file selected!", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //function pour l'auto complete



}
