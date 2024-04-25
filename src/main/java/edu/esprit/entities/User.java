package edu.esprit.entities;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String phone;
    private String profilePicture;
    private String address;
    private String gender;
    private int avertissements_count;

    public User(int id, String username, String email, String password, String phone, String profilePicture, String address, String gender, int avertissements_count) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.profilePicture = profilePicture;
        this.address = address;
        this.gender = gender;
        this.avertissements_count = avertissements_count;
    }

    public User() {
        // Default constructor
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAvertissements_count() {
        return avertissements_count;
    }

    public void setAvertissements_count(int avertissements_count) {
        this.avertissements_count = avertissements_count;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                ", avertissements_count=" + avertissements_count +
                '}';
    }
}
