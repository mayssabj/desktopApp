package edu.esprit.controller.user;

import edu.esprit.entities.User;
import edu.esprit.services.UserService;
import edu.esprit.utils.ReflectionUtil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class AdminController {
    @FXML private VBox tableContainer;
    @FXML private Pagination pagination;
    @FXML private TextField tfSearch;

    private UserService userService;
    private List<User> allUsers;
    private List<User> filteredUsers;
    private static final int ROWS_PER_PAGE = 2;
    private double columnWidth = 100;

    @FXML
    private void initialize() throws SQLException {
        userService = new UserService();
        allUsers = userService.getAllUsers();
        filteredUsers = allUsers;
        setupHeader();
        setupSearch();
        updatePagination();
    }

    private void setupHeader() {
        HBox headerRow = new HBox();
        headerRow.setSpacing(10);
        headerRow.setStyle("-fx-background-color: #6b9eef; -fx-padding: 10px;");
        List<String> labels = ReflectionUtil.getFieldNamesFromList(allUsers);
        for (String label : labels) {
            Label titleLabel = new Label(label);
            titleLabel.setPrefWidth(columnWidth);
            titleLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
            headerRow.getChildren().add(titleLabel);
        }
        tableContainer.getChildren().add(headerRow);
    }

    private void setupSearch() {
        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredUsers = allUsers.stream()
                    .filter(user -> user.toString().toLowerCase().contains(newValue.toLowerCase()))
                    .collect(Collectors.toList());
            updatePagination();
        });
    }

    private void updatePagination() {
        int pageCount = (int) Math.ceil(filteredUsers.size() / (double) ROWS_PER_PAGE);
        pagination.setPageCount(pageCount > 0 ? pageCount : 1);
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, filteredUsers.size());
        return createEntries(filteredUsers.subList(fromIndex, toIndex));
    }

    private Node createEntries(List<User> subList) {
        VBox rowsContainer = new VBox(5);
        subList.forEach(user -> rowsContainer.getChildren().add(createTableEntry(user)));
        return rowsContainer;
    }

    private HBox createTableEntry(User user) {
        HBox hbox = new HBox(5);
        hbox.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 5px;");

        try {
            for (Field field : User.class.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(user);
                String valueString = value == null ? "null" : value.toString();
                Label label = new Label(valueString);
                label.setPrefWidth(columnWidth);
                label.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 12px; -fx-text-fill: black;");
                hbox.getChildren().add(label);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // Button to toggle user enabled status
        String buttonText = user.isEnabled() ? "Disable" : "Enable";
        Button toggleButton = new Button(buttonText);
        toggleButton.setPrefWidth(120);
        toggleButton.setOnAction(event -> toggleUserEnabled(user, toggleButton));
        hbox.getChildren().add(toggleButton);

        return hbox;
    }

    private void toggleUserEnabled(User user, Button button) {
        user.setEnabled(!user.isEnabled());  // Toggle the enabled status
        userService.updateUserEnabledStatus(user);  // Assume this method updates the user in the database

        // Update button text based on new status
        button.setText(user.isEnabled() ? "Disable" : "Enable");

        // Optionally, refresh the list or UI if needed
        updatePagination();
    }


}
