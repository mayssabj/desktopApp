package edu.esprit.controller.Reclamation;


import edu.esprit.entities.Avertissement;
import edu.esprit.entities.Reclamation;
import edu.esprit.entities.User;
import edu.esprit.services.ServiceReclamation;
import edu.esprit.services.UserService;
import edu.esprit.utils.OpenAIService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class ReclamationListController {
    @FXML private TilePane tilePane;

    @FXML private TextArea chatArea;
    @FXML private TextField chatInput;
    private ServiceReclamation serviceReclamation;
    private UserService userService = new UserService();
    private OpenAIService openAIService = new OpenAIService();

    @FXML
    public void initialize() {
        serviceReclamation = new ServiceReclamation();
        loadReclamations();
    }
    @FXML
    private ListView<Reclamation> listView;

    private void loadReclamations() {
        ObservableList<Reclamation> list = null;
        try {
            list = serviceReclamation.afficherReclamation();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        listView.setItems(list);
        listView.setCellFactory(param -> new ListCell<Reclamation>() {
            @Override
            protected void updateItem(Reclamation rec, boolean empty) {
                super.updateItem(rec, empty);
                if (empty || rec == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox vbox = new VBox(10);
                    vbox.setStyle("-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: #666; -fx-background-color: #f4f4f4;");
                    Text subject = new Text("Subject: " + rec.getSubjuct());
                    subject.setStyle("-fx-fill: #333; -fx-font-weight: bold;");
                    Text description = new Text("Description: " + rec.getDescription());
                    description.setStyle("-fx-fill: #666;");
                    Text username = new Text("Reported by: " + rec.getReported_username());
                    username.setStyle("-fx-fill: #333;");
                    Text type = new Text("Type: " + rec.getType_reclamation());
                    type.setStyle("-fx-fill: #333;");

                    ImageView imageView = new ImageView();
                    if (rec.getScreenshot() != null && !rec.getScreenshot().isEmpty()) {
                        imageView.setImage(new Image(rec.getScreenshot()));
                        imageView.setFitWidth(100);
                        imageView.setPreserveRatio(true);
                    }
                    imageView.setStyle("-fx-padding: 10;");

                    Button deleteButton = new Button("Delete");
                    deleteButton.setStyle("-fx-background-color: #c00; -fx-text-fill: white;");
                    deleteButton.setOnAction(e -> deleteReclamation(rec));
                    Button modifyButton = new Button("Modify");
                    modifyButton.setStyle("-fx-background-color: #00c; -fx-text-fill: white;");
                    modifyButton.setOnAction(e -> modifyReclamation(rec));

                    HBox buttonBox = new HBox(10, deleteButton, modifyButton);
                    buttonBox.setStyle("-fx-alignment: center;");

                    vbox.getChildren().addAll(subject, description, username, type, imageView, buttonBox);
                    setGraphic(vbox);
                }
            }
        });
    }

    private void deleteReclamation(Reclamation reclamation) {
        // Implement deletion logic
        try {
            serviceReclamation.supprimerReclamation(reclamation.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tilePane.getChildren().clear();
        loadReclamations(); // Refresh the grid
    }

    private void modifyReclamation(Reclamation reclamation) {
        try {
            // Ensure the path starts with a forward slash if it's an absolute path from the classpath root
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/ReclamationEditForm.fxml"));
            Parent root = loader.load();

            ReclamationEditFormController controller = loader.getController();
            controller.initData(reclamation);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modify Reclamation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();  // Properly handle the exception
        }

    }
    @FXML
    private void handleSendMessage() {
        String message = chatInput.getText();
        if (!message.isEmpty()) {
            try {
                String jsonResponse = openAIService.getChatGPTResponse(message);
                JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

                if (jsonObject.has("choices") && jsonObject.getAsJsonArray("choices").size() > 0) {
                    String response = jsonObject.getAsJsonArray("choices").get(0).getAsJsonObject().get("text").getAsString();
                    chatArea.appendText("You: " + message + "\n");
                    chatArea.appendText("Bot: " + response + "\n");
                } else if (jsonObject.has("error")) {
                    String errorMessage = jsonObject.getAsJsonObject("error").get("message").getAsString();
                    chatArea.appendText("Error: " + errorMessage + "\n");
                    // Optionally, provide additional guidance or actions for the user here
                } else {
                    System.err.println("Unexpected JSON format: " + jsonResponse);
                    chatArea.appendText("Received unexpected data from OpenAI\n");
                }
            } catch (IOException e) {
                chatArea.appendText("Failed to get response from OpenAI\n");
                e.printStackTrace();
            } catch (Exception e) {
                chatArea.appendText("Error processing the response: " + e.getMessage() + "\n");
                e.printStackTrace();
            }
            chatInput.clear();
        }
    }
    @FXML
    private ListView<Avertissement> avertissementListView;
    @FXML
    private void validateAvertissement(ActionEvent event) {
        Avertissement selected = avertissementListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                User user = userService.getUserByUsername(selected.getReported_username());
                if (user != null) {
                    userService.incrementAvertissementCount(user.getId());
                    Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "Compteur d'avertissements incrémenté pour " + user.getUsername());
                    infoAlert.showAndWait();
                    // Vous pouvez aussi mettre à jour l'interface ici si nécessaire
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Utilisateur non trouvé.");
                    errorAlert.showAndWait();
                }
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Erreur lors de la validation de l'avertissement.");
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        } else {
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "Veuillez sélectionner un avertissement à valider.");
            infoAlert.showAndWait();
        }
    }



}