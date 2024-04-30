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
            Image image = new Image("http://localhost:8000/uploads/" + sponsoring.getImage());

            imageView.setImage(image);

        }
    }

    @FXML
    public void modifierSponsoring() throws SQLException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        if (isInputValid()) {
            sponsoring.setName(nameField.getText());
            sponsoring.setDescription(descriptionArea.getText());
            sponsoring.setDate(Date.valueOf(datePicker.getValue()));
            sponsoring.setContrat(Sponsoring.Duration.valueOf(contratComboBox.getValue()));
            sponsoring.setType(Sponsoring.TypeSpon.valueOf(typeComboBox.getValue()));

            if (selectedImageFile != null) {
                uploadImage(selectedImageFile);
                sponsoring.setImage(selectedImageFile.getName());
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
        if (nameField.getText().isEmpty() || !nameField.getText().matches("^[\\p{L} \\s]+$")) {
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
    public void uploadImage(File imageFile) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        HttpPost httpPost = new HttpPost("http://localhost:8000/upload-image");

        HttpEntity requestEntity = MultipartEntityBuilder.create()
                .addBinaryBody("image", imageFile, ContentType.APPLICATION_OCTET_STREAM, imageFile.getName())
                .build();

        httpPost.setEntity(requestEntity);
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy()).build();

        HttpClient httpClient = HttpClients.custom().setSSLContext(sslContext).build();
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
}
