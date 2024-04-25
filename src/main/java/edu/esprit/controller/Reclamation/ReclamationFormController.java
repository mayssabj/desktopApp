package edu.esprit.controller.Reclamation;

import edu.esprit.entities.Reclamation;
import edu.esprit.services.ServiceReclamation;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;


import java.io.File;

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

    @FXML
    public void saveReclamation() {
        // Validation checks
        if (subject.getText().isEmpty() || description.getText().isEmpty() || repertodusername.getText().isEmpty() || typereclamation.getValue() == null) {
            showAlert("Error", "All fields must be filled!", Alert.AlertType.ERROR);
            return;
        }

        // Create a new reclamation object
        Reclamation reclamation = new Reclamation();
        reclamation.setSubjuct(subject.getText());
        reclamation.setDescription(description.getText());
        reclamation.setReported_username(repertodusername.getText());
        reclamation.setType_reclamation(typereclamation.getValue());
        reclamation.setScreenshot(screenshoot.getText()); // Assuming the screenshot button holds the path temporarily

        // Save reclamation using service
        try {
            serviceReclamation.ajouterReclamation(reclamation);
            showAlert("Success", "Reclamation saved successfully!", Alert.AlertType.INFORMATION);
            clear();  // Clear form after save
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
}
