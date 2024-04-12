package edu.esprit.services;

import edu.esprit.entities.Post_group;
import edu.esprit.entities.Postcommentaire;
import edu.esprit.entities.Sponsoring;
import edu.esprit.entities.User;
import edu.esprit.utils.mydb;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostgroupService implements ServicePostgroup<Post_group> {
    private Connection con ;
    Statement ste ;
    public PostgroupService (){
        con = mydb.getInstance().getCon();
    }
    @Override
    public void ajouter(Post_group postGroup) throws SQLException {
        if (postGroup.getSponsoring_id() == null) {
            throw new IllegalArgumentException("Sponsoring ID cannot be null");
        }

        String req = "INSERT INTO `post_group`(`contenu`, `date`, `sponsoring_id`, `user_id`) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pg = con.prepareStatement(req)) {
            pg.setString(1, postGroup.getContenu());
            pg.setDate(2, new java.sql.Date(postGroup.getDate().getTime()));
            pg.setInt(3, postGroup.getSponsoring_id().getId());
            pg.setInt(4, postGroup.getUser_id().getId());
            pg.executeUpdate();
        }
    }

    @Override
    public void modifier(Post_group postGroup) throws SQLException {
        String req = "UPDATE `post_group` SET `contenu`=?,`date`=?,`sponsoring_id`=?,`user_id`=? WHERE `id`=?";
        try (PreparedStatement pg = con.prepareStatement(req)) {
            pg.setString(1, postGroup.getContenu());
            pg.setDate(2, new java.sql.Date(postGroup.getDate().getTime()));
            pg.setInt(3, postGroup.getSponsoring_id().getId());
            pg.setInt(4, postGroup.getUser_id().getId());
            pg.setInt(5, postGroup.getId());
            pg.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM `post_group` WHERE `id`=?";
        try (PreparedStatement pg = con.prepareStatement(req)) {
            pg.setInt(1, id);
            pg.executeUpdate();
        }
    }

    @Override
    public List<Post_group> afficher() throws SQLException {
        List<Post_group> posts = new ArrayList<>();
        String req = "SELECT * FROM `post_group`";
        try (PreparedStatement pg = con.prepareStatement(req)) {
            ResultSet rs = pg.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String contenu = rs.getString("contenu");
                Date date = rs.getDate("date");
                int sponsoringId = rs.getInt("sponsoring_id");
                int userId = rs.getInt("user_id");
                Sponsoring sponsoring = getSponsoringById(sponsoringId);
                User user = getUserById(userId);
                Post_group post = new Post_group(id, contenu,date, sponsoring,user);
                posts.add(post);
            }
        }
        return posts;
    }

    public Sponsoring getSponsoringById(int id) throws SQLException {
        String req = "SELECT * FROM `sponsoring` WHERE `id`=?";
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String sponsoringName = rs.getString("name");
                return new Sponsoring(id, sponsoringName);
            }
        }
        return null;
    }

    public User getUserById(int id) throws SQLException {
        String req = "SELECT * FROM `user` WHERE `id`=?";
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String username = rs.getString("username");
                String image = rs.getString("image");
                return new User(id, username, image); // Assuming User constructor takes id, username, and image
            }
        }
        return null;
    }

    public List<Post_group> afficherBySponsoring(String sponsoringName) throws SQLException {
        List<Post_group> posts = new ArrayList<>();
        String req = "SELECT pg.*, pc.id AS comment_id, pc.commentaire FROM post_group pg "
                + "LEFT JOIN postcommentaire pc ON pg.id = pc.postgroup_id "
                + "WHERE pg.sponsoring_id IN (SELECT id FROM sponsoring WHERE name = ?)";
        try (PreparedStatement pg = con.prepareStatement(req)) {
            pg.setString(1, sponsoringName);
            ResultSet rs = pg.executeQuery();
            Map<Integer, Post_group> postsMap = new HashMap<>();
            while (rs.next()) {
                int postId = rs.getInt("id");
                Post_group post = postsMap.getOrDefault(postId,
                        new Post_group(postId, rs.getString("contenu"), rs.getDate("date"), getSponsoringById(rs.getInt("sponsoring_id")), getUserById(rs.getInt("user_id"))));
                int commentId = rs.getInt("comment_id");
                if (commentId != 0) { // Check if there is a comment
                    Postcommentaire comment = new Postcommentaire(commentId, rs.getString("commentaire"), post, post.getUser_id());
                    post.addCommentaire(comment);
                }
                postsMap.putIfAbsent(postId, post);
            }
            posts.addAll(postsMap.values());
        }
        return posts;
    }

    public void ajouterCommentaire(Postcommentaire commentaire) throws SQLException {
        String req = "INSERT INTO `postcommentaire`(`commentaire`, `postgroup_id`, `user_id`) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setString(1, commentaire.getCommentaire());
            ps.setInt(2, commentaire.getPostgroup_id().getId());
            ps.setInt(3, commentaire.getUser_id().getId());
            ps.executeUpdate();
        }
    }



}