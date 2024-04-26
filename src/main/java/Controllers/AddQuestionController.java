package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import entities.Question;
import Services.QuestionCRUD;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class AddQuestionController {

    @FXML
    private TextField titreField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Text errorTitre;

    @FXML
    private Text errorDescription;

    @FXML
    public void ajouterQuestion(ActionEvent event) {
        if (isInputValid()) {
            String titre = titreField.getText();
            String body = descriptionArea.getText();

            Question question = new Question(0, 1, titre, body, LocalDateTime.now(), null);
            QuestionCRUD questionCRUD = new QuestionCRUD();
            try {
                questionCRUD.ajouter(question);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isInputValid() {
        boolean isValid = true;

        if (titreField.getText().isEmpty()) {
            errorTitre.setText("Title is required");
            isValid = false;
        } else {
            errorTitre.setText("");
        }

        if (descriptionArea.getText().isEmpty()) {
            errorDescription.setText("Description is required");
            isValid = false;
        } else {
            errorDescription.setText("");
        }

        return isValid;
    }

    @FXML
    private void returnToQuestionsPage(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }    }
}
