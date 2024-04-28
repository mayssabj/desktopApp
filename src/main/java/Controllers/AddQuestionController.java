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

            Question question = new Question(0, 1, "", titre, body, LocalDateTime.now(), null);
            QuestionCRUD questionCRUD = new QuestionCRUD();
            questionCRUD.ajouterQuestion(question); // changed from 'ajouter' to 'ajouterQuestion'
        }
    }

    private Question selectedQuestion;

    public void setSelectedQuestion(Question selectedQuestion) {
        this.selectedQuestion = selectedQuestion;
    }


    @FXML
    public void updateQuestion(ActionEvent event) {
        if (isInputValid() && selectedQuestion != null) {
            String titre = titreField.getText();
            String body = descriptionArea.getText();

            selectedQuestion.setTitle(titre);
            selectedQuestion.setBody(body);

            QuestionCRUD questionCRUD = new QuestionCRUD();
            try {
                questionCRUD.modifier(selectedQuestion);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    boolean isInputValid() {
        boolean isValid = true;

        if (titreField.getText().isEmpty()) {
            errorTitre.setText("Enter a valid title for your question!");
            isValid = false;
        } else {
            errorTitre.setText("");
        }

        if (descriptionArea.getText().isEmpty()) {
            errorDescription.setText("Enter a valid body for your question!");
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
        }
    }
}
