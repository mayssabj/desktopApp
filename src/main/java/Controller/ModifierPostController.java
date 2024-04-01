package Controller;

import entite.Post;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.PostCRUD;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDate;

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

    @FXML
    private Text errorTitre;

    @FXML
    private Text errorDescription;
    private File selectedImageFile;

    private Post post;

    @FXML
    private Text errorname;

    @FXML
    private Text errordescription;


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
            // Optionally, set a placeholder image if the file is not found
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

        // Validate and display error messages
        if (titreField.getText().isEmpty() || !titreField.getText().matches("^[a-zA-Z]+$")) {
            errorname.setText("Name is required and should not contain numbers");
            isValid = false;
        } else {
            errorname.setText("");
        }
        if (descriptionArea.getText().isEmpty()) {
            errordescription.setText("Description is required and should not contain numbers");
            isValid = false;
        } else {
            errordescription.setText("");
        }
        return isValid;
    }
}
