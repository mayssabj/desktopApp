package com.codewarrior.markets_coupons.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String address;

    private  String phone;

    private  String image;
    // Add other fields as needed

    public User() {
        // Default constructor
    }

    public User(int id, String username, String password, String email, String address) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
    }

    public User(int id, String username, String passwoard, String mail, String address, String phone, String o) {

        this.id = id;
        this.username = username;
        this.password = passwoard;
        this.email = mail;
        this.address = address;
        this.phone = phone;
        this.image = o;
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

    // Override toString method if needed
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public String getName() {
        return this.username;
    }
}
