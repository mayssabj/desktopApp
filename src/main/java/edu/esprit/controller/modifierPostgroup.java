package edu.esprit.controller;

import edu.esprit.Api.ProfanityFilter;
import edu.esprit.entities.Post_group;
import edu.esprit.services.PostgroupService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class modifierPostgroup {
    @FXML
    private TextField contenuField; // Supposons que cela correspond au champ de contenu dans votre interface utilisateur

    private Post_group post; // Le post à modifier
    private PostgroupService postgroupService;

    public modifierPostgroup() {
        postgroupService = new PostgroupService();
    }

    public void initData(Post_group post) {
        this.post = post;
        contenuField.setText(post.getContenu());
        // Pré-remplir le champ de contenu avec le contenu actuel du post
    }


    @FXML
    public void enregistrerModification() {
        String contenuFiltre = contenuField.getText();
        ProfanityFilter filter = new ProfanityFilter();
        if (filter.containsProfanity(contenuFiltre)) {
            contenuFiltre = filter.filterContent(contenuFiltre);
        }

        // Mettre à jour le contenu du post avec le contenu filtré
        post.setContenu(contenuFiltre);

        try {
            // Appeler le service pour modifier le post dans la base de données
            postgroupService.modifier(post);
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les erreurs de modification
            // Afficher un message d'erreur à l'utilisateur ici
        }
    }

}
