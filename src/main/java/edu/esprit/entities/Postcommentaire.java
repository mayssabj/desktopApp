package edu.esprit.entities;

import java.util.ArrayList;

public class Postcommentaire {
    private int id ;
    private String commentaire ;
    private Post_group postgroup_id ;
    private User user_id ;


    public Postcommentaire(int id, String commentaire, Post_group postgroup_id, User user_id) {
        this.id = id;
        this.commentaire = commentaire;
        this.postgroup_id = postgroup_id;
        this.user_id = user_id;

    }

    public Postcommentaire(String commentaire, Post_group postgroup_id, User user_id) {
        this.commentaire = commentaire;
        this.postgroup_id = postgroup_id;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Post_group getPostgroup_id() {
        return postgroup_id;
    }

    public void setPostgroup_id(Post_group postgroup_id) {
        this.postgroup_id = postgroup_id;
    }

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Postcommentaire{" +
                "id=" + id +
                ", commentaire='" + commentaire + '\'' +
                ", postgroup_id=" + postgroup_id +
                ", user_id=" + user_id +
                '}';
    }
}
