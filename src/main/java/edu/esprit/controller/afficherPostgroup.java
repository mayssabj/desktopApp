package edu.esprit.controller;

import edu.esprit.entities.Post_group;
import edu.esprit.entities.Sponsoring;
import edu.esprit.entities.User;
import edu.esprit.services.PostgroupService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class afficherPostgroup {

    @FXML
    private VBox postbox;

    @FXML
    private TextField sasie1;

    @FXML
    private Button ajouter1;
    @FXML
    private Label sponsoringLabel;
    @FXML
    private Button buttonretour;


    private PostgroupService postgroupService;

    public afficherPostgroup() {
        postgroupService = new PostgroupService();
    }

    @FXML
    public void initialize() {
        setSponsoringName("Nom du Sponsoring"); // Remplacez "NomDuSponsoring" par le nom du sponsoring à afficher
        afficherPostsSponsoring(sponsoringName);
    }


    public void afficherPostsSponsoring(String sponsoringName) {
        postbox.getChildren().clear();
        try {
            List<Post_group> posts = postgroupService.afficherBySponsoring(sponsoringName); // Méthode à implémenter dans PostgroupService
            for (Post_group post : posts) {
                User user = post.getUser_id();
                if (user != null) {
                    Label userNameLabel = new Label(user.getUsername());

                    // Utilisation d'ImageView pour afficher l'image de l'utilisateur
                    ImageView userImageView = new ImageView(new Image("file://" + user.getImage())); // Assurez-vous que user.getImage() contient le chemin complet de l'image
                    userImageView.setFitWidth(40);
                    userImageView.setFitHeight(40);

                    Label contenuLabel = new Label(post.getContenu());
                    HBox postItem = new HBox(userImageView, userNameLabel, new Label("   "), contenuLabel);
                    postItem.setSpacing(10); // Espacement de 10 pixels entre les éléments
                    postbox.getChildren().add(postItem);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @FXML
    public void ajouterPostgroup() {
        String contenu = sasie1.getText();
        // Supposons que vous avez déjà un utilisateur enregistré en tant que User currentUser
        User currentUser = new User(1, "username", "password"); // À remplacer par votre propre utilisateur


        // Créez un objet Sponsoring à partir de l'ID
        Sponsoring sponsoring = new Sponsoring( sponsoringId);

        // Créez un objet Post_group en spécifiant le Sponsoring et l'User
        Post_group postGroup = new Post_group(contenu, new Date(), sponsoring, currentUser);
        try {
            postgroupService.ajouter(postGroup);
            afficherPosts(); // Afficher les posts après l'ajout
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private String sponsoringName;

    public void setSponsoringName(String sponsoringName) {
        this.sponsoringName = sponsoringName;
        sponsoringLabel.setText(sponsoringName);
    }
    @FXML
    public void afficherPosts() {
        postbox.getChildren().clear();
        afficherPostsSponsoring(sponsoringLabel.getText());
    }
    private int sponsoringId;

    public void setSponsoringId(int sponsoringId) {
        this.sponsoringId = sponsoringId;
    }
    @FXML
    private void handlesponsorButtonClick() {
        try {
            // Charger le fichier FXML de la nouvelle page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/affichersponsoring.fxml"));
            Parent root = loader.load();

            // Créer la scène avec la nouvelle page
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir du bouton cliqué
            Stage stage = (Stage) buttonretour.getScene().getWindow();

            // Remplacer la scène actuelle par la nouvelle scène
            stage.setScene(scene);
            stage.show();



        } catch (Exception e) {
            e.printStackTrace();  // Gérer les exceptions en conséquence
        }
    }

}
