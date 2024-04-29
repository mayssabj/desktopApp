package edu.esprit.controller;

import edu.esprit.entities.User;
import edu.esprit.services.UserService;
import edu.esprit.utils.mydb;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import edu.esprit.services.CommentCRUD;
import edu.esprit.services.PostCRUD;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class AfficherPostController {
    @FXML
    private GridPane grid;
    @FXML
    private ComboBox comboBox;

    @FXML
    private TextField tfSearch;


    @FXML
    private ListView<Post> listView;

    ObservableList<Post> postlist = FXCollections.observableArrayList();

    UserService U=new UserService();
    User a=U.getCurrentLoggedInUser();

    @FXML
    private Pagination pagination;

    private final int ITEMS_PER_PAGE = 4;
    private ObservableList<Post> allPosts;



    @FXML
    private void initialize() {
        grid.getStyleClass().add("grid-pane");
        loadAllPosts();

        pagination.setPageFactory(this::createPage);
/*
        ObservableList<String> listTrier = FXCollections.observableArrayList("Titre", "Description", "Place", "Type", "Date");
        comboBox.setItems(listTrier);


        FilteredList<Post> filtredData = new FilteredList<>(postlist, e -> true);
        tfSearch.setOnKeyReleased(e -> {
            tfSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
                filtredData.setPredicate((Predicate<? super Post>) post -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFiler = newValue.toLowerCase();
                    if (post.getTitre().contains(lowerCaseFiler)) {
                        return true;
                    } else if (post.getDescription().toLowerCase().contains(lowerCaseFiler)) {
                        return true;
                    } else if (post.getPlace().toLowerCase().contains(lowerCaseFiler)) {
                        return true;
                    } else if (post.getType() != null && post.getType().toString().toLowerCase().contains(lowerCaseFiler)) {
                        return true;
                    } else if (post.getDate() != null && post.getDate().toString().toLowerCase().contains(lowerCaseFiler)) {
                        return true;

                    }

                    return false;
                });
            });
            SortedList<Post> sortedData = new SortedList<>(filtredData);
            listView.setItems(sortedData);

        });

     */
    }
    private void loadAllPosts() {
        PostCRUD postCRUD = new PostCRUD();
        try {
            allPosts = FXCollections.observableArrayList(postCRUD.afficher());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, allPosts.size());
        List<Post> postsForPage = allPosts.subList(fromIndex, toIndex);

        int rowIndex = 0;
        int columnIndex = 0;
        grid.setHgap(20);
        grid.setVgap(20);
        for (Post post : postsForPage) {
            VBox postBox;
            try {
                postBox = createPostBox(post);
                grid.add(postBox, columnIndex, rowIndex);

                columnIndex++;
                if (columnIndex >= 3) {
                    columnIndex = 0;
                    rowIndex++;
                }
            } catch (FileNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }

        return new ScrollPane(grid);
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
                if (columnIndex >= 3) {
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

    private VBox createPostBox(Post post) throws FileNotFoundException, SQLException {
        VBox postBox = new VBox();
        postBox.getStyleClass().add("chosen-fruit-card");
        postBox.setPrefWidth(600);
        postBox.setPadding(new Insets(10));

        int id = post.getuser_id();
        UserService user_idService = new UserService();
        User u = user_idService.getUserById(id);

        HBox namephotoBox = new HBox();
        namephotoBox.setSpacing(10);
        namephotoBox.setAlignment(Pos.CENTER_LEFT);

        String imagePath = u.getProfilePicture();
        ImageView user_idPhoto = new ImageView(new Image(new FileInputStream(imagePath)));
        user_idPhoto.setFitWidth(30);
        user_idPhoto.setFitHeight(30);

        Label labelProfilename = new Label(u.getEmail());
        labelProfilename.setFont(new Font("Cambria", 18));
        labelProfilename.setTextFill(Color.web("#333333"));



        Region spring1 = new Region();
        HBox.setHgrow(spring1, Priority.ALWAYS);

        namephotoBox.getChildren().addAll(user_idPhoto, spring1, labelProfilename);


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
            // Try to load image from local file
            InputStream inputStream = new FileInputStream(post.getImageUrl());
            fruitImg.setImage(new Image(inputStream));
        } catch (FileNotFoundException e1) {
            try {
                // If loading from local file fails, try to load from web URL
                InputStream inputStream = new URL(post.getImageUrl()).openStream();
                fruitImg.setImage(new Image(inputStream));
            } catch (Exception e2) {
                // Handle any exceptions
                e2.printStackTrace();
            }
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

/*        Button commentButton = new Button();
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
*/
        // commentButton.setOnAction(event -> handlePostClick(post));
        HBox buttonBox = new HBox(10);
        if (user_idService.getCurrentLoggedInUser().getId() == u.getId()) {
            Button deleteButton = new Button();
            ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/delete-icon.png")));
            deleteIcon.setFitWidth(16);
            deleteIcon.setFitHeight(16);
            deleteButton.setGraphic(deleteIcon);
            deleteButton.getStyleClass().add("add-btn");
            deleteButton.setFont(new Font("System Bold", 18));
            deleteButton.setTextFill(Color.valueOf("#828282"));
            if (user_idService.getCurrentLoggedInUser().getId() == u.getId()) {
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

            modifierButton.setOnAction(event -> modifierPost(post));



            buttonBox.getChildren().addAll(deleteButton, modifierButton);
            buttonBox.setSpacing(10);
            HBox.setMargin(buttonBox, new Insets(10, 0, 0, 0)); // top, right, bottom, left

            buttonBox.setPadding(new Insets(0, 0, 10, 0)); // bottom padding for buttonBox


        }

        // Retrieve comments for the selected post
        CommentCRUD commentCRUD = new CommentCRUD();
        List<Comment> comments = commentCRUD.getCommentsForPost(post);
        // Create a VBox to hold the comments
        // Inside createPostBox method
        VBox commentsBox = new VBox();
        String commentCount = new CommentCRUD().countComment(post);
        Label user_idNameLabel = new Label("Comments (" + commentCount + ")");
        commentsBox.getChildren().add(user_idNameLabel);
        commentsBox.setSpacing(10);
        commentsBox.setStyle("-fx-background-color: #ffffff; -fx-padding: 20px;");


// Add each comment to the comments VBox
        for (Comment comment : comments) {
            System.out.println(comment);

            commentsBox.getChildren().addAll(
                    new CommentDesign(comment.getId_u().getEmail(), comment.getText())
            );
            if (user_idService.getCurrentLoggedInUser().getId() == comment.getId_u().getId()) {
                Button deleteButton2 = new Button();
                ImageView deleteIcon2 = new ImageView(new Image(getClass().getResourceAsStream("/icons/delete-icon.png")));
                deleteIcon2.setFitWidth(10);
                deleteIcon2.setFitHeight(10);
                deleteButton2.setGraphic(deleteIcon2);
                deleteButton2.getStyleClass().add("add-btn");
                deleteButton2.setFont(new Font("System Bold", 18));
                deleteButton2.setTextFill(Color.valueOf("#828282"));

                System.out.println(comment);
                deleteButton2.setOnAction(event -> deleteCmnt(comment));

                commentsBox.getChildren().addAll(deleteButton2);
                ScrollPane scrollPane = new ScrollPane(commentsBox);
                scrollPane.setFitToWidth(true);
                commentsBox.setPadding(new Insets(10, 0, 0, 0));

            }
        }
        // Set margin for the buttonBox
        // top padding for commentsBox




// Add the comments VBox to the postBox
// Create a TextField for the user_id to enter the comment
        TextField commentField = new TextField();
        commentField.setPromptText("Write a comment...");
        commentField.setStyle("-fx-pref-width: 280px; -fx-pref-height: 30px;");

        Button submitButton = new Button();
        ImageView cmIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/cmnt.png")));
        cmIcon.setFitWidth(16);
        cmIcon.setFitHeight(16);
        submitButton.setGraphic(cmIcon);
        submitButton.getStyleClass().add("add-btn");
        submitButton.setFont(new Font("System Bold", 18));
        submitButton.setTextFill(Color.WHITE);
        submitButton.setStyle("-fx-pref-width: 80px; -fx-pref-height: 30px;");

        // Add an event handler to the submit button
        submitButton.setOnAction(event -> {
            String commentText = commentField.getText();
            if (!commentText.isEmpty()) {
                try {
                    User currentuser_id = user_idService.getCurrentLoggedInUser();
                    Comment comment = new Comment(commentText, post, currentuser_id);
                    commentCRUD.ajouter(comment);
                    // Reload the posts to reflect the new comment
                    loadPosts();
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle SQLException
                }
            }
        });

        // Add the comment field and submit button to a HBox
        HBox commentInputBox = new HBox(10);
        commentInputBox.getChildren().addAll(commentField, submitButton);




        HBox user_idBox = new HBox(10);
        user_idBox.getChildren().addAll(user_idPhoto, labelProfilename);

        postBox.getChildren().addAll(user_idBox, titleDateBox, fruitImg, labelType, labelDescription, labelPlace, buttonBox, new Region(), commentsBox, commentInputBox);


        return postBox;
    }


    private void loadPosts() {
        grid.getChildren().clear();
        loadAllPosts();

        pagination.setPageFactory(this::createPage);
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

    private void deleteCmnt(Comment comment) {
        CommentCRUD service = new CommentCRUD();
        try {
            service.supprimer(comment.getId());
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
/*
    private void commentOnPost(Post post) {
        // Create a TextInputDialog for the user_id to enter the comment
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Comment");
        dialog.setHeaderText("Enter your comment:");
        // Show the dialog and wait for the user_id's response
        dialog.showAndWait().ifPresent(commentText -> {
            // When the user_id submits the comment, create a new Comment object
            Comment comment = new Comment(commentText, post,a);

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
*/

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

    @FXML
    void Select(ActionEvent event) {
        if (comboBox.getSelectionModel().getSelectedItem().toString().equals("titre")) {
            grid.getChildren().clear(); // Clear existing children of the grid

            try (Connection connection = mydb.getInstance().getCon()) {
                String query = "SELECT * FROM post ORDER BY titre ASC";
                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {

                    ObservableList<Post> retrievedPosts = FXCollections.observableArrayList(); // Create a new ObservableList to hold the retrieved posts

                    while (resultSet.next()) {
                        // Create a Post object for each row in the result set
                        Post post = new Post(
                                resultSet.getInt("id"),
                                resultSet.getString("titre"),
                                resultSet.getString("description"),
                                resultSet.getString("image_url"),
                                resultSet.getDate("date"),
                                Post.Type.valueOf(resultSet.getString("type")),
                                resultSet.getString("place"),
                                resultSet.getInt("user_id")
                        );

                        retrievedPosts.add(post); // Add the Post object to the ObservableList
                    }

                    listView.setItems(retrievedPosts); // Set the ObservableList containing retrieved posts to the ListView
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}