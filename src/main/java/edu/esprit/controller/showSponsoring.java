package edu.esprit.controller;

import edu.esprit.entities.Sponsoring;
import edu.esprit.services.SponsoringService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class showSponsoring implements Initializable {

    @FXML
    private ListView<Sponsoring> sponsoringListView;
    @FXML
    private Button buttonajouter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadSponsorings();
    }

    private void loadSponsorings() {
        SponsoringService service = new SponsoringService();
        try {
            ObservableList<Sponsoring> data = FXCollections.observableArrayList(service.afficherSponsoring());
            sponsoringListView.setItems(data);
            sponsoringListView.setCellFactory(param -> new ListCell<Sponsoring>() {
                @Override
                protected void updateItem(Sponsoring item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        HBox hbox = new HBox(10);
                        hbox.setStyle("-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 0 0 1 0; -fx-border-color: #808080;");

                        VBox vbox = new VBox(5);
                        Text nameText = new Text("Name: " + item.getName());
                        Text descriptionText = new Text("Description: " + item.getDescription());
                        Text dateText = new Text("Date: " + item.getDate().toString());
                        Text contratText = new Text("Contract: " + item.getContrat().toString());
                        Text typeText = new Text("Type: " + item.getType().toString());

                        ImageView imageView = createImageView(item.getImage());

                        Button editButton = new Button("Edit");
                        editButton.setOnAction(event -> openEditSponsoringWindow(item));
                        editButton.getStyleClass().add("button-edit");

                        Button deleteButton = new Button("Delete");
                        deleteButton.setOnAction(event -> deleteSponsoring(item));
                        deleteButton.getStyleClass().add("button-delete");

                        vbox.getChildren().addAll(nameText, descriptionText, dateText, contratText, typeText);
                        hbox.getChildren().addAll(imageView, vbox, editButton, deleteButton);
                        setGraphic(hbox);
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ImageView createImageView(String imagePath) {
        ImageView imageView = new ImageView();
        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(imagePath);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);
            } else {
                System.err.println("Image file not found: " + imagePath);
                // Optionally set a default image
                // Image defaultImage = new Image("path/to/default/image.png");
                // imageView.setImage(defaultImage);
            }
        }
        return imageView;
    }

    private void openEditSponsoringWindow(Sponsoring sponsoring) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierSponsoring.fxml"));
            Parent root = loader.load();

            modifierSponsoring controller = loader.getController();
            controller.initData(sponsoring);

            Stage stage = new Stage();
            stage.setTitle("Edit Sponsoring");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadSponsorings(); // Refresh list after modifications
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteSponsoring(Sponsoring sponsoring) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this sponsoring?", ButtonType.YES, ButtonType.NO);
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    SponsoringService service = new SponsoringService();
                    service.supprimerSponsoring(sponsoring.getId());
                    loadSponsorings(); // Refresh list
                } catch (SQLException e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Error deleting sponsoring: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
        });
    }

    @FXML
    private void handlesponsorButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterSponsoring.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) buttonajouter.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
