package edu.esprit.entities;

import java.util.Date;


public class Sponsoring {
    private int id ;
    private String name ;
    private String description ;
    private String image ;
    private Date date ;
    private Duration contrat ;
    private TypeSpon type ;


    public enum TypeSpon {
        ACTIVE , DESACTIVE
    }
    public enum Duration {
        ONE_YEAR, TWO_YEARS , TREE_YEARS
    }

    public Sponsoring(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Sponsoring(int id, String name, String description, String image, Date date, Duration contrat, TypeSpon type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.date = date;
        this.contrat = contrat;
        this.type = type;
    }

    public Sponsoring(String name, String description, String image, Date date, Duration contrat, TypeSpon type) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.date = date;
        this.contrat = contrat;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Duration getContrat() {
        return contrat;
    }

    public void setContrat(Duration contrat) {
        this.contrat = contrat;
    }

    public TypeSpon getType() {
        return type;
    }

    public void setType(TypeSpon type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Sponsoring{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", date=" + date +
                ", contrat='" + contrat + '\'' +
                ", type=" + type +
                '}';
    }
}
