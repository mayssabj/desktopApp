package edu.esprit.controller;

import java.sql.SQLException;
import java.util.List;

import edu.esprit.entities.Post;
import edu.esprit.entities.Sponsoring;
import edu.esprit.entities.User;
import edu.esprit.services.PostCRUD;
import edu.esprit.services.SponsoringService;
import edu.esprit.services.UserService;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class showSponsoring {

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
        List<Sponsoring> postList = loadDataFromDatabase();

        // Create header row
        HBox headerRow = new HBox();
        headerRow.setSpacing(5);
        headerRow.setPrefWidth(1000);
        headerRow.setStyle("-fx-background-color: #6b9eef; -fx-padding: 10px; -fx-spacing: 10px;");

        // Header labels
        Label titleLabel = new Label("NAME");
        titleLabel.setPrefWidth(150);
        titleLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(titleLabel);

        Label dateLabel = new Label("Date");
        dateLabel.setPrefWidth(150);
        dateLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(dateLabel);

        Label localLabel = new Label("Description");
        localLabel.setPrefWidth(150);
        localLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(localLabel);

        Label sizeLabel = new Label("Type");
        sizeLabel.setPrefWidth(150);
        sizeLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(sizeLabel); // Add bold style

        Label desLabel = new Label("Contrat");
        desLabel.setPrefWidth(150);
        desLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(desLabel);

        Label userLabel = new Label("image");
        userLabel.setPrefWidth(150);
        userLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(userLabel);

        // Add header row to the AnchorPane
        anchorPane.getChildren().add(headerRow);
        AnchorPane.setTopAnchor(headerRow, 50.0); // Adjust vertical position as needed

        for (Sponsoring post : postList) {
            HBox postEntry = createPostEntry(post);
            anchorPane.getChildren().add(postEntry);
            AnchorPane.setTopAnchor(postEntry, 50.0 + (postIndex * 50.0)); // Adjust vertical position as needed
            postIndex++;
        }
    }

    private HBox createPostEntry(Sponsoring post) {
        HBox hbox = new HBox();
        hbox.setSpacing(40);
        hbox.setPrefWidth(1000);

        Label titleLabel = new Label( post.getName());
        titleLabel.setPrefWidth(200);
        titleLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: #6b9eef;"); // Add text color styling
        hbox.getChildren().add(titleLabel);

        Label dateLabel = new Label( post.getDate().toString());
        dateLabel.setPrefWidth(200);
        dateLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: #6b9eef;");
        hbox.getChildren().add(dateLabel);

        Label localLabel = new Label( post.getDescription());
        localLabel.setPrefWidth(200);
        localLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: #6b9eef;"); // Add text color styling
        hbox.getChildren().add(localLabel);

        Label sizeLabel = new Label( post.getType().toString());
        sizeLabel.setPrefWidth(200);
        sizeLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; " + "-fx-font-weight: bold;"); // Add bold style

        if ("DESACTIVE".equals(post.getType().toString())) {
            sizeLabel.setStyle(sizeLabel.getStyle() + "-fx-text-fill: red;");
        } else {
            sizeLabel.setStyle(sizeLabel.getStyle() + "-fx-text-fill: green;");
        }

        hbox.getChildren().add(sizeLabel);

        Label desLabel = new Label( post.getContrat().toString());
        desLabel.setPrefWidth(200);
        desLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px;");
        hbox.getChildren().add(desLabel);



        ImageView imageView = new ImageView(new Image("http://localhost:8000/uploads/" + post.getImage()));
        imageView.setFitWidth(40); // Définit la largeur de l'image
        imageView.setPreserveRatio(true); // Garde le ratio de l'image
        HBox.setMargin(imageView, new Insets(7)); // Marges autour de l'image
// Ajoute l'image à la HBox
        hbox.getChildren().add(imageView);

// Applique le style à la HBox
        hbox.setStyle("-fx-background-color: #FEFFFD; -fx-background-radius: 10px; -fx-padding: 10px; -fx-spacing: 10px;");
        // Add background color and padding

        return hbox;
    }

    private List<Sponsoring> loadDataFromDatabase() throws SQLException {
        SponsoringService postCRUD = new SponsoringService();
        return postCRUD.afficherSponsoring();
    }
}