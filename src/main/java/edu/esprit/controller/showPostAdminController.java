package edu.esprit.controller;

import java.sql.SQLException;
import java.util.List;

import edu.esprit.entities.Post;
import edu.esprit.entities.User;
import edu.esprit.services.PostCRUD;
import edu.esprit.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class showPostAdminController {

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
        int postIndex = 0;
        anchorPane.getChildren().clear();
        List<Post> postList = loadDataFromDatabase();

        for (Post post : postList) {
            HBox postEntry = createPostEntry(post);
            anchorPane.getChildren().add(postEntry);
            AnchorPane.setTopAnchor(postEntry, 50.0 + (postIndex * 50.0)); // Adjust vertical position as needed
            postIndex++;
        }
    }

    private HBox createPostEntry(Post post) {
        HBox hbox = new HBox();
        hbox.setSpacing(5);
        hbox.setPrefWidth(1000);

        Label titleLabel = new Label("Titre : "+post.getTitre());
        titleLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: #6b9eef;"); // Add text color styling
        hbox.getChildren().add(titleLabel);

        Label dateLabel = new Label("Date : " + post.getDate().toString());
        dateLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: #6b9eef;");
        hbox.getChildren().add(dateLabel);

        Label LocalLabel = new Label("Titre : "+post.getPlace());
        LocalLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: #6b9eef;"); // Add text color styling
        hbox.getChildren().add(LocalLabel);

        Label sizeLabel = new Label("Type: " + post.getType());
        sizeLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; " + "-fx-font-weight: bold;"); // Add bold style

        if ("LOST".equals(post.getType())) {
            sizeLabel.setStyle(sizeLabel.getStyle() + "-fx-text-fill: red;");
        } else {
            sizeLabel.setStyle(sizeLabel.getStyle() + "-fx-text-fill: green;");
        }

        hbox.getChildren().add(sizeLabel);

        Label deslabel = new Label("Description : " + post.getDescription().toString());
        deslabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px;");
        hbox.getChildren().add(deslabel);

        int id = post.getuser_id();
        UserService userService=new UserService();
        User user= userService.getUserById(id);

        Label nameuser = new Label("User : " + user.getEmail().toString());
        nameuser.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px;");
        hbox.getChildren().add(nameuser);

        ImageView pinImageView = new ImageView(new Image(getClass().getResourceAsStream("/images/icons8_pin_50px.png")));
        pinImageView.setFitWidth(18);
        pinImageView.setFitHeight(22);
        hbox.getChildren().add(pinImageView);

        ImageView ellipsisImageView = new ImageView(new Image(getClass().getResourceAsStream("/images/icons8_ellipsis_50px.png")));
        ellipsisImageView.setFitWidth(18);
        ellipsisImageView.setFitHeight(22);
        hbox.getChildren().add(ellipsisImageView);

        hbox.setStyle("-fx-background-color: #FEFFFD; -fx-background-radius: 10px; -fx-padding: 10px; -fx-spacing: 10px;"); // Add background color and padding

        return hbox;
    }

    private List<Post> loadDataFromDatabase() throws SQLException {
        PostCRUD postCRUD = new PostCRUD();
        return postCRUD.afficher();
    }
}
