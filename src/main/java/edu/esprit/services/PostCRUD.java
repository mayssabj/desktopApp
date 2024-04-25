package edu.esprit.services;

import edu.esprit.entities.Post;
import edu.esprit.entities.User;
import edu.esprit.utils.mydb;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class PostCRUD implements ServicePost<Post> {

    @FXML
    private ListView<String> postListView;
    @FXML
    private Label labelTitle;
    @FXML
    private Label labelDescription;
    @FXML
    private TextField Title;
    @FXML
    private TextField Description;
    @FXML
    private TextField ImageUrl;
    @FXML
    private TextField Place;
    @FXML
    private TextField Type;
    @FXML
    private Button btnadd;
    @FXML
    private GridPane grid;


    private Connection connection;
    Statement ste;

    public PostCRUD() {
        connection = mydb.getInstance().getCon();
    }

    @Override
    public void ajouter(Post post) throws SQLException {
        UserService userService=new UserService();
        User u1=userService.getCurrentLoggedInUser();
        String req = "INSERT INTO `post`(`titre`, `description`, `image_url`, `date`, `type`, `place`, `user`) VALUES (?, ?, ?, ?, ?, ?,?)";
        long millis = System.currentTimeMillis();
        java.sql.Date today = new java.sql.Date(millis);
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, post.getTitre());
            pst.setString(2, post.getDescription());
            pst.setString(3, post.getImageUrl());
            pst.setDate(4, today);
            pst.setString(5, post.getType().toString());
            pst.setString(6, post.getPlace());
            pst.setInt(7, u1.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public void modifier(Post post) throws SQLException {
        String req = "UPDATE `post` SET `titre`=?, `description`=?, `image_url`=?, `date`=?, `type`=?, `place`=? WHERE `id`=?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, post.getTitre());
            pst.setString(2, post.getDescription());
            pst.setString(3, post.getImageUrl());
            pst.setDate(4, new java.sql.Date(post.getDate().getTime()));
            pst.setString(5, post.getType().toString());
            pst.setString(6, post.getPlace());
            pst.setInt(7, post.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM `post` WHERE `id`=?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            afficher();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> afficher() throws SQLException {
        List<Post> posts = new ArrayList<>();
        String req = "SELECT * FROM `post`";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String titre = rs.getString("titre");
                String description = rs.getString("description");
                String imageUrl = rs.getString("image_url");
                Date date = rs.getDate("date");
                Post.Type type = Post.Type.valueOf(rs.getString("type"));
                String place = rs.getString("place");
                int user = rs.getInt("user");

                Post post = new Post(id, titre, description, imageUrl, date, type, place, user);
                posts.add(post);
            }
        }
        return posts;
    }


    private VBox createPostBox(String titre, String description, String type, String place) {
        VBox postBox = new VBox();
        postBox.getStyleClass().add("chosen-fruit-card");
        postBox.setPrefWidth(300);
        postBox.setPadding(new Insets(10));

        Label labelTitle = new Label(titre);
        labelTitle.setFont(new Font("Cambria", 24));
        labelTitle.setTextFill(Color.WHITE);

        ImageView fruitImg = new ImageView();
        fruitImg.setFitHeight(170);
        fruitImg.setFitWidth(285);
        fruitImg.setImage(new Image("https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg"));

        Label labelDescription = new Label(description);
        labelDescription.setFont(new Font("Cambria", 18));
        labelDescription.setTextFill(Color.WHITE);

        Label labelType = new Label("Type: " + type);
        labelType.setFont(new Font("Cambria", 24));
        labelType.setTextFill(Color.WHITE);

        Label labelPlace = new Label("Place: " + place);
        labelPlace.setFont(new Font("Cambria", 24));
        labelPlace.setTextFill(Color.WHITE);

        Button addButton = new Button("ADD TO CART");
        addButton.getStyleClass().add("add-btn");
        addButton.setFont(new Font("System Bold", 18));
        addButton.setTextFill(Color.valueOf("#828282"));

        postBox.getChildren().addAll(labelTitle, fruitImg, labelDescription, labelType, labelPlace, addButton);
        return postBox;
    }

}