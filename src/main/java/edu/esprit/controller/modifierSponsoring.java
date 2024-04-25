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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.sql.SQLException;

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
        contratComboBox.setValue(sponsoring.getContrat().toString());
        typeComboBox.setValue(sponsoring.getType().toString());

        if (sponsoring.getImage() != null && !sponsoring.getImage().isEmpty()) {
            try {
                Image image = new Image(new FileInputStream(sponsoring.getImage()));
                imageView.setImage(image);
            } catch (FileNotFoundException e) {
                System.err.println("Image file not found, setting default image.");
                imageView.setImage(new Image("/path/to/default/image.png")); // Set a default or placeholder image
            }
        }
    }

    @FXML
    public void modifierSponsoring() throws SQLException {
        if (isInputValid()) {
            sponsoring.setName(nameField.getText());
            sponsoring.setDescription(descriptionArea.getText());
            sponsoring.setDate(Date.valueOf(datePicker.getValue()));
            sponsoring.setContrat(Sponsoring.Duration.valueOf(contratComboBox.getValue()));
            sponsoring.setType(Sponsoring.TypeSpon.valueOf(typeComboBox.getValue()));

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
        File newFile = fileChooser.showOpenDialog(null);
        if (newFile != null) {
            selectedImageFile = newFile;
            Image image = new Image(newFile.toURI().toString());
            imageView.setImage(image);
        }
    }

    private boolean isInputValid() {
        boolean isValid = true;
        if (nameField.getText().isEmpty() || !nameField.getText().matches("^[a-zA-Z ]+$")) {
            errorname.setText("Name is required and should not contain numbers");
            isValid = false;
        } else {
            errorname.setText("");
        }

        if (descriptionArea.getText().isEmpty()) {
            errordescription.setText("Description is required");
            isValid = false;
        } else {
            errordescription.setText("");
        }

        return isValid;
    }
}
