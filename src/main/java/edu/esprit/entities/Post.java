package edu.esprit.entities;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;

public class Post {
    @Id
    private int id;
    private String titre;
    private String description;
    private String imageUrl;
    private Date date;
    private Type type;
    private String place;

    public enum Type {
        Lost, Found
    }

    public int user_id;

    public Post(int id, String titre, String description, String imageUrl, Date date, Type type, String place ,int user_id) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.imageUrl = imageUrl;
        this.date = date;
        this.type = type;
        this.place = place;
        this.user_id = user_id;
    }
    public Post( String titre, String description, String imageUrl, Type type, String place, int user_id) {
        this.titre = titre;
        this.description = description;
        this.imageUrl = imageUrl;
        this.type = type;
        this.place = place;
        this.user_id = user_id;
    }

    public Post(String titre, String description, String imageUrl, Date date, Type type, String place, int user_id) {
        this.titre = titre;
        this.description = description;
        this.imageUrl = imageUrl;
        this.date = date;
        this.type = type;
        this.place = place;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getuser_id() {
        return user_id;
    }

    public void setuser_id(int user_id) {
        this.user_id = user_id;
    }

    public int user_id() {
        return user_id;
    }

    public void user_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", date=" + date +
                ", type=" + type +
                ", place='" + place + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}