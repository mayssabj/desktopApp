package Controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import utils.Db;
import java.sql.*;

public class QuestionStatsController {

    @FXML
    private BarChart<String, Number> barChart;

    private Connection connection;

    public QuestionStatsController() {
        this.connection = Db.getInstance().getCnx(); // Assuming Db is your database class
    }

    public void initialize() {
        int totalQuestions = getTotalQuestions();
        int questionsToday = getQuestionsToday();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Total", totalQuestions));
        series.getData().add(new XYChart.Data<>("Today", questionsToday));

        barChart.getData().add(series);
    }

    public void setStylesheet(Scene scene) {
        scene.getStylesheets().add(getClass().getResource("/css/stylestats.css").toExternalForm());
    }

    private int getTotalQuestions() {
        String query = "SELECT COUNT(*) AS total FROM question";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getQuestionsToday() {
        String query = "SELECT COUNT(*) AS total FROM question WHERE createdAt >= DATE_SUB(NOW(), INTERVAL 1 DAY)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
