package edu.esprit.controller;

import edu.esprit.entities.User;
import edu.esprit.services.UserService;
import edu.esprit.utils.Session;
import javafx.scene.control.TextInputDialog;
import edu.esprit.entities.Comment;
import edu.esprit.entities.Post;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import edu.esprit.services.CommentCRUD;
import edu.esprit.services.PostCRUD;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
            grid.setHgap(20);
            grid.setVgap(20);
            for (Post post : posts) {
                VBox postBox = createPostBox(post);
                grid.add(postBox, columnIndex, rowIndex);

                columnIndex++;
                if (columnIndex >= 2) {
                    columnIndex = 0;
                    rowIndex++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private VBox createPostBox(Post post) throws FileNotFoundException {
        VBox postBox = new VBox();
        postBox.getStyleClass().add("chosen-fruit-card");
        postBox.setPrefWidth(300);
        postBox.setPadding(new Insets(10));

        int id=post.getUser();
        UserService userService=new UserService();
        User u=userService.getUserById(id);

        HBox namephotoBox = new HBox();
        namephotoBox.setSpacing(10);
        namephotoBox.setAlignment(Pos.CENTER_LEFT);

        String imagePath = u.getPhoto();
        ImageView userPhoto = new ImageView(new Image(new FileInputStream(imagePath)));
        userPhoto.setFitWidth(30);
        userPhoto.setFitHeight(30);

        Label labelProfilename = new Label(u.getEmail());
        labelProfilename.setFont(new Font("Cambria", 18));
        labelProfilename.setTextFill(Color.web("#333333"));


        Region spring1 = new Region();
        HBox.setHgrow(spring1, Priority.ALWAYS);

        namephotoBox.getChildren().addAll(userPhoto, spring1, labelProfilename);


        HBox titleDateBox = new HBox();
        titleDateBox.setSpacing(10);
        titleDateBox.setAlignment(Pos.CENTER_LEFT);

        Label labelTitle = new Label(post.getTitre());
        labelTitle.setFont(new Font("Cambria", 24));
        labelTitle.setTextFill(Color.web("#333333"));

        Label labelDate = new Label(post.getDate().toString());
        labelDate.setFont(new Font("Arial", 14));
        labelDate.setTextFill(Color.web("#333333"));

        Region spring = new Region();
        HBox.setHgrow(spring, Priority.ALWAYS);

        titleDateBox.getChildren().addAll(labelTitle, spring, labelDate);


        ImageView fruitImg = new ImageView();
        fruitImg.setFitHeight(170);
        fruitImg.setFitWidth(285);
        try {
            Image image = new Image(new FileInputStream(post.getImageUrl()));
            fruitImg.setImage(image);
        } catch (FileNotFoundException e) {
            System.out.println("Image file not found: " + e.getMessage());
        }


        Label labelType = new Label(" " + post.getType());
        labelType.setFont(Font.font("Cambria", FontWeight.BOLD, 17));
        if ("LOST".equals(post.getType().toString())) {
            labelType.setTextFill(Color.web("#FF0000"));
        } else if ("FOUND".equals(post.getType().toString())) {
            labelType.setTextFill(Color.web("#008000"));
        } else {
            labelType.setTextFill(Color.web("#333333"));
        }

        Label labelDescription = new Label(post.getDescription());
        labelDescription.setFont(new Font("Arial", 20));
        labelDescription.setTextFill(Color.web("#333333"));

        Label labelPlace = new Label("Place: " + post.getPlace());
        labelPlace.setFont(new Font("Cambria", 17));
        labelPlace.setTextFill(Color.web("#333333"));

        Button commentButton = new Button();
        ImageView commentIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/comment.png")));
        commentIcon.setFitWidth(16);
        commentIcon.setFitHeight(16);
        commentButton.setGraphic(commentIcon);
        commentButton.getStyleClass().add("add-btn");
        commentButton.setFont(new Font("System Bold", 18));
        commentButton.setTextFill(Color.valueOf("#828282"));

        try {
            String commentCount = new CommentCRUD().countComment(post); // Call your countComment method here
            commentButton.setText("Comments (" + commentCount + ")");
        } catch (SQLException e) {
            // Handle SQLException
        }

        commentButton.setOnAction(event -> handlePostClick(post));



        Button deleteButton = new Button();
        ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/delete-icon.png")));
        deleteIcon.setFitWidth(16);
        deleteIcon.setFitHeight(16);
        deleteButton.setGraphic(deleteIcon);
        deleteButton.getStyleClass().add("add-btn");
        deleteButton.setFont(new Font("System Bold", 18));
        deleteButton.setTextFill(Color.valueOf("#828282"));
        if (Session.getInstance().getCurrentUser().getId()==u.getId()){
            deleteButton.setOnAction(event -> deletePost(post));
        }

        Button modifierButton = new Button();
        ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/edit-icon.png")));
        editIcon.setFitWidth(16);
        editIcon.setFitHeight(16);
        modifierButton.setGraphic(editIcon);
        modifierButton.getStyleClass().add("add-btn");
        modifierButton.setFont(new Font("System Bold", 18));
        modifierButton.setTextFill(Color.valueOf("#828282"));
        if (Session.getInstance().getCurrentUser().getId()==u.getId()) {
            modifierButton.setOnAction(event -> modifierPost(post));
        }


        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(commentButton, deleteButton, modifierButton );


        postBox.getChildren().addAll(userPhoto,labelProfilename,titleDateBox, fruitImg, labelType, labelDescription, labelPlace, buttonBox);
        postBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 1) {
                    if (post != null) {
                        commentOnPost(post);
                    } else {
                        System.out.println("Post object is null.");
                    }
                }
            }
        });
        return postBox;
    }



    private void loadPosts() {
        grid.getChildren().clear();
        afficherPosts();
    }

    private void deletePost(Post post) {
        PostCRUD service = new PostCRUD();
        try {
            service.supprimer(post.getId());
            loadPosts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modifierPost(Post post) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierPost.fxml"));
            Parent modifierPostParent = loader.load();

            ModifierPostController controller = loader.getController();
            controller.initData(post);

            Scene modifierPostScene = new Scene(modifierPostParent);
            Stage window = new Stage();
            window.setScene(modifierPostScene);
            window.showAndWait();

            loadPosts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleAddPostButton(ActionEvent event) {
        try {
            Parent addPostParent = FXMLLoader.load(getClass().getResource("/addpost.fxml"));
            Scene addPostScene = new Scene(addPostParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(addPostScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void commentOnPost(Post post) {
        // Create a TextInputDialog for the user to enter the comment
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Comment");
        dialog.setHeaderText("Enter your comment:");

        // Show the dialog and wait for the user's response
        dialog.showAndWait().ifPresent(commentText -> {
            // When the user submits the comment, create a new Comment object
            Comment comment = new Comment(commentText, post);

            // Call the ajouter method to add the comment to the database
            try {
                CommentCRUD commentCRUD = new CommentCRUD();
                commentCRUD.ajouter(comment);

                // After adding the comment, refresh the posts
                loadPosts();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the SQLException
            }
        });
    }




    private List<Comment> getCommentsForPost(Post post) {
        List<Comment> comments = new ArrayList<>();
        try {
            // Retrieve comments for the given post from the database
            CommentCRUD commentCRUD = new CommentCRUD();
            comments = commentCRUD.getCommentsForPost(post);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }


    @FXML
    private void handlePostClick(Post post) {
        // Create a new stage for displaying comments
        Stage popupStage = new Stage();
        popupStage.setTitle("Comments");

        // Create a VBox to hold the comments
        VBox vbox = new VBox();
        vbox.getStyleClass().add("comments-popup"); // Add the style class for the popup
        vbox.setSpacing(10);

        // Retrieve comments for the selected post
        List<Comment> comments = getCommentsForPost(post);
        for (Comment comment : comments) {
            // Create a container for each comment
            VBox commentContainer = new VBox();
            commentContainer.getStyleClass().add("comment-container"); // Add the style class for the comment container
            commentContainer.setSpacing(5); // Adjust spacing between elements within the container

            // Create label for the comment text
            Label commentLabel = new Label(comment.getText());
            commentLabel.getStyleClass().add("comment-label"); // Add the style class for the comment label
            commentLabel.setWrapText(true); // Allow wrapping of long comments

            // Add the comment label to the comment container
            commentContainer.getChildren().add(commentLabel);

            // Add the comment container to the main VBox
            vbox.getChildren().add(commentContainer);
        }

        // Add the VBox to the scene and set the scene in the stage
        Scene scene = new Scene(vbox, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());// Load CSS file
        popupStage.setScene(scene);
        popupStage.show();
    }


}
