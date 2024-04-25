package edu.esprit.entities;

public class JavaConnector {
    private String selectedLocation; // Store the selected location


    public String getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(String selectedLocation) {
        this.selectedLocation = selectedLocation;
    }

    public void getLocationFromMap(String location) {
        System.out.println("Location received from map: " + location);
        setSelectedLocation(location); // Set the selected location
    }
}
