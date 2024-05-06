package edu.esprit.controller;

import edu.esprit.entities.Sponsoring;
import edu.esprit.services.SponsoringService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

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
    public void uploadImage(File imageFile) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        HttpPost httpPost = new HttpPost("http://localhost:8000/upload-image");

        HttpEntity requestEntity = MultipartEntityBuilder.create()
                .addBinaryBody("image", imageFile, ContentType.APPLICATION_OCTET_STREAM, imageFile.getName())
                .build();

        httpPost.setEntity(requestEntity);
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy()).build();

        HttpClient httpClient = HttpClients.custom().setSslcontext(sslContext).build();
        HttpResponse response = httpClient.execute(httpPost);
        System.out.println(response);

        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == 200) {
            Header contentDispositionHeader = response.getFirstHeader("Content-Disposition");
            if (contentDispositionHeader != null) {
                String contentDisposition = contentDispositionHeader.getValue();
                System.out.println("Success upload. Filename: ");
            } else {
                System.out.println("Success upload, but filename not found in the response");
            }
        } else {
            System.out.println("Failed upload");
        }
    }


    @FXML
    public void ajouterSponsoring() throws SQLException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        // Validate input fields before proceeding
        if (isInputValid()) {
            String name = nameField.getText();
            String description = descriptionArea.getText();
            uploadImage(selectedImageFile);
            String image = (selectedImageFile != null) ? selectedImageFile.getName() : "";
            // Assuming datePicker.getValue() returns a LocalDate, not a Date. If it returns a Date, then keep it as it was.
            LocalDate localDate = datePicker.getValue();
            Date date = Date.valueOf(localDate); // Utilisez la méthode valueOf de java.sql.Date pour la conversion

            Sponsoring.Duration contrat = Sponsoring.Duration.valueOf(contratComboBox.getValue());
            Sponsoring.TypeSpon type = Sponsoring.TypeSpon.valueOf(typeComboBox.getValue());


            // Traiter les données comme vous le souhaitez, par exemple, les envoyer à une méthode de service
            System.out.println("Name: " + name);
            System.out.println("Description: " + description);
            System.out.println("Image: " + selectedImageFile.getName());
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
        if (nameField.getText().isEmpty() || !nameField.getText().matches("^[\\p{L} \\s]+$")) {
            errorname.setText("Name is required and should not contain numbers");
            isValid = false;
        } else {
            errorname.setText("");
        }
        if (descriptionArea.getText().isEmpty() || !descriptionArea.getText().matches("^[\\p{L} \\s]+$")) {
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
