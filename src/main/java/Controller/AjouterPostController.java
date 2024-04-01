package Controller;

import entite.Post;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import services.PostCRUD;

import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

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
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private TextField placeField;

    @FXML
    private Button buttonAjouter;

    @FXML
    private Text errorTitre;

    @FXML
    private Text errorDescription;
    private File selectedImageFile;

    @FXML
    public void initialize() {
        // Set the DatePicker to January 1, 2021
        datePicker.setValue(LocalDate.of(2021, 1, 1));
    }

    @FXML
    public void ajouterPost() throws SQLException {
        if (isInputValid()) {
            String titre = titreField.getText();
            String description = descriptionArea.getText();
            String image = (selectedImageFile != null) ? selectedImageFile.getPath() : "";
            // Assuming datePicker.getValue() returns a LocalDate, not a Date. If it returns a Date, then keep it as it was.
            // Get the LocalDate from the DatePicker
            LocalDate localDate = datePicker.getValue();
            // Convert the LocalDate to java.sql.Date
            Date date = Date.valueOf(localDate);
            // Now you can use the 'date' object as needed
            Post.Type type = Post.Type.valueOf(typeComboBox.getValue());
            String place = placeField.getText();

            Post post = new Post(titre, description, image, date, type, place);
            PostCRUD service = new PostCRUD();
            service.ajouter(post);

            // Clear fields after successful addition
            titreField.clear();
            descriptionArea.clear();
            imageView.setImage(null);
            selectedImageFile = null;
            datePicker.setValue(null);
            typeComboBox.setValue(null);
            placeField.clear();
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

        // Add more validation checks as needed

        return isValid;
    }
}
