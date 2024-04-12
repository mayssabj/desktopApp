package edu.esprit.controller;

import edu.esprit.entities.Post_group;
import edu.esprit.entities.Postcommentaire;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        setSponsoringName("Nom du Sponsoring");
        afficherPostsSponsoring(sponsoringName);
    }


    public void afficherPostsSponsoring(String sponsoringName) {
        postbox.getChildren().clear();
        try {
            List<Post_group> posts = postgroupService.afficherBySponsoring(sponsoringName);
            for (Post_group post : posts) {
                User user = post.getUser_id();
                if (user != null) {
                    Label userNameLabel = new Label(user.getUsername());

                    String imageUrl = user.getImage();
                    Image image = new Image(new FileInputStream(imageUrl));
                    ImageView userImageView = new ImageView(image);
                    userImageView.setFitWidth(20);
                    userImageView.setFitHeight(20);

                    Label contenuLabel = new Label(post.getContenu());
                    HBox userInfo = new HBox(userImageView, userNameLabel);
                    userInfo.setSpacing(10); // Espace entre l'image et le nom de l'utilisateur

                    // Boutons de suppression et d'édition
                    Image deleteIcon = new Image(getClass().getResourceAsStream("/images/delete.png"));
                    ImageView deleteImageView = new ImageView(deleteIcon);
                    deleteImageView.setFitWidth(15);
                    deleteImageView.setFitHeight(15);
                    Button deleteButton = new Button("", deleteImageView);

                    Image editIcon = new Image(getClass().getResourceAsStream("/images/edit.png"));
                    ImageView editImageView = new ImageView(editIcon);
                    editImageView.setFitWidth(20);
                    editImageView.setFitHeight(20);
                    Button editButton = new Button("", editImageView);

                    deleteButton.setOnAction(event -> supprimerPostgroup(post));
                    editButton.setOnAction(event -> modifierPostgroup(post));

                    HBox postControls = new HBox(deleteButton, editButton);
                    postControls.setSpacing(10); // Espace entre les boutons

                    VBox postDetails = new VBox(userInfo, contenuLabel, postControls);
                    postDetails.setSpacing(5); // Espace entre les éléments du post

                    // Commentaires et champ pour ajouter un commentaire
                    VBox commentairesBox = new VBox();
                    for (Postcommentaire commentaire : post.getCommentaires()) {
                        Label commentaireLabel = new Label(commentaire.getCommentaire());
                        commentairesBox.getChildren().add(commentaireLabel);
                    }

                    TextField commentaireField = new TextField();
                    commentaireField.setPromptText("Add comment...");
                    Button commenterButton = new Button("Commenter");
                    commenterButton.setOnAction(event -> ajouterCommentaire(post, commentaireField.getText()));
                    commentairesBox.getChildren().addAll(commentaireField, commenterButton);

                    // Ajout de tout à un VBox principal pour ce post
                    VBox postContainer = new VBox(postDetails, commentairesBox);
                    postContainer.getStyleClass().add("post-card");
                    postContainer.setSpacing(10); // Espace entre les détails du post et les commentaires

                    // Ajouter le conteneur du post à la liste principale des posts
                    postbox.getChildren().add(postContainer);
                }
            }
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void ajouterCommentaire(Post_group post, String commentaireText) {
        User currentUser = new User(1, "username", "password"); // À remplacer par votre propre utilisateur
        Postcommentaire commentaire = new Postcommentaire(commentaireText, post, currentUser);
        try {
            postgroupService.ajouterCommentaire(commentaire); // Enregistrer le commentaire dans la base de données
            post.addCommentaire(commentaire); // Ajouter le commentaire au post
            afficherPostsSponsoring(sponsoringName); // Réafficher les posts après l'ajout du commentaire
        } catch (SQLException e) {
            e.printStackTrace();
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

    @FXML
    public void supprimerPostgroup(Post_group post) {
        User currentUser = new User(1, "username", "password");
        // Vérifier si l'utilisateur actuel est l'auteur du post
        if (post.getUser_id().getId() == currentUser.getId()) {
            try {
                postgroupService.supprimer(post.getId()); // Appeler la méthode supprimer avec l'ID du post à supprimer
                afficherPostsSponsoring(sponsoringName); // Réafficher les posts après la suppression
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            System.out.println("Vous n'êtes pas autorisé à supprimer ce post.");
        }
    }

    @FXML
    public void modifierPostgroup(Post_group post) {
        User currentUser = new User(1, "username", "password");
        // Vérifier si l'utilisateur actuel est l'auteur du post
        if (post.getUser_id().getId() == currentUser.getId()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierPostgroup.fxml"));
                Parent root = loader.load();

                modifierPostgroup controller = loader.getController();
                controller.initData(post); // Passer le post à modifier au contrôleur de la fenêtre de modification

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Afficher un message d'erreur indiquant que l'utilisateur n'est pas autorisé à modifier ce post
            System.out.println("Vous n'êtes pas autorisé à modifier ce post.");
        }
    }





}
