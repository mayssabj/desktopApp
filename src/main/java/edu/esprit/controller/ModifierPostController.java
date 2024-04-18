package edu.esprit.controller;

import edu.esprit.entities.Post;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import edu.esprit.services.PostCRUD;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;

public class ModifierPostController {

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
    private Button buttonModifier;

    private File selectedImageFile;

    private Post post;

    @FXML
    private Text errorTitre;

    @FXML
    private Text errorPlace;

    @FXML
    private Text errorType;




    @FXML
    private Text errorDescription;




    public void initData(Post post) {
        this.post = post;
        titreField.setText(post.getTitre());
        descriptionArea.setText(post.getDescription());
        typeComboBox.setValue(post.getType().toString());
        placeField.setText(post.getPlace());

        try {
            Image image = new Image(new FileInputStream(post.getImageUrl()));
            imageView.setImage(image);
        } catch (FileNotFoundException e) {
            System.out.println("Image file not found: " + e.getMessage());
            imageView.setImage(new Image("file:path/to/placeholder/image.png"));
        }
    }



    @FXML
    public void modifierPost() throws SQLException {
        if (isInputValid()) {
            post.setTitre(titreField.getText());
            post.setDescription(descriptionArea.getText());
            post.setPlace(placeField.getText());
            post.setType(Post.Type.valueOf(typeComboBox.getValue()));

            if (selectedImageFile != null) {
                post.setImageUrl(selectedImageFile.getAbsolutePath());
            }

            PostCRUD service = new PostCRUD();
            service.modifier(post);
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


        return isValid;
    }
}
