package edu.esprit.controller.Statistique;

import edu.esprit.services.ServiceAvertissement;
import edu.esprit.entities.Avertissement;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsController {

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnExport;

    private ServiceAvertissement serviceAvertissement = new ServiceAvertissement();

    @FXML
    private void initialize() {
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

            barChart.getData().clear();
            barChart.getData().add(series);
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
        File file = fileChooser.showSaveDialog(barChart.getScene().getWindow());

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("Username,Number of Warnings"); // Header
                for (XYChart.Series<String, Number> series : barChart.getData()) {
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
}
