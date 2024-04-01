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
    public void modifierCommentaire(Postcommentaire postcommentaire) throws SQLException {
        String req = "UPDATE `postcommentaire` SET `commentaire`=?,`postgroup_id`=?,`user_id`=? WHERE `id`=?";
        try (PreparedStatement com = con.prepareStatement(req)) {
            com.setString(1, postcommentaire.getCommentaire());
            com.setInt(2, postcommentaire.getPostgroup_id().getId());
            com.setInt(3, postcommentaire.getUser_id().getId());
            com.setInt(4, postcommentaire.getId());
            com.executeUpdate();
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
        List<Postcommentaire> commentairesList = new ArrayList<>();
        String req = "SELECT * FROM `postcommentaire`";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String commentaire = rs.getString("commentaire");
                int postgroupId = rs.getInt("postgroup_id");
                int userId = rs.getInt("user_id");
                Post_group postgroup = getPostgroupById(postgroupId);
                User user = getUserById(userId);
                Postcommentaire comm = new Postcommentaire(id, commentaire, postgroup, user);
                commentairesList.add(comm);
            }
        }
        return commentairesList;
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

    public Post_group getPostgroupById(int id) throws SQLException {
        String req = "SELECT * FROM `post_group` WHERE `id`=?";
        try (PreparedStatement com = con.prepareStatement(req)) {
            com.setInt(1, id);
            ResultSet rs = com.executeQuery();
            if (rs.next()) {
                String contenu = rs.getString("contenu");
                return new Post_group(id, contenu);
            }
        }
        return null;
    }
}