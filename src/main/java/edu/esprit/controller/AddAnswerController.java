package edu.esprit.controller;

import edu.esprit.entities.Answer;
import edu.esprit.entities.Question;
import edu.esprit.services.AnswerCRUD;
import edu.esprit.services.QuestionCRUD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

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
    private void sendSms(String toPhoneNumber, String messageBody) {
        // Replace these with your actual Twilio Account SID and Auth Token
        String accountSid = "ACc5237c707a293bd3b7de86316f53154d";
        String authToken = "24c4295cc32c11df8bb8ce3f26fcc0c8";

        Twilio.init(accountSid, authToken);

        Message message = Message.creator(
                        new PhoneNumber(toPhoneNumber), // to
                        new PhoneNumber("+12512378308"), // from
                        messageBody)
                .create();
    }
    @FXML
    public void ajouterAnswer(ActionEvent event) {
        if (isInputValid()) {
            String body = descriptionArea.getText();

            Answer answer = new Answer(0, 1, questionId, body, LocalDateTime.now());
            AnswerCRUD answerCRUD = new AnswerCRUD();
            answer = answerCRUD.ajouterAnswer(answer);
            descriptionArea.clear();

            QuestionCRUD questionCRUD = new QuestionCRUD();
            try {
                Question question = questionCRUD.getQuestionById(questionId);
                if (question != null) {
                    question.setAnswer(answer);
                    questionCRUD.modifier(question);

                    // Get the user's phone number
                    String phoneNumber = "+21654916152";

                    // Send an SMS
                    sendSms(phoneNumber, "Hello,an admin answered your question :) \n" +
                            "Your question was: " + question.getBody() + "\nThe answer is : " + answer.getBody());
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
            answerCRUD.supprimer(selectedAnswer.getId());
            descriptionArea.clear(); // Clear the TextArea after deleting the answer
            buttonDelete.setVisible(false); // Hide the delete button after deleting the answer
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
