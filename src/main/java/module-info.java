module edu.esprit {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires java.persistence;
    requires javafx.web;
    requires okhttp3;
    requires com.google.gson;
    requires java.mail;
    requires java.net.http;

    opens edu.esprit.controller.user to javafx.fxml;
    opens edu.esprit.controller to javafx.fxml;
    // Add this line to open the Reclamation controller package to javafx.fxml
    opens edu.esprit.controller.Reclamation to javafx.fxml;
    // Add this line to open the Reclamation controller package to javafx.fxml
    opens edu.esprit.controller.Avertissement to javafx.fxml;

    exports edu.esprit;
}
