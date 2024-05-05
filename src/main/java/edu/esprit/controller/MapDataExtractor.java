package edu.esprit.controller;

import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

public class MapDataExtractor {
    public void onDataReceived(String data) {
        // Handle the received data here
        System.out.println("Received data from map: " + data);
    }

    public String getSelectedLocation(WebEngine engine) {
        JSObject window = (JSObject) engine.executeScript("window");
        if (window != null) {
            Object javaConnector = window.getMember("javaConnector");
            if (javaConnector instanceof JSObject) {
                JSObject connector = (JSObject) javaConnector;
                Object selectedLocation = connector.call("getSelectedLocation");
                if (selectedLocation instanceof String) {
                    return (String) selectedLocation;
                }
            }
        }
        return null; // or throw an exception if needed
    }
}
