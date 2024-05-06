package edu.esprit.controller;

import edu.esprit.utils.mydb;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import java.sql.*;
public class QuestionStatsController {
    @FXML
    private BarChart<String, Number> barChart;

    private Connection connection;

    public QuestionStatsController() {
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