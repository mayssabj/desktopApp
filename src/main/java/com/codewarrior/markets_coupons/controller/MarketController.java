package com.codewarrior.markets_coupons.controller;

import com.codewarrior.markets_coupons.service.MarketDAO;
import com.codewarrior.markets_coupons.model.Market;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MarketController {

    private final MarketDAO marketDAO = new MarketDAO();

    @FXML
    private TextField addressField;

    @FXML
    private TextField cityField;

    @FXML
    private Pane display;

    @FXML
    private Pane goBack;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField nameField;

    @FXML
    private TextField regionField;

    @FXML
    private Button uploadImage;

    @FXML
    private TextField zipCodeField;

    @FXML
    private Label addressControl;

    @FXML
    private Label cityControll;

    @FXML
    private Label nameControll;

    @FXML
    private Label regionControll;

    @FXML
    private Label zipControll;

    @FXML
    void uploadImage(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(uploadImage.getScene().getWindow());
        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString();
            Image image = new Image(imagePath);
            imageView.setImage(image);
        }
    }

    @FXML
    void goBack(MouseEvent event) {
        System.out.println("redirect to Home");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/codewarrior/markets_coupons/hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Home Window");
            newStage.show();

            // Close the current stage (window)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void displayRoute(MouseEvent event) {
        System.out.println("marzabba");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/codewarrior/markets_coupons/displayMarket-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Home Window");
            newStage.show();

            // Close the current stage (window)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addMarket(MouseEvent event) throws IOException {

        boolean isValid = true;

        System.out.println("add market");
        String name = nameField.getText();
        String address = addressField.getText();
        String city = cityField.getText();
        String region = regionField.getText();
        int zipCode = 0;
        String zipCodeText = zipCodeField.getText();
        if (zipCodeText.isEmpty()) {
            zipControll.setText("ZIP Code required");
            isValid = false;
        } else {
            try {
                zipCode = Integer.parseInt(zipCodeText);
            } catch (NumberFormatException e) {
                zipControll.setText("Invalid ZIP Code");
                isValid = false;
            }
        }
        String image = null;

        // Check if imageView has an image
        if (imageView.getImage() != null) {
            // Get the file path of the image
            image = imageView.getImage().getUrl();
            // Remove "file:" prefix from the URL
            image = image.substring("file:".length());
        }
        // Validate inputs


        if (name.isEmpty()) {
            nameControll.setText("Name is required");
            isValid = false;
        }

        if (address.isEmpty()) {
            addressControl.setText("Address is required");
            isValid = false;
        }

        if (city.isEmpty()) {
            cityControll.setText("City is required");
            isValid = false;
        }

        if (region.isEmpty()) {
            regionControll.setText("Region is required");
            isValid = false;
        }

        if (zipCode < 1000 || zipCode > 9999) {
            zipControll.setText("Zip Code must be between 1000 and 9999");
            isValid = false;
        }

        if (!isValid) {
            // If any field is invalid, don't proceed
            System.out.println("can't add market");
            return;
        }
        Market newMarket = new Market(name, image, address, city, region, zipCode);
        marketDAO.addMarket(newMarket);
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"messages\":[{\"destinations\":[{\"to\":\"21658450148\"}],\"from\":\"ServiceSMS\",\"text\":\""+newMarket.getName() + newMarket.getAddress() + newMarket.getCity() + newMarket.getRegion() + String.valueOf(newMarket.getZipCode()) + "\"}]}");

        Request request = new Request.Builder()
                .url("https://ggeryw.api.infobip.com/sms/2/text/advanced")
                .post(body)
                .addHeader("Authorization", "App 85a3a918a66e2c017ce57a5678d6b6e7-7b713c47-9ed6-4175-b543-df955333f4ff")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());


    }
}
