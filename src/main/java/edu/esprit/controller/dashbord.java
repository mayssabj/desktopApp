package edu.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class dashbord {
    @FXML
    private VBox vboxdash;
    @FXML
<<<<<<< HEAD
    void showsponsor(MouseEvent event) {

        try {
            // Load user.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/showsponsoring.fxml"));
            Node eventFXML = loader.load();

            // Clear existing content from FieldHolder
            vboxdash.getChildren().clear();

            // Add the loaded userFXML to FieldHolder
            vboxdash.getChildren().add(eventFXML);
        } catch (IOException e) {
            // Handle exception (e.g., file not found or invalid FXML)
=======
    void showpost(MouseEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/showPostAdminController.fxml"));
            Node eventFXML = loader.load();

            vboxdash.getChildren().clear();

            vboxdash.getChildren().add(eventFXML);
        } catch (IOException e) {
>>>>>>> 560008013e3371a7c394d79c8393560c80ec93e7
            e.printStackTrace();
        }
    }

<<<<<<< HEAD
}
=======
}
>>>>>>> 560008013e3371a7c394d79c8393560c80ec93e7
