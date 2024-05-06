package edu.esprit.entities;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Postcommentaire {
    private int id;
    private String commentaire;
    private Post_group postgroup_id;
    private User user_id;
    private int likes;
    private Map<Integer, Integer> likedByUsers; // Using Map to simulate the "{key:value}" structure

    public Postcommentaire() {
        this.likedByUsers = new LinkedHashMap<>();
    }

    public Postcommentaire(int id, String commentaire, Post_group postgroup_id, User user_id, int likes) {
        this.id = id;
        this.commentaire = commentaire;
        this.postgroup_id = postgroup_id;
        this.user_id = user_id;
        this.likes = likes;
        this.likedByUsers = new LinkedHashMap<>();
    }
    public Postcommentaire(String commentText, Post_group post, User currentUser){
        this.commentaire=commentText;
        this.postgroup_id=post;
        this.user_id=currentUser;
    }

    // Add user to likedByUsers and increment likes
    public void addLike(Integer userId) {
        if (!likedByUsers.containsKey(userId)) {
            likedByUsers.put(userId, userId);
            likes++;
        }
    }

    // Remove user from likedByUsers and decrement likes
    public void removeLike(Integer userId) {
        if (likedByUsers.containsKey(userId)) {
            likedByUsers.remove(userId);
            likes = Math.max(0, likes - 1);
        }
    }

    // Converts the Map to a string that mimics PHP serialized array format
    public String getLikedByUsersAsString() {
        return "{" + likedByUsers.keySet().stream()
                .map(key -> key + ":" + likedByUsers.get(key))
                .collect(Collectors.joining(", ")) + "}";
    }

    // Parses a string in a specific format and converts it into the Map
    public void setLikedByUsersFromString(String likedByString) {
        this.likedByUsers.clear();
        if (likedByString != null && !likedByString.equals("{}")) {
            likedByString = likedByString.substring(1, likedByString.length() - 1); // Remove "{" and "}"
            String[] pairs = likedByString.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    int key = Integer.parseInt(keyValue[0].trim());
                    int value = Integer.parseInt(keyValue[1].trim());
                    likedByUsers.put(key, value);
                }
            }
        }
    }

    // Getters and Setters
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Map<Integer, Integer> getLikedByUsers() {
        return likedByUsers;
    }

    public void setLikedByUsers(Map<Integer, Integer> likedByUsers) {
        this.likedByUsers = likedByUsers;
    }
}
