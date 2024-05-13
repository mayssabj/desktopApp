package edu.esprit.controller;

import edu.esprit.entities.Question;
import edu.esprit.services.QuestionCRUD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Questionsadmin {
    @FXML
    private Pagination pagination;
    private final int rowsPerPage = 3; // Change as needed
    private ObservableList<Question> questionlist = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        pagination.setPageFactory(new Callback<Integer, javafx.scene.Node>() {
            @Override
            public javafx.scene.Node call(Integer pageIndex) {
                int fromIndex = pageIndex * rowsPerPage;
                int toIndex = Math.min(fromIndex + rowsPerPage, questionlist.size());
                ListView<Question> listView = createListView(questionlist.subList(fromIndex, toIndex));
                return new BorderPane(listView);
            }
        });

        // Show the questions in the list view
        showQuestions();
    }

    private ListView<Question> createListView(List<Question> sublist) {
        ListView<Question> listView = new ListView<>();
        listView.setItems(FXCollections.observableArrayList(sublist));
        listView.setCellFactory(param -> new ListCell<Question>() {
            @Override
            protected void updateItem(Question item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create the delete and update buttons with icons
                    Image deleteImage = new Image(getClass().getResource("/imgs/icons/delete-icon.png").toExternalForm());
                    ImageView deleteImageView = new ImageView(deleteImage);
                    deleteImageView.setFitHeight(20); // Set the height
                    deleteImageView.setFitWidth(20); // Set the width
                    Button deleteButton = new Button("Delete", deleteImageView);
                    deleteButton.setStyle("-fx-text-fill: red;"); // This will make the text red
                    deleteButton.setAlignment(Pos.CENTER_RIGHT);

                    Image answerImage = new Image(getClass().getResource("/imgs/icons/answer.png").toExternalForm());
                    ImageView answerImageView = new ImageView(answerImage);
                    answerImageView.setFitHeight(20); // Set the height
                    answerImageView.setFitWidth(20); // Set the width
                    Button answerButton = new Button("Answer");
                    answerButton.setAlignment(Pos.CENTER_RIGHT);
                    answerButton.setStyle("-fx-text-fill: #0800ff;"); // This will make the text blue

                    // Set the actions for the buttons
                    deleteButton.setUserData(item);
                    deleteButton.setOnAction(event -> {
                        // Get the question associated with the clicked delete button
                        Question selectedQuestion = (Question) ((Button) event.getSource()).getUserData();

                        if (selectedQuestion != null) {
                            QuestionCRUD questionCRUD = new QuestionCRUD();
                            try {
                                // Delete the selected question from the database
                                questionCRUD.supprimer(selectedQuestion.getId());
                                // Remove the selected question from the list view
                                listView.getItems().remove(selectedQuestion);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    answerButton.setUserData(item); // item is the current question
                    answerButton.setOnAction(event -> {
                        // Get the question associated with the clicked answer button
                        Question selectedQuestion = (Question) ((Button) event.getSource()).getUserData();

                        if (selectedQuestion != null) {
                            try {
                                // Load the fxml file
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/addAnswer.fxml"));
                                Parent parent = fxmlLoader.load();

                                // Get the controller of the scene
                                AddAnswerController controller = fxmlLoader.getController();

                                // Set the selected question's ID to the controller
                                controller.setQuestionId(selectedQuestion.getId());

                                // Create a new stage
                                Stage stage = new Stage();
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.setScene(new Scene(parent, 600, 600)); // Set the size of the dialog to 600x600

                                // Show the dialog and wait
                                stage.showAndWait();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // Show an error message if no question is selected
                            System.out.println("No question selected to answer.");
                        }
                    });

                    Label label = new Label(item.toString());
                    HBox.setHgrow(label, Priority.ALWAYS); // This will make the label only take up the necessary space

                    // Create a horizontal box to hold the buttons
                    HBox buttonBox = new HBox(deleteButton, answerButton);
                    buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
                    buttonBox.setSpacing(20); // This will add 20px of space between the buttons
                    HBox.setMargin(buttonBox, new Insets(0, 10, 0, 0)); // Add some margin to the right of the buttons

                    // Create a border pane to hold the label on the left and the buttons on the bottom right
                    BorderPane borderPane = new BorderPane();
                    borderPane.setLeft(label);
                    borderPane.setBottom(buttonBox);

                    setGraphic(borderPane);
                }
            }
        });
        return listView;
    }

    @FXML
    private void showQuestions() {
        // Create a new QuestionCRUD object
        QuestionCRUD questionCRUD = new QuestionCRUD();
        try {
            // Get the list of questions
            List<Question> questions = questionCRUD.afficher();
            // Add the questions to the observable list
            questionlist.addAll(questions);
            // Update the page count for the pagination control
            pagination.setPageCount((questionlist.size() / rowsPerPage + 1));
        } catch (SQLException e) {
            // Print the stack trace for the SQLException
            e.printStackTrace();
        }
    }
}