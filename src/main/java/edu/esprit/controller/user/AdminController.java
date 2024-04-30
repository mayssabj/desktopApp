package edu.esprit.controller.user;

import edu.esprit.entities.Post;
import edu.esprit.entities.User;
import edu.esprit.services.UserService;
import edu.esprit.utils.ReflectionUtil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminController {
    public AnchorPane anchorPane;

    private UserService userService;


    private double columnWidth = 150; // Fixed width for each column, adjust as necessary

    private HBox createTableEntry(Object object) {
        HBox hbox = new HBox();
        hbox.setSpacing(5);
        hbox.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 5px;");

        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(object);
                String valueString = value == null ? "null" : value.toString();
                if(value instanceof String){
                    valueString = ((String) value).length() ==0?"null":value.toString();
                }
                Label label = new Label(valueString);
                label.setPrefWidth(columnWidth); // Use the fixed column width
                label.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 12px; -fx-text-fill: black; -fx-padding: 2px;");
                hbox.getChildren().add(label);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return hbox;
    }

    public void setupTable(List<String> labels, List<?> objects) {
        // Create header row
        HBox headerRow = new HBox();
        headerRow.setSpacing(5);
//        headerRow.setPrefWidth(columnWidth * labels.size());
        headerRow.setStyle("-fx-background-color: #6b9eef; -fx-padding: 10px; -fx-spacing: 5px;");

        for (String label : labels) {
            Label titleLabel = new Label(label);
            titleLabel.setPrefWidth(columnWidth); // Use the fixed column width for headers
            titleLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
            headerRow.getChildren().add(titleLabel);
        }

        // Add header row to the AnchorPane
        anchorPane.getChildren().add(headerRow);
        AnchorPane.setTopAnchor(headerRow, 50.0); // Set the top anchor for the header

        // Add rows for each object
        double yOffset = 100.0; // Initial offset for the first row below the header
        for (Object object : objects) {
            HBox objectEntry = createTableEntry(object);
            anchorPane.getChildren().add(objectEntry);
            AnchorPane.setTopAnchor(objectEntry, yOffset); // Position each row
            yOffset += 35.0; // Increment y-offset for the next row
        }
    }

    @FXML
    private void initialize() throws SQLException {
        this.userService = new UserService();

//        fnReloadData();
        List<User> users = userService.getAllUsers();
        System.out.println(users);
        List<String> labels = ReflectionUtil.getFieldNamesFromList(users);
        setupTable(labels,users);
//        tfSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
//            for (Node child : anchorPane.getChildren()) {
//                if (child instanceof HBox) {
//                    HBox postEntry = (HBox) child;
//                    // Add your filtering logic here
//                    boolean isVisible = postEntry.getChildren().stream()
//                            .filter(Label.class::isInstance)
//                            .map(Label.class::cast)
//                            .anyMatch(label -> label.getText().toLowerCase().contains(newValue.toLowerCase()));
//                    postEntry.setVisible(isVisible);
//                    postEntry.setManaged(isVisible);
//                }
//            }
//        });
    }
}
