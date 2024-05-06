package edu.esprit.controller;

import edu.esprit.services.StatisticsService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class dashbord {

    @FXML
    private VBox vboxdash;

    @FXML
    private BarChart<String, Integer> statisticsChart;

    private StatisticsService statisticsService = new StatisticsService();

    @FXML
    void initialize() {
        displayStatistics();
    }

    @FXML
    void showsponsor(MouseEvent event) {
        loadFXML("/showsponsoring.fxml");
    }

    @FXML
    void showspostgroup(MouseEvent event) {
        loadFXML("/showpostgroup.fxml");
    }
    @FXML
    void ajoutsponsor(MouseEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterSponsoring.fxml"));
            Node eventFXML = loader.load();

            // Clear existing content from vboxdash
            vboxdash.getChildren().clear();

            // Add the loaded eventFXML to vboxdash
            vboxdash.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();  // Handle IOException (e.g., file not found or invalid FXML)
        }
    }

    @FXML
    void showcomment(MouseEvent event) {
        loadFXML("/showcommentairegroup.fxml");
    }

    private void loadFXML(String resource) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
            Node eventFXML = loader.load();
            vboxdash.getChildren().clear();
            vboxdash.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void displayStatistics() {
        Map<String, Integer> statistics = statisticsService.calculateActiveDesactiveStatistics(); // Utilisation de statisticsService
        int activeCount = statistics.get("ACTIVE");
        int desactiveCount = statistics.get("DESACTIVE");


        // Clear any existing data from the chart
        statisticsChart.getData().clear();

        // Define the data series for lost and found items
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("Active et Desactive");

        // Populate the data series with the calculated statistics
        series.getData().add(new XYChart.Data<>("ACTIVE", activeCount));
        series.getData().add(new XYChart.Data<>("DESACTIVE", desactiveCount));

        // Add the data series to the chart
        statisticsChart.getData().add(series);
    }

    @FXML
    void showcategory(MouseEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/displayCategory-view.fxml"));
            Node eventFXML = loader.load();

            // Clear existing content from vboxdash
            vboxdash.getChildren().clear();

            // Add the loaded eventFXML to vboxdash
            vboxdash.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();  // Handle IOException (e.g., file not found or invalid FXML)
        }
    }

    @FXML
    void showmarket(MouseEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/displayMarket-view.fxml"));
            Node eventFXML = loader.load();

            // Clear existing content from vboxdash
            vboxdash.getChildren().clear();

            // Add the loaded eventFXML to vboxdash
            vboxdash.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();  // Handle IOException (e.g., file not found or invalid FXML)
        }
    }

    @FXML
    void showvoucher(MouseEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/displayVoucher-view.fxml"));
            Node eventFXML = loader.load();

            // Clear existing content from vboxdash
            vboxdash.getChildren().clear();

            // Add the loaded eventFXML to vboxdash
            vboxdash.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();  // Handle IOException (e.g., file not found or invalid FXML)
        }
    }
}
