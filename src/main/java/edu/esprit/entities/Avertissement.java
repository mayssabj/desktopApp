package edu.esprit.entities;

public class Avertissement {
    private int id;
    private String reported_username;
    private int confirmation;
    private String screenshot;
    private String raison;
    public Avertissement() {
    }

    public Avertissement(int id, String reported_username, int confirmation, String screenshot, String raison) {
        this.id = id;
        this.reported_username = reported_username;
        this.confirmation = confirmation;
        this.screenshot = screenshot;
        this.raison = raison;
    }

    public Avertissement(String reported_username, int confirmation, String screenshot, String raison) {
        this.reported_username = reported_username;
        this.confirmation = confirmation;
        this.screenshot = screenshot;
        this.raison = raison;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReported_username() {
        return reported_username;
    }

    public void setReported_username(String reported_username) {
        this.reported_username = reported_username;
    }

    public int getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(int confirmation) {
        this.confirmation = confirmation;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

}
