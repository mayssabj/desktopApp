package edu.esprit.services;

import edu.esprit.entities.Comment;
import edu.esprit.entities.Post;
import edu.esprit.entities.User;
import edu.esprit.utils.mydb;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CommentCRUD implements ServicePost<Comment> {

    @FXML
    private ListView<String> commentListView;
    @FXML
    private TextField commentTextField;

    private Connection connection;

    public CommentCRUD() {
        connection = mydb.getInstance().getCon();
    }

    @Override
    public void ajouter(Comment comment) throws SQLException {
        String req = "INSERT INTO `comment`(`text`,`post`) VALUES (?,?)";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, comment.getText());
            pst.setInt(2, comment.getPost().getId());
            pst.executeUpdate();
        }
    }

    @Override
    public void modifier(Comment comment) throws SQLException {
        String req = "UPDATE `comment` SET `text`=? WHERE `id`=?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, comment.getText());
            pst.setInt(2, comment.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM `comment` WHERE `id`=?";
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
    public List<Comment> afficher() throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String req = "SELECT * FROM `comment`";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String text = rs.getString("text");
                // You might need to retrieve the post ID from the result set and fetch the corresponding post object from the database
                // For simplicity, I'm assuming you already have a method to get Post object by ID
                int postId = rs.getInt("post_id");
                Post post = getPostById(postId);

                Comment comment = new Comment(id, text, post);
                comments.add(comment);
            }
        }
        return comments;
    }

    private Post getPostById(int postId) throws SQLException {
        return null;
    }

    public List<Comment> getCommentsForPost(Post post) throws SQLException {
        List<Comment> commentsForPost = new ArrayList<>();
        String req = "SELECT * FROM `comment` WHERE `post`=?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, post.getId());
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String text = rs.getString("text");
                    Comment comment = new Comment(text, post);
                    commentsForPost.add(comment);
                }
            }
        }
        return commentsForPost;
    }

    public String countComment(Post post) throws SQLException {
        String req = "SELECT COUNT(*) AS comment_count FROM `comment` WHERE `post`=?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, post.getId());
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("comment_count");
                    return Integer.toString(count);
                }
            }
        }
        return "0"; // Return 0 if no result found
    }




    public void initData(Post post) {
        // Implementation of initData method
    }


}
