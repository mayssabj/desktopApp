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

import java.io.File;
import java.io.IOException;

public class MarketController{

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
    void addMarket(MouseEvent event) {

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
        System.out.println("take rout to display market");
        displayRoute(event);
    }



}
