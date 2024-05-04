package edu.esprit.entities;

import java.time.LocalDateTime;

public class VerificationCode {
    private int id;
    private User user;
    private String code;
    private LocalDateTime expiryDate;

    public VerificationCode(User user, String code, int validityDurationInMinutes) {
        this.user = user;
        this.code = code;
        this.expiryDate = LocalDateTime.now().plusMinutes(validityDurationInMinutes);
    }

    public VerificationCode() {

    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }

    @Override
    public String toString() {
        return "VerificationCode{" +
                "id=" + id +
                ", user=" + user +
                ", code='" + code + '\'' +
                ", expiryDate=" + expiryDate +
                '}';
    }
}
