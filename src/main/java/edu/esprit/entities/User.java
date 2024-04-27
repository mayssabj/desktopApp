package edu.esprit.entities;
import com.google.gson.Gson;
import edu.esprit.enums.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User {
    private int id;
    private List<Role> roles;
    private String password;
    private String email;
    private String address;
    private String phone;
    private String photo;
    private boolean isEnabled;
    private String emailVerificationToken;
    private boolean isVerified;
    private String username;
    private String gender;
    private String resetToken;
    private int avertissementsCount;
    private Integer reputation;  // Using Integer to allow null values

    public User() {
        // Default constructor initializes roles with a default value
        this.roles = new ArrayList<>(Arrays.asList(Role.ROLE_USER));
        this.isEnabled = true;
    }
    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }

    public void setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public int getAvertissementsCount() {
        return avertissementsCount;
    }

    public void setAvertissementsCount(int avertissementsCount) {
        this.avertissementsCount = avertissementsCount;
    }

    public Integer getReputation() {
        return reputation;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", roles=" + roles +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", photo='" + photo + '\'' +
                ", isEnabled=" + isEnabled +
                ", emailVerificationToken='" + emailVerificationToken + '\'' +
                ", isVerified=" + isVerified +
                ", username='" + username + '\'' +
                ", gender='" + gender + '\'' +
                ", resetToken='" + resetToken + '\'' +
                ", avertissementsCount=" + avertissementsCount +
                ", reputation=" + reputation +
                '}';
    }

    public String getRolesAsString() {
        Gson gson = new Gson();
        return gson.toJson(this.roles);
    }

}
