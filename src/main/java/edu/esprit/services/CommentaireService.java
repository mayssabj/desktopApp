package edu.esprit.services;

import edu.esprit.entities.Post_group;
import edu.esprit.entities.Postcommentaire;
import edu.esprit.entities.User;
import edu.esprit.utils.mydb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentaireService implements ServiceCommentaire<Postcommentaire>{
    private Connection con ;
    Statement ste ;
    public CommentaireService (){
        con = mydb.getInstance().getCon();
    }
    @Override
    public void ajouterCommentaire(Postcommentaire postcommentaire) throws SQLException {
        String req = "INSERT INTO `postcommentaire`( `commentaire`, `postgroup_id`, `user_id`) VALUES (?,?,?)";
        try (PreparedStatement com = con.prepareStatement(req)) {
            com.setString(1, postcommentaire.getCommentaire());
            com.setInt(2, postcommentaire.getPostgroup_id().getId());
            com.setInt(3, postcommentaire.getUser_id().getId());
            com.executeUpdate();
        }
    }



    @Override
    public void modifierCommentaire(int idCommentaire, String nouveauTexte) throws SQLException {
        // Préparer la requête SQL pour mettre à jour le commentaire
        String sql = "UPDATE postcommentaire SET commentaire = ? WHERE id = ?";

        // Créer un objet PreparedStatement pour exécuter la requête
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            // Définir les paramètres de la requête
            statement.setString(1, nouveauTexte);
            statement.setInt(2, idCommentaire);

            // Exécuter la mise à jour
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Commentaire mis à jour avec succès.");
            } else {
                System.out.println("Aucun commentaire trouvé avec l'ID fourni.");
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la mise à jour du commentaire: " + e.getMessage());
        }
    }


    @Override
    public void supprimerCommentaire(int id) throws SQLException {
        String req = "DELETE FROM `postcommentaire` WHERE `id`=?";
        try (PreparedStatement com = con.prepareStatement(req)) {
            com.setInt(1, id);
            com.executeUpdate();
        }
    }

    @Override
    public List<Postcommentaire> afficherCommentaire() throws SQLException {
        return null;
    }


    public User getUserById(int id) throws SQLException {
        String req = "SELECT * FROM `user` WHERE `id`=?";
        try (PreparedStatement com = con.prepareStatement(req)) {
            com.setInt(1, id);
            ResultSet rs = com.executeQuery();
            if (rs.next()) {
                String username = rs.getString("username");
                String image = rs.getString("image");
                return new User(id, username, image);
            }
        }
        return null;
    }





}