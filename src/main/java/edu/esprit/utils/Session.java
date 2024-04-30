package edu.esprit.utils;

import edu.esprit.entities.User;

public class Session {
    private static Session instance = null;
    private User currentUser;
    private String resetPasswordEmail;

    public String getResetPasswordEmail() {
        return resetPasswordEmail;
    }

    public void setResetPasswordEmail(String resetPasswordEmail) {
        this.resetPasswordEmail = resetPasswordEmail;
    }

    private Session() {}

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
