package edu.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class dash {
    @FXML
    private HBox vboxdash;

    @FXML
    void showsponsor(MouseEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/affichersponsoring.fxml"));
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
    private void handleAddPostButton(ActionEvent event) {
        try {
            Parent addPostParent = FXMLLoader.load(getClass().getResource("/addpost.fxml"));
            Scene addPostScene = new Scene(addPostParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(addPostScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showpost(MouseEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Post.fxml"));
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
    void showMarket(MouseEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/marketDisplayCard.fxml"));
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
    void showVoucher(MouseEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/voucherList.fxml"));
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
    void showQA(MouseEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Questions.fxml"));
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
    void showpostgroupe(MouseEvent event) {
        try {
            // Load showsponsoring.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherPostgroup.fxml"));
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
