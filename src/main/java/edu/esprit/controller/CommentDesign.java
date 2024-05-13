package edu.esprit.controller;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class CommentDesign extends VBox {

    public CommentDesign(String user_idName, String commentText,String imagePath2) throws IOException {
        //image user actuel for login
        ImageView user_idPhoto2 = new ImageView();

        Image profileImage2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/default_user.png")));;
        if(!(imagePath2==null)){
            try {
                InputStream inputStream = new FileInputStream(imagePath2);
                profileImage2 = new Image(inputStream);
            }catch (FileNotFoundException e1){
                profileImage2 = new Image(new URL(imagePath2).openStream());
            }
        }
        user_idPhoto2.setImage(profileImage2);
        user_idPhoto2.setFitWidth(30);
        user_idPhoto2.setFitHeight(30);
        // Create a label for the user_id name
        Label user_idNameLabel = new Label(user_idName);
        user_idNameLabel.setStyle("-fx-font-weight: bold;");

        // Create a label for the comment text
        Label commentTextLabel = new Label(commentText);

        // Create an HBox to hold the user_id name and comment text
        HBox contentBox = new HBox();
        contentBox.getChildren().addAll(user_idPhoto2,user_idNameLabel, commentTextLabel);
        contentBox.setSpacing(7);

        // Style the content box
        contentBox.setStyle("-fx-padding: 7px;");

        // Add content box to this VBox
        getChildren().add(contentBox);

        // Style the comment design
        setStyle("-fx-background-color: #f5f1f1; -fx-padding: 8px;");
    }
}
