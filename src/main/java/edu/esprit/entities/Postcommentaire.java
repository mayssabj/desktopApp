package edu.esprit.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Postcommentaire {
    private int id ;
    private String commentaire ;
    private Post_group postgroup_id ;
    private User user_id ;
    private int likes; // Supposons que cela représente le nombre total de likes
    private List<Integer> likedByUsers;


    public Postcommentaire(int id, String commentaire, Post_group postgroup_id, User user_id, int likes) {
        this.id = id;
        this.commentaire = commentaire;
        this.postgroup_id = postgroup_id;
        this.user_id = user_id;
        this.likes = likes;
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
    public Postcommentaire(int id, String commentaire, Post_group postgroup_id, User user_id) {
        this.id = id;
        this.commentaire = commentaire;
        this.postgroup_id = postgroup_id;
        this.user_id = user_id;
        this.likedByUsers = new ArrayList<>(); // Initialize the list
        // Set likes based on likedByUsers.size() if necessary
    }


    public Postcommentaire(String commentaire, Post_group postgroup_id, User user_id) {
        this.commentaire = commentaire;
        this.postgroup_id = postgroup_id;
        this.user_id = user_id;
        this.likes = 0; // Assume likes also needs initializing
        this.likedByUsers = new ArrayList<>(); // Initialize to an empty list
    }


    public Postcommentaire() {
        // Default constructor
        this.likedByUsers = new ArrayList<>(); // Initialize the list
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
    // Méthode pour définir la liste des utilisateurs qui ont aimé à partir d'une chaîne sérialisée
    public void setLikedByUsersFromString(String likedByString) {
        this.likedByUsers = Arrays.stream(likedByString.replace("[", "").replace("]", "").split(","))
                .filter(s -> !s.isEmpty()) // Évite les éléments vides si la chaîne est "[]"
                .map(String::trim) // Enlève les espaces blancs éventuels
                .map(Integer::valueOf) // Convertit en Integer
                .collect(Collectors.toList());
    }

    // Méthode pour obtenir la chaîne sérialisée à partir de la liste des utilisateurs qui ont aimé
    public String getLikedByUsersAsString() {
        return "[" + likedByUsers.stream().map(String::valueOf).collect(Collectors.joining(", ")) + "]";
    }

    // Ajoute un utilisateur à la liste des likes si ce n'est pas déjà fait
    public void like(int userId) {
        if (!likedByUsers.contains(userId)) {
            likedByUsers.add(userId);
            likes++; // Incrémente le compteur de likes
            // Vous devrez aussi mettre à jour la base de données ici.
        }
    }
    public void addLike(Integer userId) {
        if (likedByUsers == null) {
            likedByUsers = new ArrayList<>();
        }
        if (!likedByUsers.contains(userId)) {
            likedByUsers.add(userId);
            likes++;
        }
    }

    public void removeLike(Integer userId) {
        if (likedByUsers != null && likedByUsers.contains(userId)) {
            likedByUsers.remove(userId);
            likes = Math.max(0, likes - 1); // S'assurer que les likes ne vont pas en dessous de zéro
        }
    }




    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<Integer> getLikedByUsers() {
        return likedByUsers;
    }

    public void setLikedByUsers(List<Integer> likedByUsers) {
        this.likedByUsers = likedByUsers;
    }


}