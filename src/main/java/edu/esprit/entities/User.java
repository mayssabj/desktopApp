package edu.esprit.entities;

public class User {
    private int id;
    private String email;
    private String password;
    private String phone;
    private String profilePicture;
    private String address;
    private String gender;


    public User() {
        // Default constructor
    }

    public User(int id, String email, String password, String phone, String profilePicture, String address, String gender) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.profilePicture = profilePicture;
        this.address = address;
        this.gender = gender;
    }

    public User(int id, String email, String phone, String profilePicture, String address, String gender) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.profilePicture = profilePicture;
        this.address = address;
        this.gender = gender;
    }

    public User(String email, String profilePicture) {
        this.email = email;
        this.profilePicture = profilePicture;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
