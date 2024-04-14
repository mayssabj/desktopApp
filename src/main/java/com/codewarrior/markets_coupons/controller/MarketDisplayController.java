package com.codewarrior.markets_coupons.controller;

import com.codewarrior.markets_coupons.model.Market;
import com.codewarrior.markets_coupons.service.MarketDAO;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    void getMarketInfo(MouseEvent event) {
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

            String s = market.toString();
            System.out.println(s);

            // Attempt to load the image from the classpath resources
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            if (image != null) {
                marketImage.setImage(image);
            } else {
                System.err.println("Image file not found: " + imagePath);
            }
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
}
