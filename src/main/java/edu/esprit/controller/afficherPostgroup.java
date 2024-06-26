package edu.esprit.controller;

import edu.esprit.Api.ProfanityFilter;
import edu.esprit.entities.*;
import edu.esprit.services.CommentaireService;
import edu.esprit.services.PostgroupService;
import edu.esprit.services.UserService;
import edu.esprit.utils.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
                UserService userserv=new UserService();
                    User user = userserv.getUserById(post.getUser_id());
                if (user != null) {
                    Label userNameLabel = new Label(user.getUsername());
                    ImageView userImageView = new ImageView();

                    Image profileImage;

                    if (user.getPhoto() != null && !user.getPhoto().isEmpty()) {
                        profileImage = new Image(new URL(user.getPhoto()).openStream());

                    }else{
                        profileImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/default_user.png")));
                    }
                    userImageView.setImage(profileImage);
                    userImageView.setFitWidth(20);
                    userImageView.setFitHeight(20);

                    Label contenuLabel = new Label(post.getContenu());
                    HBox userInfo = new HBox(userImageView, userNameLabel);
                    userInfo.setSpacing(10);

                    HBox postControls = createPostControls(post);
                    VBox postDetails = new VBox(userInfo, contenuLabel, postControls);
                    postDetails.setSpacing(10);


                    VBox commentairesBox = createCommentsBox(post);
                    VBox postContainer = new VBox(postDetails, commentairesBox);
                    postContainer.getStyleClass().add("post-card");
                    postContainer.setSpacing(10);

                    postbox.getChildren().add(postContainer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private HBox createPostControls(Post_group post) {
        UserService userService = new UserService();
        User currentUser = Session.getInstance().getCurrentUser();

        HBox postControls = new HBox();
        postControls.setSpacing(10);
        postControls.setAlignment(Pos.CENTER_RIGHT);




            // Ajouter des boutons uniquement si l'utilisateur actuel est l'auteur du post
        if ((post.getUser_id()==currentUser.getId())) {
            Image deleteIcon = new Image(getClass().getResourceAsStream("/images/delete.png"));
            ImageView deleteImageView = new ImageView(deleteIcon);
            deleteImageView.setFitWidth(15);
            deleteImageView.setFitHeight(15);
            Button deleteButton = new Button("", deleteImageView);
            deleteButton.setOnAction(event -> supprimerPostgroup(post));

            Image editIcon = new Image(getClass().getResourceAsStream("/images/edit.png"));
            ImageView editImageView = new ImageView(editIcon);
            editImageView.setFitWidth(20);
            editImageView.setFitHeight(20);
            Button editButton = new Button("", editImageView);

            editButton.setOnAction(event -> modifierPostgroup(post));

            postControls.getChildren().addAll(deleteButton, editButton);
        }

        return postControls;
    }
    private VBox createCommentsBox(Post_group post) throws IOException {
        VBox commentairesBox = new VBox();
        commentairesBox.setSpacing(8);// Espacement entre les commentaires

        UserService userService = new UserService();
        User currentUser = Session.getInstance().getCurrentUser();  // Récupérer l'utilisateur actuel

        for (Postcommentaire commentaire : post.getCommentaires()) {
            User user = commentaire.getUser_id();
            ImageView commentUserImageView =new ImageView();

            Image profileImage;

            if (user.getPhoto() != null && !user.getPhoto().isEmpty()) {
                profileImage = new Image(new URL(user.getPhoto()).openStream());

            }else{
                profileImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/default_user.png")));
            }

            commentUserImageView.setImage(profileImage);
            commentUserImageView.setFitWidth(20);
            commentUserImageView.setFitHeight(20);


            // Supposé déjà configuré
            Label commentUserNameLabel = new Label(commentaire.getUser_id().getUsername());
            Label commentaireLabel = new Label(commentaire.getCommentaire());
            Label likesLabel = new Label("Likes: " + commentaire.getLikes());
            System.out.println("Likes from DB for comment ID " + commentaire.getId() + ": " + commentaire.getLikes());
            likesLabel.getStyleClass().add("likes-label"); // Optionally add a CSS class for styling

            Image like = new Image(getClass().getResourceAsStream("/images/like.png"));
            ImageView likeImageView = new ImageView(like);
            likeImageView.setFitWidth(20);
            likeImageView.setFitHeight(20);
            Button likeButton = new Button("", likeImageView);


            likeButton.setOnAction(event -> {
                onLikeButtonClicked(commentaire);
                likesLabel.setText("Likes: " + commentaire.getLikes()); // Update likes count on UI
            });

            HBox commentItem = new HBox(commentUserImageView, commentUserNameLabel, commentaireLabel, likesLabel, likeButton);
            commentItem.setSpacing(10);
            commentItem.setPadding(new Insets(5)); // Ajoute des marges internes


            // Ajouter des boutons uniquement si l'utilisateur actuel est l'auteur du commentaire
            if (commentaire.getUser_id() != null  && commentaire.getUser_id().getId()==(currentUser.getId())) {

                Image delete = new Image(getClass().getResourceAsStream("/images/delete.png"));
                ImageView deleteImageView = new ImageView(delete);
                deleteImageView.setFitWidth(20);
                deleteImageView.setFitHeight(20);
                Button deleteButton = new Button("", deleteImageView);


                deleteButton.setOnAction(event -> supprimerCommentaire(commentaire));

                Image edit = new Image(getClass().getResourceAsStream("/images/edit.png"));
                ImageView editImageView = new ImageView(edit);
                editImageView.setFitWidth(20);
                editImageView.setFitHeight(20);
                Button editButton = new Button("", editImageView);



                editButton.setOnAction(event -> modifierCommentaire(commentaire));



                commentItem.getChildren().addAll(deleteButton, editButton);

            }

            commentairesBox.getChildren().add(commentItem);

            // Ajouter un séparateur entre les commentaires sauf pour le dernier
            if (commentaire != post.getCommentaires().get(post.getCommentaires().size() - 1)) {
                Separator separator = new Separator();
                separator.setPadding(new Insets(5, 0, 5, 0));
                commentairesBox.getChildren().add(separator);
            }
        }

        // Ajouter un champ de texte et un bouton pour les nouveaux commentaires
        TextField newCommentField = new TextField();
        newCommentField.setPromptText("Add a comment...");


        Button commentButton = new Button("Post Comment");
        commentButton.setOnAction(event -> addComment(post, newCommentField.getText()));

        HBox commentInputBox = new HBox(newCommentField, commentButton);
        commentInputBox.setSpacing(10);
        commentInputBox.setPadding(new Insets(10));
        commentairesBox.getChildren().add(commentInputBox);

            return commentairesBox;
    }



    private void addComment(Post_group post, String commentText) {
        if (!commentText.isEmpty()) {
            // Initialiser le filtre de profanité
            ProfanityFilter filter = new ProfanityFilter();
            if (filter.containsProfanity(commentText)) {
                commentText = filter.filterContent(commentText);  // Filtrer le contenu s'il contient des mots indésirables
            }
            // Assuming UserService can give us the current user
            UserService userService = new UserService();
            User currentUser = Session.getInstance().getCurrentUser();
            Postcommentaire newComment = new Postcommentaire(commentText, post, currentUser);

            // Add the comment through your service layer
            try {
                postgroupService.ajouterCommentaire(newComment);
                afficherPostsSponsoring(sponsoringName);
                // Refresh comments display or handle UI update
                System.out.println("Comment added successfully!");
            } catch (SQLException e) {
                System.out.println("Failed to add comment: " + e.getMessage());
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Erreur de Saisie"); alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir du contenu ");
            alert.showAndWait();
        }


    }

    public void supprimerCommentaire(Postcommentaire commentaire) {
        UserService userService = new UserService();
        User currentUser = Session.getInstance().getCurrentUser();
        CommentaireService commentaireService = new CommentaireService();
        try {
            // Vérifier si l'utilisateur actuel est l'auteur du commentaire
            if (commentaire.getUser_id().getId()==currentUser.getId()) {
                commentaireService.supprimerCommentaire(commentaire.getId());
                afficherPostsSponsoring(sponsoringName); // Réafficher les posts après la suppression
            } else {
                System.out.println("Vous n'êtes pas autorisé à supprimer ce commentaire.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void modifierCommentaire(Postcommentaire commentaire) {
        UserService userService = new UserService();
        User currentUser = Session.getInstance().getCurrentUser();

        // Vérifier si l'utilisateur actuel est autorisé à modifier le commentaire
        if (commentaire.getUser_id() != null && commentaire.getUser_id().getEmail() != null && commentaire.getUser_id().getEmail().equals(currentUser.getEmail())) {
            System.out.println("Vous n'êtes pas autorisé à modifier ce commentaire.");
            return;
        }

        // Créer et configurer le TextInputDialog pour modifier le commentaire
        TextInputDialog dialog = new TextInputDialog(commentaire.getCommentaire());
        dialog.setTitle("Modifier Commentaire");
        dialog.setHeaderText("Modification du commentaire");
        dialog.setContentText("Entrez le nouveau texte du commentaire:");

        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
        Optional<String> result = dialog.showAndWait();

        // Traiter la réponse
        result.ifPresent(nouveauTexte -> {
            // Filtrer le nouveau texte pour la profanité avant de sauvegarder
            ProfanityFilter filter = new ProfanityFilter();
            String filteredText = nouveauTexte;
            if (filter.containsProfanity(nouveauTexte)) {
                filteredText = filter.filterContent(nouveauTexte);
            }

            try {
                // Mettre à jour le commentaire dans la base de données avec le texte filtré
                CommentaireService commentaireService = new CommentaireService();
                commentaireService.modifierCommentaire(commentaire.getId(), filteredText);
                afficherPostsSponsoring(sponsoringName); // Réafficher les posts après la modification
                System.out.println("Commentaire modifié avec succès !");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Échec de la modification du commentaire : " + e.getMessage());
            }
        });
    }





    @FXML
    public void ajouterPostgroup() {
        String contenu = sasie1.getText();

        if (contenu.isEmpty()) {
            // Afficher un message d'erreur à l'utilisateur s'il n'a pas saisi de contenu
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de Saisie");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir du contenu avant d'ajouter un nouveau post.");
            alert.showAndWait();
        } else {
            // Initialiser le filtre de profanité
            ProfanityFilter filter = new ProfanityFilter();
            if (filter.containsProfanity(contenu)) {
                contenu = filter.filterContent(contenu);  // Filtrer le contenu s'il contient des mots indésirables
            }

            // Supposer qu'un utilisateur est déjà enregistré en tant que User currentUser
            UserService userService = new UserService();
            User u1 = Session.getInstance().getCurrentUser(); // À remplacer par votre propre utilisateur

            // Créer un objet Sponsoring à partir de l'ID
            Sponsoring sponsoring = new Sponsoring(sponsoringId);

            // Créer un objet Post_group en spécifiant le Sponsoring et l'User
            Post_group postGroup = new Post_group(contenu, new Date(), sponsoring, u1.getId());
            try {
                postgroupService.ajouter(postGroup);
                afficherPosts(); // Afficher les posts après l'ajout
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashb.fxml"));
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
        UserService userService = new UserService();
        User currentUser = Session.getInstance().getCurrentUser();
        // Vérifier si l'utilisateur actuel est l'auteur du post
        if ((post.getUser_id()==currentUser.getId())) {
            try {
                postgroupService.supprimer(post.getId()); // Appeler la méthode supprimer avec l'ID du post à supprimer
                afficherPostsSponsoring(sponsoringName); // Réafficher les posts après la suppression
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            System.out.println("Vous n'êtes pas autorisé à supprimer ce post.");
            System.out.println("ID de l'utilisateur actuel : " + currentUser.getId());
            System.out.println("ID de l'utilisateur associé au post : " + post.getUser_id());
        }
    }

    @FXML
    public void modifierPostgroup(Post_group post) {
        UserService userService = new UserService();
        User currentUser = Session.getInstance().getCurrentUser();
        // Vérifier si l'utilisateur actuel est l'auteur du post
        if (post.getUser_id()==currentUser.getId()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierPostgroup.fxml"));
                Parent root = loader.load();

                modifierPostgroup controller = loader.getController();
                controller.initData(post);
                // Passer le post à modifier au contrôleur de la fenêtre de modification

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setOnHidden(e ->afficherPostsSponsoring(sponsoringName));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Afficher un message d'erreur indiquant que l'utilisateur n'est pas autorisé à modifier ce post
            System.out.println("Vous n'êtes pas autorisé à modifier ce post.");
            System.out.println("ID de l'utilisateur actuel : " + currentUser.getId());
            System.out.println("ID de l'utilisateur associé au post : " + post.getUser_id());
        }
    }

    public void onLikeButtonClicked(Postcommentaire commentaire) {
        UserService userService = new UserService();
        User currentUser = Session.getInstance().getCurrentUser();

        CommentaireService commentaireService = new CommentaireService();
        try {
            commentaireService.likeComment(commentaire.getId(), currentUser.getId());
            afficherPostsSponsoring(sponsoringName);

            // Mise à jour de l'interface utilisateur ici
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'erreur ici (afficher un message à l'utilisateur, par exemple)
        }
    }







}