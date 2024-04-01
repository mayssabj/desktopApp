package Controller;

import entite.Post;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import services.PostCRUD;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherPostController {
    @FXML
    private GridPane grid;

    @FXML
    private void initialize() {
        grid.getStyleClass().add("grid-pane");
        afficherPosts();
    }

    @FXML
    private void afficherPosts() {
        PostCRUD postCRUD = new PostCRUD();
        try {
            List<Post> posts = postCRUD.afficher();
            int rowIndex = 0;
            int columnIndex = 0;
            // Set the horizontal and vertical gaps between grid cells
            grid.setHgap(20); // Horizontal gap
            grid.setVgap(20); // Vertical gap
            for (Post post : posts) {
                VBox postBox = createPostBox(post);
                grid.add(postBox, columnIndex, rowIndex);

                // Update row and column indices for the next post
                columnIndex++;
                if (columnIndex >= 2) { // Assuming you want 2 posts per row
                    columnIndex = 0;
                    rowIndex++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

    private VBox createPostBox(Post post) {
        VBox postBox = new VBox();
        postBox.getStyleClass().add("chosen-fruit-card");
        postBox.setPrefWidth(300);
        postBox.setPadding(new Insets(10));

        Label labelTitle = new Label(post.getTitre());
        labelTitle.setFont(new Font("Cambria", 24));
        labelTitle.setTextFill(Color.WHITE);


        // Créer un ImageView pour afficher l'image du sponsoring
        ImageView fruitImg = new ImageView();
        fruitImg.setFitHeight(170);
        fruitImg.setFitWidth(285);
        try {
            Image image = new Image(new FileInputStream(post.getImageUrl()));
            fruitImg.setImage(image);
        } catch (FileNotFoundException e) {
            // Handle the exception, e.g., log an error or display a placeholder image
            System.out.println("Image file not found: " + e.getMessage());
        }
// Adjust the height of the image


        Label labelDescription = new Label(post.getDescription());
        labelDescription.setFont(new Font("Cambria", 18));
        labelDescription.setTextFill(Color.WHITE);

        Label labelType = new Label("Type: " + post.getType());
        labelType.setFont(new Font("Cambria", 24));
        labelType.setTextFill(Color.WHITE);

        Label labelPlace = new Label("Place: " + post.getPlace());
        labelPlace.setFont(new Font("Cambria", 24));
        labelPlace.setTextFill(Color.WHITE);

        Button deleteButton = new Button("Supprimer Post");
        deleteButton.getStyleClass().add("add-btn");
        deleteButton.setFont(new Font("System Bold", 18));
        deleteButton.setTextFill(Color.valueOf("#828282"));
        deleteButton.setOnAction(event -> deletePost(post));

        Button modifierButton = new Button("Modifier Post");
        modifierButton.getStyleClass().add("add-btn");
        modifierButton.setFont(new Font("System Bold", 18));
        modifierButton.setTextFill(Color.valueOf("#828282"));
        modifierButton.setOnAction(event -> modifierPost(post));

        postBox.getChildren().addAll(labelTitle, fruitImg, labelDescription, labelType, labelPlace, deleteButton, modifierButton);
        return postBox;
    }


    private void loadPosts() {
        // This method can be used to reload the posts after a post is deleted
        grid.getChildren().clear();
        afficherPosts();
    }

    private void deletePost(Post post) {
        PostCRUD service = new PostCRUD();
        try {
            service.supprimer(post.getId());
            loadPosts(); // Reload the posts after deletion
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

    private void modifierPost(Post post) {
        try {
            // Load the modifierPost.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierPost.fxml"));
            Parent modifierPostParent = loader.load();

            // Get the controller and initialize it with the post to be modified
            ModifierPostController controller = loader.getController();
            controller.initData(post);

            // Set the scene and show the new window
            Scene modifierPostScene = new Scene(modifierPostParent);
            Stage window = new Stage();
            window.setScene(modifierPostScene);
            window.showAndWait(); // Use showAndWait to wait for the window to close

            loadPosts(); // Reload the posts after modification
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }


    @FXML
    private void handleAddPostButton(ActionEvent event) {
        try {
            // Load the addpost.fxml file
            Parent addPostParent = FXMLLoader.load(getClass().getResource("/addpost.fxml"));
            Scene addPostScene = new Scene(addPostParent);

            // Get the stage from the event source and set the new scene
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(addPostScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }
}
