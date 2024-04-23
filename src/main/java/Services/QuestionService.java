package Services;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Duration;
import entities.Question;
import entities.Answer;
import utils.Db;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.geometry.Insets;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
public class QuestionService implements Initializable {

    @FXML
    private TextField Qbody;

    @FXML
    private TextField Qtitle;

    @FXML
    private Button btnask;

    @FXML
    private Button btndelete;

    @FXML
    private Button btnupdate;

    @FXML
    private ListView<Question> list;

    private Db db;
    private ObservableList<Question> questions;

    private Timeline timeline;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        db = Db.getInstance();
        loadQuestions();

        list.setItems(questions);

        timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            loadQuestions();
            list.setItems(questions);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        list.setCellFactory(param -> new ListCell<Question>() {
            @Override
            protected void updateItem(Question item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Set text format for each cell
                    setText("Title: " + item.getTitle() + "\n\n" + "Body: " + item.getBody());

                    Button deleteButton = new Button("Delete");
                    deleteButton.setOnAction(event -> deleteQuestion(item));

                    Button updateButton = new Button("Update");
                    updateButton.setOnAction(event -> updateQuestion(item));
                    setGraphic(new HBox(5, deleteButton, updateButton));
                }
            }
        });


        list.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                Question selectedQuestion = list.getSelectionModel().getSelectedItem();
                if (selectedQuestion != null) {
                    showAnswer(selectedQuestion);
                }
            }
        });
    }

    private void showAnswer(Question question) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Answer");
        dialog.setHeaderText("Answer for question: " + question.getTitle());

        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);

        Label answerLabel = new Label("Answer:");
        TextArea answerTextArea = new TextArea();
        answerTextArea.setText(question.getAnswer() != null ? question.getAnswer().getBody() : "No answer yet");
        answerTextArea.setEditable(false);
        answerTextArea.setWrapText(true);
        answerTextArea.setMaxWidth(Double.MAX_VALUE);
        answerTextArea.setMaxHeight(Double.MAX_VALUE);

        GridPane.setVgrow(answerTextArea, Priority.ALWAYS);
        GridPane.setHgrow(answerTextArea, Priority.ALWAYS);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(answerLabel, 0, 0);
        grid.add(answerTextArea, 0, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait();
    }

    private void loadQuestions() {
        questions = FXCollections.observableArrayList();
        try (Connection connection = db.getCnx();
             PreparedStatement statement = connection.prepareStatement("SELECT q.id, q.user_id, q.title, q.body, q.created_at, q.answer_id, a.body AS answer_body FROM question q LEFT JOIN answer a ON q.answer_id = a.id");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int userId = resultSet.getInt("user_id");
                String title = resultSet.getString("title");
                String body = resultSet.getString("body");
                LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                int answerId = resultSet.getInt("answer_id");
                String answerBody = resultSet.getString("answer_body");

                Answer answer = new Answer(answerId, userId, id, answerBody, createdAt);
                questions.add(new Question(id, userId, title, body, createdAt, answer));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteQuestion(Question question) {
        try (Connection cnx = db.getCnx();
             PreparedStatement pstmt = cnx.prepareStatement("DELETE FROM question WHERE id = ?")) {
            pstmt.setInt(1, question.getId());
            pstmt.executeUpdate();
            loadQuestions(); // Reload the questions after deleting one
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateQuestion(Question question) {
        String newTitle = Qtitle.getText();
        String newBody = Qbody.getText();
        if (!newTitle.isEmpty() && !newBody.isEmpty()) {
            try (Connection cnx = db.getCnx();
                 PreparedStatement pstmt = cnx.prepareStatement("UPDATE question SET title = ?, body = ? WHERE id = ?")) {
                pstmt.setString(1, newTitle);
                pstmt.setString(2, newBody);
                pstmt.setInt(3, question.getId());
                pstmt.executeUpdate();
                loadQuestions(); // Reload the questions after updating one
                Qtitle.clear();
                Qbody.clear();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void addQuestion() {
        String title = Qtitle.getText();
        String body = Qbody.getText();
        if (title.isEmpty() || body.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Title and body must not be empty.");
            alert.showAndWait();
            return;
        }

        LocalDateTime createdAt = LocalDateTime.now();
        Question question = new Question(0, 1, title, body, createdAt,null); // Provide a valid userId

        try (Connection connection = db.getCnx();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO question (user_id, title, body, created_at) VALUES (?, ?, ?, ?)")) {
            statement.setInt(1, question.getUserId());
            statement.setString(2, question.getTitle());
            statement.setString(3, question.getBody());
            statement.setTimestamp(4, Timestamp.valueOf(question.getCreatedAt()));
            statement.executeUpdate();
            Qtitle.clear();
            Qbody.clear();
            loadQuestions(); // Reload the questions after adding one
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while adding the question.");
            alert.showAndWait();
        }
    }
}
