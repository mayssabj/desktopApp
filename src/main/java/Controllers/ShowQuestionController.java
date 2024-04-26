package Controllers;

import entities.Question;
import Services.QuestionCRUD;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;
import java.util.List;

public class ShowQuestionController {

    @FXML
    private ListView<Question> listView;

    ObservableList<Question> questionlist = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        showQuestions();
    }

    @FXML
    private void showQuestions() {
        QuestionCRUD questionCRUD = new QuestionCRUD();
        try {
            List<Question> questions = questionCRUD.afficher();
            questionlist.addAll(questions);
            listView.setItems(questionlist);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
