package edu.esprit.controller;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CommentDesign extends VBox {

    public CommentDesign(String userName, String commentText) {
        // Create a label for the user name
        Label userNameLabel = new Label(userName);
        userNameLabel.setStyle("-fx-font-weight: bold;");

        // Create a label for the comment text
        Label commentTextLabel = new Label(commentText);

        // Create an HBox to hold the user name and comment text
        HBox contentBox = new HBox();
        contentBox.getChildren().addAll(userNameLabel, commentTextLabel);
        contentBox.setSpacing(10);

        // Style the content box
        contentBox.setStyle("-fx-padding: 5px;");

        // Add content box to this VBox
        getChildren().add(contentBox);

        // Style the comment design
        setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10px;");
    }
}
