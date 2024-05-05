package edu.esprit.controller;

import java.sql.SQLException;
import java.util.List;

import edu.esprit.entities.Post;
import edu.esprit.entities.Post_group;
import edu.esprit.entities.Sponsoring;
import edu.esprit.entities.User;
import edu.esprit.services.PostCRUD;
import edu.esprit.services.PostgroupService;
import edu.esprit.services.SponsoringService;
import edu.esprit.services.UserService;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class showpostgroup {

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
        List<Post_group> postList = loadDataFromDatabase();

        // Create header row
        HBox headerRow = new HBox();
        headerRow.setSpacing(10);
        headerRow.setPrefWidth(700);
        headerRow.setStyle("-fx-background-color: #6b9eef; -fx-padding: 10px; -fx-spacing: 10px;");

        // Header labels
        Label titleLabel = new Label("Contenu");
        titleLabel.setPrefWidth(170);
        titleLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(titleLabel);

        Label dateLabel = new Label("Date");
        dateLabel.setPrefWidth(170);
        dateLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(dateLabel);


        Label userLabel = new Label("SponsorName");
        userLabel.setPrefWidth(170);
        userLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(userLabel);



        // Add header row to the AnchorPane
        anchorPane.getChildren().add(headerRow);
        AnchorPane.setTopAnchor(headerRow, 50.0); // Adjust vertical position as needed

        for (Post_group post : postList) {
            HBox postEntry = createPostEntry(post);
            anchorPane.getChildren().add(postEntry);
            AnchorPane.setTopAnchor(postEntry, 50.0 + (postIndex * 50.0)); // Adjust vertical position as needed
            postIndex++;
        }
    }

    private HBox createPostEntry(Post_group post) {
        HBox hbox = new HBox();
        hbox.setSpacing(40);
        hbox.setPrefWidth(1000);

        Label titleLabel = new Label( post.getContenu());
        titleLabel.setPrefWidth(150);
        titleLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: #6b9eef;"); // Add text color styling
        hbox.getChildren().add(titleLabel);

        Label dateLabel = new Label( post.getDate().toString());
        dateLabel.setPrefWidth(150);
        dateLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: #6b9eef;");
        hbox.getChildren().add(dateLabel);

        Label localLabel = new Label( post.getSponsoring_id().getName());
        localLabel.setPrefWidth(150);
        localLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: #6b9eef;"); // Add text color styling
        hbox.getChildren().add(localLabel);

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> deletePostGroup(post));
        deleteButton.getStyleClass().add("button-delete");
        hbox.getChildren().add(deleteButton);





        return hbox;
    }

    private List<Post_group> loadDataFromDatabase() throws SQLException {
        PostgroupService postCRUD = new PostgroupService();
        return postCRUD.afficher();
    }
    private void deletePostGroup(Post_group postgroup) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this post group?", ButtonType.YES, ButtonType.NO);
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    PostgroupService service = new PostgroupService();
                    service.supprimer(postgroup.getId());
                    loadDataFromDatabase(); // Refresh list
                } catch (SQLException e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Error deleting post group: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
        });
    }
}