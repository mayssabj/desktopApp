package edu.esprit.entities;


import java.util.Date;

public class Post_group {
    private int id ;
    private String contenu ;
    private Date date ;
    private Sponsoring sponsoring_id;
    private User user_id ;

    public Post_group(int id, String contenu,Date date , Sponsoring sponsoring_id , User user_id) {
        this.id = id;
        this.contenu = contenu;
        this.date = date;
        this.sponsoring_id = sponsoring_id;
        this.user_id = user_id;
    }

    public Post_group(String contenu, Date date, Sponsoring sponsoring_id, User user_id) {
        this.contenu = contenu;
        this.date = date;
        this.sponsoring_id = sponsoring_id;
        this.user_id = user_id;
    }

    public Post_group(int id, String contenu) {
        this.id = id;
        this.contenu = contenu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Sponsoring getSponsoring_id() {
        return sponsoring_id;
    }

    public void setSponsoring_id(Sponsoring sponsoring_id) {
        this.sponsoring_id = sponsoring_id;
    }

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Post_group{" +
                "id=" + id +
                ", contenu='" + contenu + '\'' +
                ", date=" + date +
                ", sponsoring_id=" + sponsoring_id +
                ", user_id=" + user_id +
                '}';
    }


}

