package edu.esprit.controller;

import edu.esprit.entities.Sponsoring;
import edu.esprit.services.SponsoringService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

public class ajouterSponsoring {

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
    private Button buttonajouter;
    @FXML
    private Text errorname;
    @FXML
    private Text errordescription;
    private File selectedImageFile;

    @FXML
    public void ajouterSponsoring() throws SQLException {
        // Validate input fields before proceeding
        if (isInputValid()) {
            String name = nameField.getText();
            String description = descriptionArea.getText();
            String image = (selectedImageFile != null) ? selectedImageFile.getPath() : "";
            // Assuming datePicker.getValue() returns a LocalDate, not a Date. If it returns a Date, then keep it as it was.
            LocalDate localDate = datePicker.getValue();
            Date date = Date.valueOf(localDate); // Utilisez la méthode valueOf de java.sql.Date pour la conversion

            Sponsoring.Duration contrat = Sponsoring.Duration.valueOf(contratComboBox.getValue());
            Sponsoring.TypeSpon type = Sponsoring.TypeSpon.valueOf(typeComboBox.getValue());


            // Traiter les données comme vous le souhaitez, par exemple, les envoyer à une méthode de service
            System.out.println("Name: " + name);
            System.out.println("Description: " + description);
            System.out.println("Image: " + image);
            System.out.println("Date: " + date);
            System.out.println("Contrat: " + contrat);
            System.out.println("Type: " + type);

            Sponsoring sponsoring = new Sponsoring(name, description, image, date, contrat, type);
            SponsoringService service = new SponsoringService();
            service.ajouterSponsoring(sponsoring);
            nameField.clear();
            descriptionArea.clear();
            datePicker.setValue(null);
            contratComboBox.setValue(null);
            typeComboBox.setValue(null);
            imageView.setImage(null);
            selectedImageFile = null;
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
    @FXML
    private void handlesponsorButtonClick() {
        try {
            // Charger le fichier FXML de la nouvelle page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/showsponsoring.fxml"));
            Parent root = loader.load();

            // Créer la scène avec la nouvelle page
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton cliqué
            Stage stage = (Stage) buttonajouter.getScene().getWindow();

            // Remplacer la scène actuelle par la nouvelle scène
            stage.setScene(scene);
            stage.show();



        } catch (Exception e) {
            e.printStackTrace();  // Gérer les exceptions en conséquence
        }
    }
    @FXML
    void initialize()  {

        assert nameField != null : "fx:id=\"nameFiled\" was not injected: check your FXML file 'ajouter.fxml'.";
        assert descriptionArea != null : "fx:id=\"descriptionArea\" was not injected: check your FXML file 'ajouter.fxml'.";
        //assert image != null : "fx:id=\"image\" was not injected: check your FXML file 'ajouter.fxml'.";
        assert contratComboBox != null : "fx:id=\"contratComboBox\" was not injected: check your FXML file 'ajouter.fxml'.";
        assert typeComboBox != null : "fx:id=\"typeComboBox\" was not injected: check your FXML file 'ajouter.fxml'.";
        assert buttonajouter != null : "fx:id=\"buttonajouter\" was not injected: check your FXML file 'ajouter.fxml'.";

    }
}
