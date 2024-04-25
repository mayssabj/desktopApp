package edu.esprit.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.esprit.entities.Post;
import edu.esprit.entities.User;
import edu.esprit.services.PostCRUD;
import edu.esprit.services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class dashboard {

    @FXML
    private TextField tfSearch;

    @FXML
    private VBox vboxdash;

    ObservableList<Post> userList = FXCollections.observableArrayList();

    List<VBox> postBoxes = new ArrayList<>();

    @FXML
    private void initialize() throws SQLException {
        fnReloadData();
    }
    @FXML
    private void fnReloadData() throws SQLException {
        userList.clear();
        userList.addAll(loadDataFromDatabase());

        FilteredList<Post> filteredData = new FilteredList<>(userList, e -> true);

        SortedList<Post> sortedData = new SortedList<>(filteredData);

        vboxdash.getChildren().clear();

        for (Post post : sortedData) {
            Label titleLabel = new Label(post.getTitre());
            Label descriptionLabel = new Label(post.getDescription());
            Label placeLabel = new Label(post.getPlace());
            Label typeLabel = new Label(post.getType().toString());
            Label dateLabel = new Label(post.getDate().toString());
            int a=post.getUser();
            UserService s=new UserService();
            User u1=s.getUserById(a);
            Label userLabel = new Label(u1.getAddress());

            VBox postBox = new VBox(userLabel,titleLabel, descriptionLabel, placeLabel, typeLabel, dateLabel);
            postBox.setStyle("-fx-padding: 10px; -fx-background-color: #f0f0f0; -fx-border-color: #ddd; -fx-border-width: 1px;");
            postBox.setSpacing(5);

            vboxdash.getChildren().add(postBox);

            postBoxes.add(postBox);
        }


        tfSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            for (VBox postBox : postBoxes) {
                String lowerCaseFilter = newValue.toLowerCase();
                boolean isVisible = postBox.getChildren().stream()
                        .filter(Label.class::isInstance)
                        .map(Label.class::cast)
                        .anyMatch(label -> label.getText().toLowerCase().contains(lowerCaseFilter));
                postBox.setVisible(isVisible);
                postBox.setManaged(isVisible);
            }
        });

    }
    private List<Post> loadDataFromDatabase() throws SQLException {
        List<Post> data = new ArrayList<>();
        PostCRUD us = new PostCRUD();
        for (int i = 0; i < us.afficher().size(); i++) {
            System.out.println(us.afficher().get(i).toString());
            Post post = us.afficher().get(i);
            data.add(post);
        }
        return data;
    }
}
