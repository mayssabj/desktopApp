package edu.esprit.controller;

import edu.esprit.services.StatisticsService;
import edu.esprit.utils.mydb;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

public class dashbord {

    @FXML
    private VBox vboxdash;

    @FXML
    private BarChart<String, Integer> statisticsChart;

    private StatisticsService statisticsService = new StatisticsService();


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
    private BarChart<String, Number> barChart;

    private Connection connection;

    public dashbord() {
        this.connection = mydb.getInstance().getCon();
    }
    public void initialize() {
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
        try (PreparedStatement pst = connection.prepareStatement(query)) {
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


