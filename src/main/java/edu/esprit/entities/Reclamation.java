package edu.esprit.entities;




    public class Reclamation {
        private int id;
        private String subjuct;
        private String description;
        private String reported_username;
        private String type_reclamation;
        private String screenshot;

        public Reclamation() {
        }

        public Reclamation(int id, String subjuct, String description, String reported_username, String type_reclamation, String screenshot) {
        this.id = id;
        this.subjuct = subjuct;
        this.description = description;
        this.reported_username = reported_username;
        this.type_reclamation = type_reclamation;
        this.screenshot = screenshot;

    }
    public Reclamation( String subjuct, String description, String reported_username, String type_reclamation, String screenshot) {

        this.subjuct = subjuct;
        this.description = description;
        this.reported_username = reported_username;
        this.type_reclamation = type_reclamation;
        this.screenshot = screenshot;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubjuct() {
        return subjuct;
    }

    public void setSubjuct(String subjuct) {
        this.subjuct = subjuct;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReported_username() {
        return reported_username;
    }

    public void setReported_username(String reported_username) {
        this.reported_username = reported_username;
    }

    public String getType_reclamation() {
        return type_reclamation;
    }

    public void setType_reclamation(String type_reclamation) {
        this.type_reclamation = type_reclamation;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", subjuct='" + subjuct + '\'' +
                ", description='" + description + '\'' +
                ", reported_username='" + reported_username + '\'' +
                ", type_reclamation='" + type_reclamation + '\'' +
                ", screenshot='" + screenshot + '\'' +
                '}';
    }


}
