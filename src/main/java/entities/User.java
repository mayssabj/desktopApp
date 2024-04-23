package entities;
import java.util.Collection;

public class User {
    private int id;
    private String password;
    private String email;
    private String address;
    private String phone;
    private String username;
    private String gender;

    // Constructor
    public User(int id, String password, String email, String address, String phone, String username, String gender) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.username = username;
        this.gender = gender;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    // toString method
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", username='" + username + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
