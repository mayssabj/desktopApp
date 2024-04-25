package edu.esprit.controller.Reclamation;

import edu.esprit.entities.Reclamation;
import edu.esprit.services.ServiceReclamation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import javafx.stage.Stage;

import java.io.File;

public class ReclamationEditFormController {

    @FXML private TextField subject;
    @FXML private TextArea description;
    @FXML private TextField reportedUsername;
    @FXML private ChoiceBox<String> typeReclamation;
    @FXML private ImageView imageView;
    @FXML private Button uploadScreenshot;

    private Reclamation currentReclamation;
    private ServiceReclamation serviceReclamation = new ServiceReclamation();

    public void initData(Reclamation reclamation) {
        currentReclamation = reclamation;
        subject.setText(reclamation.getSubjuct());
        description.setText(reclamation.getDescription());
        reportedUsername.setText(reclamation.getReported_username());
        typeReclamation.setValue(reclamation.getType_reclamation());
        if (reclamation.getScreenshot() != null && !reclamation.getScreenshot().isEmpty()) {
            imageView.setImage(new Image(reclamation.getScreenshot()));
        }
    }

    @FXML
    void updateReclamation() {
        if (subject.getText().isEmpty() || description.getText().isEmpty() || reportedUsername.getText().isEmpty() || typeReclamation.getValue() == null) {
            showAlert("Error", "All fields must be filled!", Alert.AlertType.ERROR);
            return;
        }

        currentReclamation.setSubjuct(subject.getText());
        currentReclamation.setDescription(description.getText());
        currentReclamation.setReported_username(reportedUsername.getText());
        currentReclamation.setType_reclamation(typeReclamation.getValue());
        currentReclamation.setScreenshot(uploadScreenshot.getText()); // Assuming the uploadScreenshot button holds the path temporarily

        try {
            serviceReclamation.modifierReclamation(currentReclamation); // Assuming there is a method to update
            showAlert("Success", "Reclamation updated successfully!", Alert.AlertType.INFORMATION);
            redirectToReclamationList();
        } catch (Exception e) {
            showAlert("Error", "Failed to update reclamation: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void ajoutScreenshoot() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Screenshot");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            uploadScreenshot.setText(file.toURI().toString());  // Store the path in the uploadScreenshot button
        } else {
            showAlert("Error", "No file selected!", Alert.AlertType.ERROR);
        }
    }
    private void redirectToReclamationList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/ReclamationList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) subject.getScene().getWindow(); // Get the current window
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Reclamation List view: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void clear() {
        subject.clear();
        description.clear();
        reportedUsername.clear();
        typeReclamation.setValue(null);
        imageView.setImage(null);
        uploadScreenshot.setText("");  // Clear the screenshot path if used
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}