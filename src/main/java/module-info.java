module edu.esprit {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires java.persistence;
    requires javafx.web;
    requires com.google.gson;
    requires java.net.http;
    requires cloudinary.core;
    exports edu.esprit.enums to com.google.gson;
    requires jdk.jsobject;
    requires org.kordamp.bootstrapfx.core;
    requires twilio;
    requires itextpdf;
    requires java.desktop;
    requires httpmime;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires java.datatransfer;

    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires javax.mail.api;
    requires okhttp3;
    requires google.api.translate.java;
    requires spring.security.crypto;

    opens edu.esprit.controller.user to javafx.fxml;
    opens edu.esprit.controller to javafx.fxml;
    // Add this line to open the Reclamation controller package to javafx.fxml
    opens edu.esprit.controller.Reclamation to javafx.fxml;
    // Add this line to open the Reclamation controller package to javafx.fxml
    opens edu.esprit.controller.Avertissement to javafx.fxml;


    opens edu.esprit.entities to javafx.base;

    exports edu.esprit;
    // Exporte le package contenant StatisticsController au module javafx.fxml
    exports edu.esprit.controller.Statistique to javafx.fxml;

    // Ouvre le package pour la réflexion par javafx.fxml
    opens edu.esprit.controller.Statistique to javafx.fxml;



}
