package edu.esprit.controller.user;

import edu.esprit.entities.User;
import edu.esprit.services.UserService;
import edu.esprit.utils.ReflectionUtil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;

public class AdminController {
    @FXML private VBox tableContainer; // Assume this VBox is defined to hold both header and rows.
    @FXML private Pagination pagination;

    private UserService userService;
    private List<User> allUsers;
    private static final int ROWS_PER_PAGE = 2;  // More realistic row count per page
    private double columnWidth = 105;  // This should fit the expected number of columns

    @FXML
    private void initialize() throws SQLException {
        userService = new UserService();
        allUsers = userService.getAllUsers();
        setupHeader();
        updatePagination();
    }

    private void setupHeader() {
        HBox headerRow = new HBox();
        headerRow.setSpacing(10);
        headerRow.setStyle("-fx-background-color: #6b9eef; -fx-padding: 10px; -fx-spacing: 5px;");
        List<String> labels = ReflectionUtil.getFieldNamesFromList(allUsers);
        for (String label : labels) {
            Label titleLabel = new Label(label);
            titleLabel.setPrefWidth(columnWidth);
            titleLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
            headerRow.getChildren().add(titleLabel);
        }
        tableContainer.getChildren().add(headerRow);  // Add the headerRow to the VBox
    }

    private void updatePagination() {
        int pageCount = (int) Math.ceil(allUsers.size() / (double) ROWS_PER_PAGE);
        pagination.setPageCount(pageCount > 0 ? pageCount : 1);
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, allUsers.size());
        List<User> subList = allUsers.subList(fromIndex, toIndex);
        return createEntries(subList);
    }

    private Node createEntries(List<User> subList) {
        VBox rowsContainer = new VBox();
        rowsContainer.setSpacing(5);

        for (User user : subList) {
            HBox row = createTableEntry(user);
            rowsContainer.getChildren().add(row);
        }
        return rowsContainer;
    }

    private HBox createTableEntry(User user) {
        HBox hbox = new HBox();
        hbox.setSpacing(5);
        hbox.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 5px;");
        try {
            for (Field field : User.class.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(user);
                String valueString = value == null ? "null" : value.toString();
                Label label = new Label(valueString);
                label.setPrefWidth(columnWidth);
                label.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 12px; -fx-text-fill: black; -fx-padding: 2px;");
                hbox.getChildren().add(label);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return hbox;
    }
}
