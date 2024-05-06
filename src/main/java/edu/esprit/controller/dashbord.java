
package edu.esprit.controller;

import edu.esprit.entities.Avertissement;
import edu.esprit.services.ServiceAvertissement;
import edu.esprit.services.StatisticsService;
import edu.esprit.utils.mydb;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class dashbord {


    @FXML
    private VBox vboxdash;

    @FXML
    private BarChart<String, Integer> statisticsChart;

    private StatisticsService statisticsService = new StatisticsService();
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private BarChart<String, Number> barChart1;

    private Connection connection;


    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnExport;

    private ServiceAvertissement serviceAvertissement = new ServiceAvertissement();
    @FXML
    void initialize() {

        displayStatistics();


        int totalQuestions = getTotalQuestions();
        int answeredQuestions = getAnsweredQuestions();

        XYChart.Series<String, Number> totalSeries = new XYChart.Series<>();
        XYChart.Data<String, Number> totalData = new XYChart.Data<>("Questions", totalQuestions);
        totalSeries.getData().add(totalData);
        totalSeries.setName("Total number of questions");

        XYChart.Series<String, Number> answeredSeries = new XYChart.Series<>();
        XYChart.Data<String, Number> answeredData = new XYChart.Data<>("Answers", answeredQuestions);
        answeredSeries.getData().add(answeredData);
        answeredSeries.setName("Total number of answers");

        barChart.getData().addAll(totalSeries, answeredSeries);
        loadChart();

    }
    private void loadChart() {
        try {
            List<Avertissement> avertissements = serviceAvertissement.afficherAvertissement();
            Map<String, Long> countByUser = avertissements.stream()
                    .collect(Collectors.groupingBy(Avertissement::getReported_username, Collectors.counting()));

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            countByUser.forEach((username, count) -> {
                series.getData().add(new XYChart.Data<>(username, count));
            });

            barChart1.getData().clear();
            barChart1.getData().add(series);
        } catch (SQLException e) {
            showError("Erreur de chargement des données", e.getMessage());
        }
    }

    @FXML
    void refreshChart(ActionEvent event) {
        loadChart();
    }

    @FXML
    void exportData(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Data");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = fileChooser.showSaveDialog(barChart1.getScene().getWindow());

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("Username,Number of Warnings"); // Header
                for (XYChart.Series<String, Number> series : barChart1.getData()) {
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        writer.println(data.getXValue() + "," + data.getYValue());
                    }
                }
                showInformation("Export Successful", "Data has been exported successfully.");
            } catch (Exception e) {
                showError("Export Error", "Failed to export data: " + e.getMessage());
            }
        }
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInformation(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

     @FXML
    void showQA(MouseEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Questionsadmin.fxml"));
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



    private int getTotalQuestions() {
        String query = "SELECT COUNT(*) AS total FROM question";
        return executeCountQuery(query);
    }

    private int getAnsweredQuestions() {
        String query = "SELECT COUNT(*) AS total FROM answer WHERE QuestionId IS NOT NULL";
        return executeCountQuery(query);
    }

    private int executeCountQuery(String query) {
        try (PreparedStatement pst = mydb.getInstance().getCon().prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
            // Consider logging the error or rethrowing it as a custom exception
        }
        return 0;
    }



}
