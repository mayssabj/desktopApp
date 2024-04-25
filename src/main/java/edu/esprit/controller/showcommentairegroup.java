package edu.esprit.controller;

import edu.esprit.entities.Post_group;
import edu.esprit.entities.Postcommentaire;
import edu.esprit.services.CommentaireService;
import edu.esprit.services.PostgroupService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class showcommentairegroup implements Initializable {
    @FXML
    private ListView<Postcommentaire> commentairegroupListView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCommentairegroups();
    }
    private void loadCommentairegroups() {
        CommentaireService service = new CommentaireService();
        try {
            ObservableList<Postcommentaire> data = FXCollections.observableArrayList(service.afficherCommentaire());
            commentairegroupListView.setItems(data);
            commentairegroupListView.setCellFactory(param -> new ListCell<Postcommentaire>() {
                @Override
                protected void updateItem(Postcommentaire item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        HBox hbox = new HBox(10);
                        hbox.setStyle("-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 0 0 1 0; -fx-border-color: #808080;");

                        VBox vbox = new VBox(5);
                        Text commnetaireText = new Text("Commentaire: " + item.getCommentaire());
                        Text postgroup_idText = new Text("Post group ID: " + (item.getPostgroup_id() != null ? item.getPostgroup_id() .getId() : "None"));
                        Text user_idText = new Text("User ID: " + (item.getUser_id() != null ? item.getUser_id().getId() : "None"));
                        Text likesText = new Text("likes: " + item.getLikes());
                        Text likedbyText = new Text("likedby: " + item.getLikedByUsers());


                        Button deleteButton = new Button("Delete");
                        deleteButton.setOnAction(event -> deleteCommentaireGroup(item));
                        deleteButton.getStyleClass().add("button-delete");

                        vbox.getChildren().addAll(commnetaireText, postgroup_idText, user_idText,likesText,likedbyText);
                        hbox.getChildren().addAll(vbox, deleteButton);
                        setGraphic(hbox);
                    }
                }
            });
        } catch (SQLException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Error loading Commentaire groups: " + e.getMessage());
            errorAlert.showAndWait();
        }
    }

    private void deleteCommentaireGroup(Postcommentaire commentaire) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this post group?", ButtonType.YES, ButtonType.NO);
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    CommentaireService service = new CommentaireService();
                    service.supprimerCommentaire(commentaire.getId());
                    loadCommentairegroups(); // Refresh list
                } catch (SQLException e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Error deleting post group: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
        });
    }
}