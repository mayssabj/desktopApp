package edu.esprit.controller;

import edu.esprit.MainFX;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;



public class dash {
    @FXML
    private HBox vboxdash;



    public void showsponsor(MouseEvent mouseEvent) {
    }
    @FXML
    public void showreclamation(MouseEvent mouseEvent) {

        try {
            // Charger la page ListAvertissement.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/ReclamationList.fxml"));
            Node avertissementPage = loader.load();

            // Effacer le contenu actuel de vboxdash et ajouter la page ListAvertissement.fxml
            vboxdash.getChildren().clear();
            vboxdash.getChildren().add(avertissementPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
