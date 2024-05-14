package edu.esprit.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import edu.esprit.entities.*;
import edu.esprit.services.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class showcommentairegroup {

    private static final int POSTS_PER_ROW = 3;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField tfSearch;

    @FXML
    private void initialize() throws SQLException {
        fnReloadData();
        tfSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            for (Node child : anchorPane.getChildren()) {
                if (child instanceof HBox) {
                    HBox postEntry = (HBox) child;
                    // Add your filtering logic here
                    boolean isVisible = postEntry.getChildren().stream()
                            .filter(Label.class::isInstance)
                            .map(Label.class::cast)
                            .anyMatch(label -> label.getText().toLowerCase().contains(newValue.toLowerCase()));
                    postEntry.setVisible(isVisible);
                    postEntry.setManaged(isVisible);
                }
            }
        });
    }

    @FXML
    private void fnReloadData() throws SQLException {
        int postIndex = 1; // Start post index from 1 to accommodate the header row
        anchorPane.getChildren().clear();
        List<Postcommentaire> postList = loadDataFromDatabase();

        // Create header row
        HBox headerRow = new HBox();
        headerRow.setSpacing(5);
        headerRow.setPrefWidth(1000);
        headerRow.setStyle("-fx-background-color: #6b9eef; -fx-padding: 10px; -fx-spacing: 10px;");

        // Header labels
        Label titleLabel = new Label("Commentaire");
        titleLabel.setPrefWidth(170);
        titleLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(titleLabel);

        Label sponLabel = new Label("Post");
        sponLabel.setPrefWidth(170);
        sponLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(sponLabel);


        Label likeLabel = new Label("nblikes");
        likeLabel.setPrefWidth(170);
        likeLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(likeLabel);

        Label userLabel = new Label("user");
        userLabel.setPrefWidth(170);
        userLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(userLabel);








        // Add header row to the AnchorPane
        anchorPane.getChildren().add(headerRow);
        AnchorPane.setTopAnchor(headerRow, 50.0); // Adjust vertical position as needed

        for (Postcommentaire post : postList) {
            HBox postEntry = createPostEntry(post);
            anchorPane.getChildren().add(postEntry);
            AnchorPane.setTopAnchor(postEntry, 50.0 + (postIndex * 50.0)); // Adjust vertical position as needed
            postIndex++;
        }
    }

    private HBox createPostEntry(Postcommentaire post) {
        HBox hbox = new HBox();
        hbox.setSpacing(40);
        hbox.setPrefWidth(1000);

        Label titleLabel = new Label( post.getCommentaire());
        titleLabel.setPrefWidth(150);
        titleLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: #6b9eef;"); // Add text color styling
        hbox.getChildren().add(titleLabel);

        String s= String.valueOf(post.getPostgroup_id().getId());
        Label localLabel = new Label(s);
        localLabel.setPrefWidth(150);
        localLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: #6b9eef;"); // Add text color styling
        hbox.getChildren().add(localLabel);

        String ss= String.valueOf(post.getLikes());
        Label likesLabel = new Label( ss);
        likesLabel.setPrefWidth(150);
        likesLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: #6b9eef;");
        hbox.getChildren().add(likesLabel);


        String sss= String.valueOf(post.getUser_id().getId());
        Label userLabel = new Label( sss);
        userLabel.setPrefWidth(150);
        userLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: #6b9eef;");
        hbox.getChildren().add(userLabel);

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> deleteCommentaireGroup(post));
        deleteButton.getStyleClass().add("button-delete");
        hbox.getChildren().add(deleteButton);











        return hbox;
    }

    private List<Postcommentaire> loadDataFromDatabase() throws SQLException {
        CommentaireService postCRUD = new CommentaireService();
        return postCRUD.afficherCommentaire();
    }
    private void deleteCommentaireGroup(Postcommentaire commentaire) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this post group?", ButtonType.YES, ButtonType.NO);
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    CommentaireService service = new CommentaireService();
                    service.supprimerCommentaire(commentaire.getId());
                    loadDataFromDatabase(); // Refresh list
                } catch (SQLException e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Error deleting post group: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
        });
    }


}