module edu.esprit {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires java.persistence;
    requires javafx.web;

    opens edu.esprit.controller.user to javafx.fxml;
    opens edu.esprit.controller to javafx.fxml;

    exports edu.esprit;
}
