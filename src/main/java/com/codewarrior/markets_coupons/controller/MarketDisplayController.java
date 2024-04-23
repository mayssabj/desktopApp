package com.codewarrior.markets_coupons.controller;

import com.codewarrior.markets_coupons.model.Market;
import com.codewarrior.markets_coupons.service.MarketDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class MarketDisplayController implements Initializable {
    private final MarketDAO marketDAO = new MarketDAO();
    @FXML
    private Pane back;

    @FXML
    private ImageView marketImage;


    @FXML
    private TableColumn<Market, String> colAddress;

    @FXML
    private TableColumn<Market, String> colCity;

    @FXML
    private TableColumn<Market, String> colName;

    @FXML
    private TableColumn<Market, String> colRegion;

    @FXML
    private TableColumn<Market, Integer> colZipCode;


    @FXML
    private TableView<Market> marketTable;

    @FXML
    private Label controlAddress;

    @FXML
    private Label controlCity;

    @FXML
    private Label controllName;

    @FXML
    private Label controllRegion;

    @FXML
    private Label controllZipCode;

    @FXML
    private TextField updateAddress;

    @FXML
    private TextField updateCity;

    @FXML
    private TextField updateName;

    @FXML
    private TextField updateRegion;

    @FXML
    private TextField updateZipCode;

    @FXML
    private Button updateButton;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayMarkets();
    }

    public ObservableList<Market> getMarkets(){
        ObservableList<Market> markets ;
        return markets = marketDAO.getAllMarketsOb();
    }

    public void displayMarkets(){
        ObservableList<Market> list = getMarkets();
        marketTable.setItems(list);
        colName.setCellValueFactory(new PropertyValueFactory<Market,String>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<Market,String>("address"));
        colCity.setCellValueFactory(new PropertyValueFactory<Market,String>("city"));
        colRegion.setCellValueFactory(new PropertyValueFactory<Market,String>("region"));
        colZipCode.setCellValueFactory(new PropertyValueFactory<Market,Integer>("zipCode"));
    }

    @FXML
    Market getMarketInfo(MouseEvent event) {
        Market selectedMarket = marketTable.getSelectionModel().getSelectedItem();
        if (selectedMarket != null) {
            int id = selectedMarket.getId();
            String name = selectedMarket.getName();
            String imagePath = selectedMarket.getImage();
            String address = selectedMarket.getAddress();
            String city = selectedMarket.getCity();
            String region = selectedMarket.getRegion();
            int zipCode = selectedMarket.getZipCode();

            Market market = new Market(id, name, imagePath, address, city, region, zipCode);
            this.updateAddress.setText(market.getAddress());
            this.updateName.setText(market.getName());
            this.updateRegion.setText(market.getRegion());
            this.updateCity.setText(market.getCity());
            String strZipCode = String.valueOf(market.getZipCode());
            this.updateZipCode.setText(strZipCode);

            String s = market.toString();
            System.out.println(s);

            // Create a File object from the image path
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                // Convert the File object to a URI
                URI imageUri = imageFile.toURI();
                // Convert the URI to a string and create an Image object
                Image image = new Image(imageUri.toString());
                // Set the image to your ImageView
                marketImage.setImage(image);
            } else {
                System.err.println("Image file not found: " + imagePath);
            }
        }
        return selectedMarket;
    }

    private Market getSelectedMarket() {
        return marketTable.getSelectionModel().getSelectedItem();
    }

    // Method to display market information and set image
    @FXML
    Market displayMarketInfo() {
        Market selectedMarket = getSelectedMarket();
        if (selectedMarket != null) {
            int id = selectedMarket.getId();
            String name = selectedMarket.getName();
            String imagePath = selectedMarket.getImage();
            String address = selectedMarket.getAddress();
            String city = selectedMarket.getCity();
            String region = selectedMarket.getRegion();
            int zipCode = selectedMarket.getZipCode();

            Market market = new Market(id, name, imagePath, address, city, region, zipCode);
            String s = market.toString();
            System.out.println(s);

            // Create a File object from the image path
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                // Convert the File object to a URI
                URI imageUri = imageFile.toURI();
                // Convert the URI to a string and create an Image object
                Image image = new Image(imageUri.toString());
                // Set the image to your ImageView
                marketImage.setImage(image);
            } else {
                System.err.println("Image file not found: " + imagePath);
            }



            return market;
        }
        return null;
    }

    // Method to delete a market
    @FXML
    void deleteMarket() {
        Market marketFound = getSelectedMarket();
        if (marketFound != null) {
            // Delete the market from the database
            marketDAO.deleteMarket(marketFound.getId());
            System.out.println("Market with id: " + marketFound.getId() + " deleted !");

            // Remove the deleted market from the table view
            marketTable.getItems().remove(marketFound);
        }
    }


    @FXML
    void updateMarket(MouseEvent event) {
        boolean isValid = true;
        Market market = new Market();
        market.setId(displayMarketInfo().getId());
        if(this.updateName.getText().isEmpty()){
            controllName.setText("name is required 1");
            isValid = false;
        }

        if(this.updateAddress.getText().isEmpty()){
            controlAddress.setText("address is required");
            isValid = false;
        }
        if(this.updateCity.getText().isEmpty()){
            controlCity.setText("city is required");
            isValid = false;
        }
        if(this.updateRegion.getText().isEmpty()){
            controllRegion.setText("region is required");
            isValid = false;
        }
        if(this.updateZipCode.getText().isEmpty()){
            controllZipCode.setText("zipCode is required");
            isValid = false;
        }

        if(!isValid){
            System.out.println("Invalid input");
            return;
        }
        market.setName(this.updateName.getText());
        market.setImage(displayMarketInfo().getImage());
        market.setAddress(this.updateAddress.getText());
        market.setCity(this.updateCity.getText());
        market.setRegion(this.updateRegion.getText());
        int zipCode = Integer.parseInt(this.updateZipCode.getText());
        market.setZipCode(zipCode);
        System.out.println("Update Button :> "+market.toString());
        if (market !=null){
            marketDAO.updateMarket(market);
            System.out.println("Market with id: " + market.getId() + " updated !");

            // Refresh the TableView
            displayMarkets();
        }
    }



    @FXML
    void goBack(MouseEvent event) {
        System.out.println("redirect to Home");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/codewarrior/markets_coupons/market-view.fxml"));
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
}
