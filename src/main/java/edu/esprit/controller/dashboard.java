package edu.esprit.controller;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import edu.esprit.entities.Post;
import edu.esprit.services.PostCRUD;
import edu.esprit.utils.mydb;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;

public class dashboard {

    @FXML
    private TableColumn<Post, Integer> nameColumn;

    @FXML
    private TableColumn<Post, String> descriptionColumn;

    @FXML
    private TableColumn<Post, String> dateColumn2;

    @FXML
    private TableColumn<Post, String> placeColumn;

    @FXML
    private TableColumn<Post, Boolean> typeColumn;
    @FXML
    private TableColumn<Post, Date> dateColumn;

    @FXML
    private TableView<Post> tableViewUsers;

    @FXML
    private TableColumn<Post, String> actionsCol;

    @FXML
    private TextField tfSearch;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private VBox vboxdash;

    ObservableList<Post> userList = FXCollections.observableArrayList();

    Button blockButton = null;

    @FXML
    private void initialize() throws SQLException {
        // Initialiser la vue
        fnReloadData();

        // Ajouter un écouteur sur la liste déroulante pour détecter les changements de sélection
        comboBox.setOnAction(event -> {
            try {
                fnReloadData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


    @FXML
    private void Select(ActionEvent event) {
        if (comboBox.getSelectionModel().getSelectedItem().equals("username")) {
            userList.clear();
            try (Connection connection = mydb.getInstance().getCon()) {
                String query = "SELECT * FROM `post` WHERE id != 1 ORDER BY titre ASC";
                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        Post post = new Post(
                                resultSet.getInt("id"),
                                resultSet.getString("titre"),
                                resultSet.getString("description"),
                                resultSet.getString("image_url"),
                                resultSet.getDate("date"),
                                Post.Type.valueOf(resultSet.getString("type")),
                                resultSet.getString("place"),
                                resultSet.getInt("user")
                        );
                        userList.add(post);
                    }
                }
                tableViewUsers.setItems(userList);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

        } else if (comboBox.getSelectionModel().getSelectedItem().equals("email")) {
            userList.clear();
            try (Connection connection = mydb.getInstance().getCon()) {
                String query = "SELECT * FROM `post` WHERE id != 1 ORDER BY numero ASC";
                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        // Récupération de l'état de vérification de l'utilisateur
                        Post post = new Post(
                                resultSet.getInt("id"),
                                resultSet.getString("titre"),
                                resultSet.getString("description"),
                                resultSet.getString("image_url"),
                                resultSet.getDate("date"),
                                Post.Type.valueOf(resultSet.getString("type")),
                                resultSet.getString("place"),
                                resultSet.getInt("user")
                        );
                        PostCRUD us = new PostCRUD();
                        userList.add(post);
                    }
                }
                tableViewUsers.setItems(userList);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/showPostAdminController.fxml"));
            Node eventFXML = loader.load();

            vboxdash.getChildren().clear();

            vboxdash.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void fnReloadData() throws SQLException {
        // Associer les colonnes du tableau aux propriétés de l'objet User
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        placeColumn.setCellValueFactory(new PropertyValueFactory<>("place"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        // Clear existing items and load new data from the database
        userList.clear();
        userList.addAll(loadDataFromDatabase());

        // Create a new FilteredList based on the user list
        FilteredList<Post> filteredData = new FilteredList<>(userList, e -> true);

        // Set the search functionality
        tfSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Post>) post -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (post.getTitre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (post.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (post.getPlace().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (post.getType() != null && post.getType().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (post.getDate() != null && post.getDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        // Wrap the filtered list in a SortedList
        SortedList<Post> sortedData = new SortedList<>(filteredData);

        // Bind the SortedList comparator to the TableView comparator
        tableViewUsers.setItems(sortedData);
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
