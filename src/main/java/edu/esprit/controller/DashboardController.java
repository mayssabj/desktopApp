package edu.esprit.controller;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import edu.esprit.entities.Post;
import edu.esprit.services.PostCRUD;
import edu.esprit.services.StatisticsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


public class DashboardController {

    @FXML
    private TextField tfSearch;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private VBox vboxdash;

    @FXML
    private BarChart<String, Integer> statisticsChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;


    ObservableList<Post> user_idList = FXCollections.observableArrayList();

    // Inject StatisticsService
    private StatisticsService statisticsService = new StatisticsService();

    @FXML
    private void initialize() throws SQLException {
        // Initialize the view
        fnReloadData();

        // Add a listener to the ComboBox to detect selection changes
        comboBox.setOnAction(event -> {
            try {
                fnReloadData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    @FXML
    private void fnReloadData() throws SQLException {
        user_idList.clear();
        user_idList.addAll(loadDataFromDatabase());
        displayStatistics();
    }

    private List<Post> loadDataFromDatabase() throws SQLException {
        List<Post> data = new ArrayList<>();
        PostCRUD us = new PostCRUD();
        for (int i = 0; i < us.afficher().size(); i++) {
            System.out.println(us.afficher().get(i).toString());
            Post post = us.afficher().get(i);
            data.add(post);
        }
        return data;
    }

    private void displayStatistics() {
        Map<String, Integer> statistics = statisticsService.calculateLostAndFoundStatistics();
        int lostCount = statistics.get("Lost");
        int foundCount = statistics.get("Found");

        // Clear any existing data from the chart
        statisticsChart.getData().clear();

        // Define the data series for lost and found items
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("Lost and Found");

        // Populate the data series with the calculated statistics
        series.getData().add(new XYChart.Data<>("Lost", lostCount));
        series.getData().add(new XYChart.Data<>("Found", foundCount));

        // Add the data series to the chart
        statisticsChart.getData().add(series);
    }
}