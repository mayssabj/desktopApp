module edu.esprit {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    exports edu.esprit.enums to com.google.gson;
    requires com.google.gson;

    opens edu.esprit.controller.user to javafx.fxml;

    exports edu.esprit;
}
