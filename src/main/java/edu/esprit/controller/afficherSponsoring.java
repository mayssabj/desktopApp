package edu.esprit.controller;

import edu.esprit.entities.Sponsoring;
import edu.esprit.services.SponsoringService;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

public class afficherSponsoring {
    @FXML
    private GridPane grid;

    @FXML
    private void initialize() {
        grid.getStyleClass().add("grid-pane");
        afficherSponsoring();
    }


    @FXML
    private void afficherSponsoring() {
        SponsoringService sponsoringService = new SponsoringService();
        try {
            List<Sponsoring> sponsorings = sponsoringService.afficherSponsoring();
            int row = 0;
            int column = 0;
            for (Sponsoring sponsoring : sponsorings) {
                // Créer une carte (card) pour chaque sponsoring
                VBox sponsoringBox = new VBox();
                sponsoringBox.setAlignment(Pos.CENTER);
                sponsoringBox.setSpacing(5); // Espacement vertical entre les éléments
                sponsoringBox.getStyleClass().add("card"); // Appliquer le style CSS de la carte
                // Set the margin around the VBox
                VBox.setMargin(sponsoringBox, new Insets(10, 10, 10, 10));

                // Créer un ImageView pour afficher l'image du sponsoring
                String imageUrl = sponsoring.getImage(); // Supposons que getImageUrl() renvoie le chemin d'accès à l'image
                Image image = new Image(new FileInputStream(imageUrl));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100); // Ajuster la largeur de l'image
                imageView.setFitHeight(100); // Ajuster la hauteur de l'image
                sponsoringBox.getChildren().add(imageView);

                // Créer un Label pour afficher le nom du sponsoring
                Label nameLabel = new Label(sponsoring.getName());
                sponsoringBox.getChildren().add(nameLabel);

                // Ajouter la carte à votre GridPane
                grid.add(sponsoringBox, column, row);

                // Incrémenter l'indice de colonne
                column++;
                if (column == 3) {
                    column = 0;
                    row++;
                }
            }
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }





}
