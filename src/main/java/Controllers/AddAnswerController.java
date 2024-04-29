package Controllers;

import Services.QuestionCRUD;
import entities.Question;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import entities.Answer;
import Services.AnswerCRUD;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class AddAnswerController {

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Text errorDescription;

    @FXML
    private Button buttonDelete;

    private int questionId; // Add this field to store the questionId

    @FXML
    public void initialize() {
        buttonDelete.setVisible(false);
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
    @FXML
    public void ajouterAnswer(ActionEvent event) {
        if (isInputValid()) {
            String body = descriptionArea.getText();

            Answer answer = new Answer(0, 1, questionId, body, LocalDateTime.now()); // Use the questionId field here
            AnswerCRUD answerCRUD = new AnswerCRUD();
            answer = answerCRUD.ajouterAnswer(answer); // Use the returned Answer object with the ID set
            descriptionArea.clear();

            // Now, update the Question to include the new Answer
            QuestionCRUD questionCRUD = new QuestionCRUD();
            try {
                Question question = questionCRUD.getQuestionById(questionId); // Assuming you have a method to get a question by its ID
                if (question != null) {
                    question.setAnswer(answer); // Set the answer of the question
                    questionCRUD.modifier(question); // Update the question in the database
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    private Answer selectedAnswer;

    public void setSelectedAnswer(Answer selectedAnswer) {
        this.selectedAnswer = selectedAnswer;

        if (selectedAnswer != null) {
            buttonDelete.setVisible(true);
            descriptionArea.setText(selectedAnswer.getBody()); // Set the text of the TextArea to the body of the answer
        }
    }

    @FXML
    public void deleteAnswer(ActionEvent event) {
        if (selectedAnswer != null) {
            AnswerCRUD answerCRUD = new AnswerCRUD();
            try {
                answerCRUD.supprimer(selectedAnswer.getId());
                descriptionArea.clear(); // Clear the TextArea after deleting the answer
                buttonDelete.setVisible(false); // Hide the delete button after deleting the answer
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    boolean isInputValid() {
        boolean isValid = true;

        if (descriptionArea.getText().isEmpty()) {
            errorDescription.setText("Enter a valid body for your answer!");
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
