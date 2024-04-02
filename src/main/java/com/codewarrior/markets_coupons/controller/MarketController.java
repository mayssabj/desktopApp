package com.codewarrior.markets_coupons.controller;

import com.codewarrior.markets_coupons.model.MarketDAO;
import com.codewarrior.markets_coupons.model.Market;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MarketController {

    private final MarketDAO marketDAO = new MarketDAO();

    @FXML
    private TextField addressField;

    @FXML
    private TextField cityField;

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
    void addMarket(MouseEvent event) {

        System.out.println("add market");
        String name = nameField.getText();
        String address = addressField.getText();
        String city = cityField.getText();
        String region = regionField.getText();
        String zipCode = zipCodeField.getText();
        String image = imageView.getImage() != null ? imageView.getImage().getUrl() : null;


        Market newMarket = new Market(name, image, address, city, region, zipCode);
        marketDAO.addMarket(newMarket);
    }

}
