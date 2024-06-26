package edu.esprit.entities;

public class Market {
    private int id;
    private String name;
    private String image;
    private String address;
    private String city;
    private String region;
    private int zipCode;

    public Market() {
    }

    public Market(int id, String name, String image, String address, String city, String region, int zipCode) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.address = address;
        this.city = city;
        this.region = region;
        this.zipCode = zipCode;
    }

    public Market(String name, String image, String address, String city, String region, int zipCode) {
        this.name = name;
        this.image = image;
        this.address = address;
        this.city = city;
        this.region = region;
        this.zipCode = zipCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage(){
        return this.image;
    }
    public void setImage(String image){
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return "Market{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", region='" + region + '\'' +
                ", zipCode=" + zipCode +
                '}';
    }
}