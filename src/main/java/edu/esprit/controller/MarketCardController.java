package edu.esprit.controller;

import edu.esprit.entities.Market;
import edu.esprit.services.MarketDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MarketCardController implements Initializable {

    private final MarketDAO marketDAO = new MarketDAO();

    @FXML
    private VBox marketContainer;

    @FXML
    private Label marketName;

    @FXML
    private Label marketAddress;

    @FXML
    private Label marketCityRegion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Market> markets = marketDAO.getAllMarkets();
        for (Market market : markets) {
            // Create a new VBox to represent each market card
            VBox marketCard = createMarketCard(market);
            marketContainer.getChildren().add(marketCard);
        }
        marketName.setStyle("-fx-text-fill: green;");
        marketAddress.setStyle("-fx-text-fill: green;");
        marketCityRegion.setStyle("-fx-text-fill: green;");
    }

    private VBox createMarketCard(Market market) {
        // Create labels for market information
        Label nameLabel = new Label(market.getName());
        Label addressLabel = new Label("Address: " + market.getAddress());
        Label cityRegionLabel = new Label("City/Region: " + market.getCity() + ", " + market.getRegion());

        // Create an image view for market image (assuming you have imageURL in Market class)
        ImageView imageView = new ImageView();
        // Set image properties based on market.getImageURL()

        // Create a VBox to hold the market information
        VBox marketCard = new VBox(10); // Set spacing between elements
        marketCard.getChildren().addAll(nameLabel, addressLabel, cityRegionLabel, imageView);

        return marketCard;
    }
}
