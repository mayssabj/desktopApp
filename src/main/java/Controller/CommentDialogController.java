package Controller;

import entite.Comment;
import entite.Post;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import services.CommentCRUD;

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
        String commentText = commentTextArea.getText();
        if (!commentText.isEmpty()) {
            Comment comment = new Comment(commentText, post);
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
