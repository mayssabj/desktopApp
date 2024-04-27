
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
    void showsponsor(MouseEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/showsponsoring.fxml"));
            Node eventFXML = loader.load();

            // Clear existing content from vboxdash
            vboxdash.getChildren().clear();

            // Add the loaded eventFXML to vboxdash
            vboxdash.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();  // Handle IOException (e.g., file not found or invalid FXML)
        }
    }
    @FXML
    void showspostgroup(MouseEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/showpostgroup.fxml"));
            Node eventFXML = loader.load();

            // Clear existing content from vboxdash
            vboxdash.getChildren().clear();

            // Add the loaded eventFXML to vboxdash
            vboxdash.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();  // Handle IOException (e.g., file not found or invalid FXML)
        }
    }

    @FXML
    void showpost(MouseEvent event) {
        try {
            // Load showPostAdminController.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/showPostAdminController.fxml"));
            Node eventFXML = loader.load();

            // Clear existing content from vboxdash
            vboxdash.getChildren().clear();

            // Add the loaded eventFXML to vboxdash
            vboxdash.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();  // Handle IOException
        }
    }
    @FXML
    void showpostad(MouseEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/showPostAdminController.fxml"));
            Node eventFXML = loader.load();

            // Clear existing content from vboxdash
            vboxdash.getChildren().clear();

            // Add the loaded eventFXML to vboxdash
            vboxdash.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();  // Handle IOException (e.g., file not found or invalid FXML)
        }
    }
}
