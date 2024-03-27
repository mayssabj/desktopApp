package edu.esprit.controller;

import edu.esprit.entities.Sponsoring;
import edu.esprit.services.SponsoringService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class showSponsoring implements Initializable {

    @FXML
    private TableView<Sponsoring> sponsoringTable;
    @FXML
    private TableColumn<Sponsoring, String> nameColumn;
    @FXML
    private TableColumn<Sponsoring, String> descriptionColumn;
    @FXML
    private TableColumn<Sponsoring, String> imageColumn;
    @FXML
    private TableColumn<Sponsoring, Date> dateColumn;
    @FXML
    private TableColumn<Sponsoring, Sponsoring.Duration> contratColumn;
    @FXML
    private TableColumn<Sponsoring, Sponsoring.TypeSpon> typeColumn;
    @FXML
    private TableColumn<Sponsoring, Void> actionsColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialisez vos colonnes ici
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        contratColumn.setCellValueFactory(new PropertyValueFactory<>("contrat"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        // Ajouter la colonne Actions avec les boutons de modification et de suppression
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(event -> {
                    Sponsoring sponsoring = getTableView().getItems().get(getIndex());
                    // Ouvrir la fenêtre de modification avec les détails du sponsoring
                    openEditSponsoringWindow(sponsoring);
                });

                deleteButton.setOnAction(event -> {
                    Sponsoring sponsoring = getTableView().getItems().get(getIndex());
                    // Supprimer le sponsoring
                    deleteSponsoring(sponsoring);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(editButton, deleteButton);
                    buttons.setSpacing(5);
                    setGraphic(buttons);
                }
            }
        });

        // Chargez vos données
        loadSponsorings();
    }

    private void loadSponsorings() {
        SponsoringService service = new SponsoringService();
        try {
            ObservableList<Sponsoring> data = FXCollections.observableArrayList(service.afficherSponsoring());
            sponsoringTable.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception en conséquence
        }
    }

    private Stage editDialogStage;

    private void openEditSponsoringWindow(Sponsoring sponsoring) {
        try {
            // Charger le fichier FXML de la nouvelle page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierSponsoring.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur de la nouvelle fenêtre
            modifierSponsoring controller = loader.getController();
            controller.initData(sponsoring);

            // Créer la scène avec la nouvelle page
            Scene scene = new Scene(root);

            // Créer une nouvelle fenêtre de dialogue
            editDialogStage = new Stage();
            editDialogStage.setScene(scene);
            editDialogStage.showAndWait(); // Attendre que la fenêtre soit fermée

            // Mettre à jour la ligne correspondante dans la table
            sponsoringTable.refresh(); // Rafraîchir la table pour afficher les changements
        } catch (Exception e) {
            e.printStackTrace();  // Gérer les exceptions en conséquence
        }
    }



    private void deleteSponsoring(Sponsoring sponsoring) {
        SponsoringService service = new SponsoringService();
        try {
            service.supprimerSponsoring(sponsoring.getId());
            loadSponsorings(); // Recharger les données après la suppression
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception en conséquence
        }
    }

}
