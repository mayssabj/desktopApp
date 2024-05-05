package com.codewarrior.markets_coupons.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String address;
    private String phone;
    private String photo; // Assuming 'photo' corresponds to 'image' in the database
    private int reputation;

    // Constructor
    public User() {
        // Default constructor
    }

    public User(int id, String username, String password, String email, int reputation) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.reputation = reputation;
    }

    // Getters and setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    // Override toString method if needed
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", photo='" + photo + '\'' +
                ", reputation=" + reputation +
                '}';
    }
}
