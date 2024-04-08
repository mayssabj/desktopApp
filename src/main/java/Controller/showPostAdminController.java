package Controller;
import entite.Post;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.PostCRUD;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class showPostAdminController implements Initializable {

    @FXML
    private TableView<Post> postTable;
    @FXML
    private TableColumn<Post, String> nameColumn;
    @FXML
    private TableColumn<Post, String> descriptionColumn;
    @FXML
    private TableColumn<Post, String> imageColumn;
    @FXML
    private TableColumn<Post, Date> dateColumn;
    @FXML
    private TableColumn<Post, String> placeColumn;
    @FXML
    private TableColumn<Post, Post.Type> typeColumn;
    @FXML
    private TableColumn<Post, Void> actionsColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        placeColumn.setCellValueFactory(new PropertyValueFactory<>("place"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Post post = getTableView().getItems().get(getIndex());
                    deletePost(post);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(deleteButton);
                    buttons.setSpacing(5);
                    setGraphic(buttons);
                }
            }
        });

        loadPost();
    }

    private void loadPost() {
        PostCRUD service = new PostCRUD();
        try {
            ObservableList<Post> data = FXCollections.observableArrayList(service.afficher());
            postTable.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void deletePost(Post post) {
        PostCRUD service = new PostCRUD();
        try {
            service.supprimer(post.getId());
            loadPost();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
