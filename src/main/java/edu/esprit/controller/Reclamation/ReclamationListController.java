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
import javafx.geometry.Rectangle2D;
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
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


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
        listView.setStyle("-fx-padding: 10px 10px 50px 10px;"); // Increase bottom padding
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
                    // Set the background to white
                    vbox.setStyle("-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: #666; -fx-background-color: #ffffff;");

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
                        // Set the image to be as wide as the VBox
                        imageView.setFitWidth(350); // You can adjust this width based on your VBox width
                        imageView.setPreserveRatio(true);
                    }
                    imageView.setStyle("-fx-padding: 10;");

                    Button deleteButton = new Button("Delete");
                    deleteButton.setStyle("-fx-background-color: #c00; -fx-text-fill: white;");
                    deleteButton.setOnAction(e -> deleteReclamation(rec));
                    Button modifyButton = new Button("Modify");
                    modifyButton.setOnAction(e -> {
                        // Assuming listView is your ListView containing Reclamation objects
                        Reclamation selectedReclamation = listView.getSelectionModel().getSelectedItem();
                        if (selectedReclamation != null) {
                            Stage currentStage = (Stage) modifyButton.getScene().getWindow();
                            modifyReclamation(selectedReclamation, currentStage);
                        } else {
                            // Handle case where no reclamation is selected (show an error message or disable the button)
                            System.out.println("No reclamation selected");
                        }
                    });

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

    private void modifyReclamation(Reclamation reclamation, Stage currentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/ReclamationEditForm.fxml"));
            Parent root = loader.load();

            ReclamationEditFormController controller = loader.getController();
            controller.initData(reclamation);

            // Close the current stage (list view)
            currentStage.close();

            // Create a new stage for the edit form
            Stage editStage = new Stage();
            editStage.setScene(new Scene(root));
            editStage.setTitle("Modify Reclamation");
            editStage.show();
        } catch (IOException e) {
            e.printStackTrace();  // Properly handle the exception
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


