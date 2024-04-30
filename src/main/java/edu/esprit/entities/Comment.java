package edu.esprit.entities;

import javax.persistence.*;

public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String text;

    private User id_u;

    public User getId_u() {
        return id_u;
    }

    public void setId_u(User id_u) {
        this.id_u = id_u;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post")
    private Post post;

    public Comment(int id, String text, Post post,User id_u) {
        this.id = id;
        this.text = text;
        this.post = post;
        this.id_u = id_u;
    }

    public Comment(String text, Post post,User id_u) {
        this.text = text;
        this.post = post;
        this.id_u = id_u;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", post=" + post.getTitre() + // Assuming you want to display post title
                '}';
    }
}
