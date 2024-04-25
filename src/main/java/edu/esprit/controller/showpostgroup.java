package edu.esprit.controller;

import edu.esprit.entities.Post_group;

import edu.esprit.services.PostgroupService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class showpostgroup implements Initializable {

    @FXML
    private ListView<Post_group> postgroupListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadPostgroups();
    }

    private void loadPostgroups() {
        PostgroupService service = new PostgroupService();
        try {
            ObservableList<Post_group> data = FXCollections.observableArrayList(service.afficher());
            postgroupListView.setItems(data);
            postgroupListView.setCellFactory(param -> new ListCell<Post_group>() {
                @Override
                protected void updateItem(Post_group item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        HBox hbox = new HBox(10);
                        hbox.setStyle("-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 0 0 1 0; -fx-border-color: #808080;");

                        VBox vbox = new VBox(5);
                        Text contenuText = new Text("Contenu: " + item.getContenu());
                        Text dateText = new Text("Date: " + item.getDate().toString());
                        Text sponsoring_idText = new Text("Sponsoring ID: " + (item.getSponsoring_id() != null ? item.getSponsoring_id().getId() : "None"));
                        Text user_idText = new Text("User ID: " + (item.getUser_id() != null ? item.getUser_id().getId() : "None"));

                        Button deleteButton = new Button("Delete");
                        deleteButton.setOnAction(event -> deletePostGroup(item));
                        deleteButton.getStyleClass().add("button-delete");

                        vbox.getChildren().addAll(contenuText, dateText, sponsoring_idText, user_idText);
                        hbox.getChildren().addAll(vbox, deleteButton);
                        setGraphic(hbox);
                    }
                }
            });
        } catch (SQLException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Error loading post groups: " + e.getMessage());
            errorAlert.showAndWait();
        }
    }

    private void deletePostGroup(Post_group postgroup) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this post group?", ButtonType.YES, ButtonType.NO);
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    PostgroupService service = new PostgroupService();
                    service.supprimer(postgroup.getId());
                    loadPostgroups(); // Refresh list
                } catch (SQLException e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Error deleting post group: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
        });
    }
}
