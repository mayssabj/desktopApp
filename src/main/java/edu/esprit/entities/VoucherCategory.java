package edu.esprit.entities;

public class VoucherCategory {

    private int id;
    private String titre;
    private String description;

    public VoucherCategory() {
    }

    // Constructor with parameters
    public VoucherCategory(int id, String titre, String description) {
        this.id = id;
        this.titre = titre;
        this.description = description;
    }

    public VoucherCategory(String titre, String description) {
        this.titre = titre;
        this.description = description;
    }

    // Getters and setters
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

    // toString method
    @Override
    public String toString() {
        return "VoucherCategory{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}