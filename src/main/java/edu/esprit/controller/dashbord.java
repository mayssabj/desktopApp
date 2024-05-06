
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
    void showUsers(MouseEvent event) {
        try {
            // Ensure the path starts with a slash indicating it's relative to the classpath root
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/showUsersAdmin.fxml"));
            Node eventFXML = loader.load();

            // Assuming vboxdash is already defined and part of your scene
            vboxdash.getChildren().clear();
            vboxdash.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();  // Log the exception to understand what went wrong
        }
    }
    @FXML
    void ajoutsponsor(MouseEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterSponsoring.fxml"));
            Node eventFXML = loader.load();

            // Clear existing content from vboxdash
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
            e.printStackTrace();  // Log the exception to understand what went wrong
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
    void showpost(MouseEvent event) {
        try {
            // Load showPostAdminController.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/showPostAdminController.fxml"));
            Node eventFXML = loader.load();

            // Clear existing content from vboxdash
            vboxdash.getChildren().clear();

            // Add the loaded eventFXML to vboxdash
            vboxdash.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();  // Handle IOException
        }
    }

     @FXML
    private void showAvertissementPage(MouseEvent event) {
        try {
            // Charger la page ListAvertissement.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Avertissement/ListAvertissement.fxml"));
            Node avertissementPage = loader.load();

            // Effacer le contenu actuel de vboxdash et ajouter la page ListAvertissement.fxml
            vboxdash.getChildren().clear();
            vboxdash.getChildren().add(avertissementPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
