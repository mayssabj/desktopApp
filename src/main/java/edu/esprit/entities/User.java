package edu.esprit.entities;

public class User {
    private int id ;
    private String username ;
    private String image ;
    public User(int id, String username, String image) {
        this.id = id;
        this.username = username;
        this.image = image;
    }

    public User(String username, String image) {
        this.username = username;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

}
