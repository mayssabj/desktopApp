package edu.esprit.controller.Reclamation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class Dashboard2Cpntroller {
    @FXML
    private VBox vboxdash;
    @FXML
    void showpost(MouseEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashb.fxml"));
            Node eventFXML = loader.load();

            vboxdash.getChildren().clear();

            vboxdash.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
