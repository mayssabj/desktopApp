package edu.esprit.entities;

public class User {
<<<<<<< HEAD
    private int id ;
    private String username ;
    private String image ;
    public User(int id, String username, String image) {
        this.id = id;
        this.username = username;
        this.image = image;
    }

    public User(String username, String image) {
        this.username = username;
        this.image = image;
    }

=======
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

    // Getters and setters
>>>>>>> 560008013e3371a7c394d79c8393560c80ec93e7
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

<<<<<<< HEAD
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
=======
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
>>>>>>> 560008013e3371a7c394d79c8393560c80ec93e7
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
<<<<<<< HEAD
                ", username='" + username + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

=======
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
>>>>>>> 560008013e3371a7c394d79c8393560c80ec93e7
}
