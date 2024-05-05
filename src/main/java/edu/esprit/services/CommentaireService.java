package edu.esprit.services;

import edu.esprit.entities.Post_group;
import edu.esprit.entities.Postcommentaire;
import edu.esprit.entities.User;
import edu.esprit.utils.mydb;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommentaireService implements ServiceCommentaire<Postcommentaire>{
    private Connection con ;
    Statement ste ;
    public CommentaireService (){
        con = mydb.getInstance().getCon();
    }

    @Override
    public void ajouterCommentaire(Postcommentaire postcommentaire) throws SQLException {
        String req = "INSERT INTO `postcommentaire`( `commentaire`, `postgroup_id`, `user_id`, `likes`, `liked_by`) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement com = con.prepareStatement(req)) {
            com.setString(1, postcommentaire.getCommentaire());
            com.setInt(2, postcommentaire.getPostgroup_id().getId());
            com.setInt(3, postcommentaire.getUser_id().getId());
            com.setInt(4, postcommentaire.getLikes());
            com.setString(5, postcommentaire.getLikedByUsersAsString()); // Serialize the liked by users list
            com.executeUpdate();
        }
    }




    @Override
    public void modifierCommentaire(int idCommentaire, String nouveauTexte) throws SQLException {
        String sql = "UPDATE postcommentaire SET commentaire = ? WHERE id = ?";

        // Start a transaction
        con.setAutoCommit(false);
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, nouveauTexte);
            statement.setInt(2, idCommentaire);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Commentaire mis à jour avec succès.");
                con.commit();  // Commit the transaction
            } else {
                System.out.println("Aucun commentaire trouvé avec l'ID fourni.");
                con.rollback();  // Rollback if no rows affected
            }
        } catch (SQLException e) {
            con.rollback();  // Rollback on error
            System.err.println("Erreur lors de la mise à jour du commentaire: " + e.getMessage());
            throw e;
        } finally {
            con.setAutoCommit(true);  // Reset auto-commit to true
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
        List<Postcommentaire> commentaires = new ArrayList<>();
        String req = "SELECT * FROM postcommentaire " ;
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("user_id"));
                Post_group post = new Post_group(rs.getInt("postgroup_id"), null);
                // This line fetches the likes count from the ResultSet, it should correctly reflect what's in your database
                Postcommentaire commentaire = new Postcommentaire(
                        rs.getInt("id"),
                        rs.getString("commentaire"),
                        post,
                        user,
                        rs.getInt("likes") // Make sure this is the correct column name in your database
                );

                commentaires.add(commentaire);
            }
        }
        return commentaires;
    }
    public List<Postcommentaire> afficherCommentaires(Post_group pst) throws SQLException {
        List<Postcommentaire> commentaires = new ArrayList<>();
        String req = "SELECT pc.*, u.email, p.contenu AS post_content FROM postcommentaire pc " +
                "JOIN user u ON pc.user_id = u.id " +
                "JOIN post_group p ON pc.postgroup_id = p.id " +
                "WHERE pc.postgroup_id = ?";

        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, pst.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String commentaireText = rs.getString("commentaire");
                // Other user and post attributes should be set appropriately
                User user = new User();
                user.setId(rs.getInt("comment_user_id"));
                user.setUsername(rs.getString("comment_user_email"));
                Post_group post = new Post_group(rs.getInt("postgroup_id"), rs.getString("post_content"));
                Postcommentaire commentaire = new Postcommentaire(
                        rs.getInt("id"),
                        rs.getString("commentaire"),
                        post, // Assume you have this object created correctly
                        user, // Assume you have this object created correctly
                        rs.getInt("likes") // This should be fetching the likes count from the ResultSet
                );
                // Set the likedByUsers list from the 'liked_by' column
                String likedBySerialized = rs.getString("liked_by");
                commentaire.setLikedByUsersFromString(Optional.ofNullable(likedBySerialized).orElse("[]"));
                commentaires.add(commentaire);
            }
        }
        return commentaires;
    }



    public User getUserById(int id) throws SQLException {
        String req = "SELECT * FROM `user` WHERE `id`=?";
        try (PreparedStatement com = con.prepareStatement(req)) {
            com.setInt(1, id);
            ResultSet rs = com.executeQuery();
            if (rs.next()) {
                String username = rs.getString("username");
                String image = rs.getString("image");
                return new User();
            }
        }
        return null;
    }
    public void likeComment(int commentId, int userId) throws SQLException {
        String sqlSelect = "SELECT liked_by, likes FROM postcommentaire WHERE id = ?";
        String sqlUpdate = "UPDATE postcommentaire SET liked_by = ?, likes = ? WHERE id = ?";

        try (PreparedStatement stmtSelect = con.prepareStatement(sqlSelect);
             PreparedStatement stmtUpdate = con.prepareStatement(sqlUpdate)) {

            // Select the comment to get current likes and liked_by list
            stmtSelect.setInt(1, commentId);
            ResultSet rs = stmtSelect.executeQuery();

            if (rs.next()) {
                String likedBy = rs.getString("liked_by");
                int currentLikes = rs.getInt("likes");
                List<Integer> likedByUsers = Arrays.stream(likedBy.replace("[", "").replace("]", "").split(","))
                        .filter(s -> !s.isEmpty())
                        .map(String::trim) // Add this line to trim the whitespace
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());


                // Determine whether to like or unlike based on current state
                boolean isAddingLike = !likedByUsers.contains(userId);
                if (isAddingLike) {
                    likedByUsers.add(userId);  // Add like
                    currentLikes++;  // Increment like count
                } else {
                    likedByUsers.remove(Integer.valueOf(userId));  // Remove like
                    currentLikes = Math.max(0, currentLikes - 1);  // Decrement like count, ensuring it doesn't go below zero
                }

                // Convert the list of user IDs back into a string for storage
                String likedByString = "[" + likedByUsers.stream().map(String::valueOf).collect(Collectors.joining(", ")) + "]";


                // Update the comment in the database with new likes and liked_by list
                stmtUpdate.setString(1, likedByString);
                stmtUpdate.setInt(2, currentLikes);
                stmtUpdate.setInt(3, commentId);
                stmtUpdate.executeUpdate();

            }
        }
    }

    public void unlikeComment(int commentId, int userId) throws SQLException {
        // Vous devez implémenter la logique pour décrémenter le compteur de likes
        // et retirer l'ID utilisateur de la liste des utilisateurs qui ont aimé.
        // Ceci est un exemple basique de la façon dont vous pourriez le faire :
        String sqlSelect = "SELECT liked_by FROM postcommentaire WHERE id = ?";
        String sqlUpdate = "UPDATE postcommentaire SET likes = GREATEST(likes - 1, 0), liked_by = ? WHERE id = ?";

        try (PreparedStatement stmtSelect = con.prepareStatement(sqlSelect);
             PreparedStatement stmtUpdate = con.prepareStatement(sqlUpdate)) {

            // Sélectionner le commentaire
            stmtSelect.setInt(1, commentId);
            ResultSet rs = stmtSelect.executeQuery();

            if (rs.next()) {
                String likedBy = rs.getString("liked_by");
                List<Integer> likedByUsers = Arrays.stream(likedBy.replace("[", "").replace("]", "").split(","))
                        .filter(s -> !s.isEmpty())
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());

                if (likedByUsers.remove(Integer.valueOf(userId))) {
                    // Convertir la liste d'IDs utilisateur en chaîne de caractères pour la stocker dans la base de données
                    String likedByString = "[" + String.join(",", likedByUsers.stream().map(String::valueOf).collect(Collectors.toList())) + "]";

                    // Mettre à jour le commentaire dans la base de données
                    stmtUpdate.setString(1, likedByString);
                    stmtUpdate.setInt(2, commentId);
                    stmtUpdate.executeUpdate();
                }
            }
        }
    }






}