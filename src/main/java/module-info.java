module edu.esprit {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires java.persistence;
    requires javafx.web;
    requires java.datatransfer;
    requires org.apache.httpcomponents.httpcore;
    requires httpclient;
    requires httpmime;

    exports edu.esprit.enums to com.google.gson;
    requires com.google.gson;
    requires javax.mail;
    requires okhttp3;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires itextpdf;
    requires java.desktop;

    opens edu.esprit.controller.user to javafx.fxml;
    opens edu.esprit.controller to javafx.fxml;

    opens edu.esprit.entities to javafx.base;

    exports edu.esprit;
}
