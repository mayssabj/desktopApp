package edu.esprit.controller;

import edu.esprit.entities.Question;
import edu.esprit.entities.User;
import edu.esprit.services.QuestionCRUD;
import edu.esprit.utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class ShowQuestionController {

    @FXML
    private ListView<Question> listView;
    @FXML
    private Button btn_ask;
    @FXML
    private Button btn_stats;


    // Observable list to hold the questions
    ObservableList<Question> questionlist = FXCollections.observableArrayList();

        @FXML
        private void initialize() {
            User user = Session.getInstance().getCurrentUser();
            // Set the cell factory for the list view
            listView.setCellFactory(param -> new ListCell<Question>() {
                @Override
                protected void updateItem(Question item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        if (item.getUserId() == user.getId()) {
                            // Create the delete and update buttons with icons
                            Image deleteImage = new Image(getClass().getResource("/imgs/icons/delete-icon.png").toExternalForm());
                            ImageView deleteImageView = new ImageView(deleteImage);
                            deleteImageView.setFitHeight(20); // Set the height
                            deleteImageView.setFitWidth(20); // Set the width
                            Button deleteButton = new Button("Delete", deleteImageView);
                            deleteButton.setStyle("-fx-text-fill: red;"); // This will make the text red

                            Image updateImage = new Image(getClass().getResource("/imgs/icons/edit-icon.png").toExternalForm());
                            ImageView updateImageView = new ImageView(updateImage);
                            updateImageView.setFitHeight(20); // Set the height
                            updateImageView.setFitWidth(20); // Set the width
                            Button updateButton = new Button("Update", updateImageView);
                            updateButton.setStyle("-fx-text-fill: #1ED760;");


                            deleteButton.setUserData(item);

                            // Set the actions for the buttons
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
                            updateButton.setUserData(item);
                            updateButton.setOnAction(event -> {
                                // Get the question associated with the clicked update button
                                Question selectedQuestion = (Question) ((Button) event.getSource()).getUserData();

                                if (selectedQuestion != null) {
                                    try {
                                        // Load the fxml file
                                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/updatequestion.fxml"));
                                        Parent parent = fxmlLoader.load();

                                        // Get the controller of the scene
                                        AddQuestionController controller = fxmlLoader.getController();

                                        // Set the selected question to the controller
                                        controller.setSelectedQuestion(selectedQuestion);
                                        // Create a new stage
                                        Stage stage = new Stage();
                                        stage.initModality(Modality.APPLICATION_MODAL);
                                        stage.setScene(new Scene(parent, 600, 600)); // Set the size of the dialog to 600x600

                                        // Show the dialog and wait
                                        stage.showAndWait();

                                        // Get the index of the selected question
                                        int index = listView.getItems().indexOf(selectedQuestion);

                                        // Check if the question is still in the list before trying to update it
                                        if (index != -1) {
                                            // Refresh the selected question in the list view
                                            listView.getItems().set(index, selectedQuestion);
                                        }

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    // Show an error message if no question is selected
                                    System.out.println("No question selected for update.");
                                }
                            });

                            // Set buttons visibility
                            deleteButton.setVisible(true);
                            updateButton.setVisible(true);

                            Label label = new Label(item.toString());
                            HBox.setHgrow(label, Priority.ALWAYS); // This will make the label only take up the necessary space

                            // Create a horizontal box to hold the buttons
                            HBox buttonBox = new HBox(updateButton, deleteButton);
                            buttonBox.setSpacing(10); // This will add 10px of space between the buttons
                            buttonBox.setStyle("-fx-alignment: bottom-right; -fx-padding: 0 20 0 0;"); // This will align the buttons to the right and add a padding of 20px to the right

                            // Create a horizontal box to hold the label and button box
                            HBox hBox = new HBox(label, buttonBox);

                            setGraphic(hBox);
                        } else {
                            // Hide the buttons if the current user is not the owner of the question
                            setGraphic(new Label(item.toString()));
                        }
                    }
                }
            });

            // Show the questions in the list view
            showQuestions();
        }
    public void showQuestionStatsDialog() {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/QuestionStats.fxml")); // Replace with the actual path to your FXML file
            Parent root = loader.load();

            // Create a new stage for the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Question Statistics");
            dialogStage.setScene(new Scene(root));

            // Show the dialog
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            // Set the items for the list view
            listView.setItems(questionlist);
        } catch (SQLException e) {
            // Print the stack trace for the SQLException
            e.printStackTrace();
        }
    }
    @FXML
    void handleask(ActionEvent event) {
        try {
            // Load the fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/addquestion.fxml"));
            Parent parent = fxmlLoader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(parent));
            stage.setTitle("Ask");
            stage.setMinWidth(600);
            stage.setMinHeight(600);
            stage.setMaxWidth(600);
            stage.setMaxHeight(600);

            // Show the dialog
            stage.showAndWait();
            listView.refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
