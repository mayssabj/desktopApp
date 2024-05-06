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
import java.util.Optional;
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
                    vbox.setStyle("-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: #666; -fx-background-color: #ffffff;");

                    Text subject = new Text("Subject: " + rec.getSubjuct());
                    subject.setStyle("-fx-fill: #333; -fx-font-weight: bold; -fx-font-size: 16px;");

                    Text description = new Text("Description: " + rec.getDescription());
                    description.setStyle("-fx-fill: #666; -fx-font-weight: bold; -fx-font-size: 14px;");

                    Text username = new Text("Reported by: " + rec.getReported_username());
                    username.setStyle("-fx-fill: #333; -fx-font-weight: bold; -fx-font-size: 14px;");

                    Text type = new Text("Type: " + rec.getType_reclamation());
                    type.setStyle("-fx-fill: #333; -fx-font-weight: bold; -fx-font-size: 14px;");

                    ImageView imageView = new ImageView();
                    if (rec.getScreenshot() != null && !rec.getScreenshot().isEmpty()) {
                        imageView.setImage(new Image(rec.getScreenshot()));
                        imageView.setFitWidth(600); // Adjust width based on your VBox width
                        imageView.setPreserveRatio(true);
                    }
                    imageView.setStyle("-fx-padding: 10;");

                    // Button styles
                    String commonButtonStyle = "-fx-font-size: 14px; -fx-padding: 10 20; -fx-text-fill: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);";
                    Button deleteButton = new Button("Delete");
                    deleteButton.setStyle("-fx-background-color: #c00; " + commonButtonStyle);

                    Button modifyButton = new Button("Modify");
                    modifyButton.setStyle("-fx-background-color: #336699; " + commonButtonStyle); // Blue color

                    deleteButton.setOnAction(e -> deleteReclamation(rec));
                    modifyButton.setOnAction(e -> {
                        Reclamation selectedReclamation = listView.getSelectionModel().getSelectedItem();
                        if (selectedReclamation != null) {
                            Stage currentStage = (Stage) modifyButton.getScene().getWindow();
                            modifyReclamation(selectedReclamation, currentStage);
                        } else {
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
        // Create a confirmation dialog
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Deletion");
        confirmDialog.setHeaderText("Delete Reclamation");
        confirmDialog.setContentText("Are you sure you want to delete this reclamation?");

        // Optional: Customize the dialog buttons if needed
        confirmDialog.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        // Display the dialog and wait for the user's response
        Optional<ButtonType> result = confirmDialog.showAndWait();

        // Check the user's choice
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                serviceReclamation.supprimerReclamation(reclamation.getId()); // Delete the reclamation
                loadReclamations(); // Refresh the ListView by re-loading the reclamations
            } catch (SQLException e) {
                // Handle SQL Exception here
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Deletion Failed");
                errorAlert.setContentText("Failed to delete the reclamation: " + e.getMessage());
                errorAlert.showAndWait();
            }
        }
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
    private void openReclamationForm(ActionEvent event) {
        try {
            // Load the Reclamation form FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/ReclamationForm.fxml"));
            Parent root = loader.load();

            // Create a new Stage for the Reclamation Form
            Stage stage = new Stage();
            stage.setTitle("Reclamation Form");
            stage.setScene(new Scene(root));
            stage.show();

            // Optional: If you want to hide the current window
            // ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception properly
        }
    }

}


