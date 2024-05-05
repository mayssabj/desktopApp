package edu.esprit.controller;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CommentDesign extends VBox {

    public CommentDesign(String user_idName, String commentText) {
        // Create a label for the user_id name
        Label user_idNameLabel = new Label(user_idName);
        user_idNameLabel.setStyle("-fx-font-weight: bold;");

        // Create a label for the comment text
        Label commentTextLabel = new Label(commentText);

        // Create an HBox to hold the user_id name and comment text
        HBox contentBox = new HBox();
        contentBox.getChildren().addAll(user_idNameLabel, commentTextLabel);
        contentBox.setSpacing(7);

        // Style the content box
        contentBox.setStyle("-fx-padding: 7px;");

        // Add content box to this VBox
        getChildren().add(contentBox);

        // Style the comment design
        setStyle("-fx-background-color: #f5f1f1; -fx-padding: 8px;");
    }
}
