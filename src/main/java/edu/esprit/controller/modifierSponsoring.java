package edu.esprit.controller;

import edu.esprit.entities.Sponsoring;
import edu.esprit.services.SponsoringService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;

public class modifierSponsoring {

    @FXML
    private TextField nameField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button selectImage;

    @FXML
    private ImageView imageView;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> contratComboBox;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private Button buttonModifier;

    @FXML
    private Text errorname;

    @FXML
    private Text errordescription;

    private File selectedImageFile;

    private Sponsoring sponsoring;

    public void initData(Sponsoring sponsoring) {
        this.sponsoring = sponsoring;
        nameField.setText(sponsoring.getName());
        descriptionArea.setText(sponsoring.getDescription());
        contratComboBox.setValue(sponsoring.getContrat().toString()); // Assuming getContrat() returns a Duration enum value
        typeComboBox.setValue(sponsoring.getType().toString()); // Assuming getType() returns a TypeSpon enum value
        // Set other fields similarly
    }


    @FXML
    public void modifierSponsoring() throws SQLException {
        // Validate input fields before proceeding
        if (isInputValid()) {
            sponsoring.setName(nameField.getText());
            sponsoring.setDescription(descriptionArea.getText());
            sponsoring.setDate(Date.valueOf(datePicker.getValue())); // Convert LocalDate to Date
            sponsoring.setContrat(Sponsoring.Duration.valueOf(contratComboBox.getValue())); // Assuming Contrat is an enum
            sponsoring.setType(Sponsoring.TypeSpon.valueOf(typeComboBox.getValue())); // Assuming TypeSpon is an enum
            // Update other fields of the sponsoring object

            // Update the image if a new one is selected
            if (selectedImageFile != null) {
                sponsoring.setImage(selectedImageFile.getAbsolutePath());
            }

            SponsoringService service = new SponsoringService();
            service.modifierSponsoring(sponsoring);
            Stage stage = (Stage) buttonModifier.getScene().getWindow();
            stage.close();
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
            // Display the selected image in the ImageView
            Image image = new Image(selectedImageFile.toURI().toString());
            imageView.setImage(image);
        }
    }
    private boolean isInputValid() {
        boolean isValid = true;

        // Validate and display error messages
        if (nameField.getText().isEmpty() || !nameField.getText().matches("^[a-zA-Z]+$")) {
            errorname.setText("Name is required and should not contain numbers");
            isValid = false;
        } else {
            errorname.setText("");
        }
        if (descriptionArea.getText().isEmpty() || !descriptionArea.getText().matches("^[a-zA-Z]+$")) {
            errordescription.setText("Description is required and should not contain numbers");
            isValid = false;
        } else {
            errordescription.setText("");
        }
        return isValid;
    }
}
