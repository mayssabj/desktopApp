package edu.esprit.controller;

import edu.esprit.entities.Comment;
import edu.esprit.entities.Post;
import edu.esprit.entities.User;
import edu.esprit.services.UserService;
import edu.esprit.utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import edu.esprit.services.CommentCRUD;

import java.sql.SQLException;

public class CommentDialogController {
    @FXML
    private TextArea commentTextArea;

    private Post post;

    public void setPost(Post post) {
        this.post = post;
    }

    @FXML
    private void submitComment() {
        User U= Session.getInstance().getCurrentUser();
        String commentText = commentTextArea.getText();
        if (!commentText.isEmpty()) {
            Comment comment = new Comment(commentText, post,U);
            CommentCRUD commentCRUD = new CommentCRUD();
            try {
                commentCRUD.ajouter(comment);
                closeDialog();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeDialog() {
        Stage stage = (Stage) commentTextArea.getScene().getWindow();
        stage.close();
    }
}
