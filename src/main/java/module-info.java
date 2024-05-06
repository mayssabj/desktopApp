module edu.esprit {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    exports edu.esprit.enums to com.google.gson;
    requires com.google.gson;
    requires javax.mail;
    requires java.persistence;
    requires javafx.web;
    requires jdk.jsobject;
    requires cloudinary.core;
    requires org.kordamp.bootstrapfx.core;
    requires twilio;
    requires itextpdf;
    requires java.desktop;
    requires httpcore;
    requires httpclient;
    requires httpmime;

    opens edu.esprit.controller.user to javafx.fxml;
    opens edu.esprit.controller to javafx.fxml;
    opens edu.esprit.entities to javafx.base;

    exports edu.esprit;
}
