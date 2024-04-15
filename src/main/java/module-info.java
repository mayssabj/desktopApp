module edu.esprit {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;

    opens edu.esprit.controller.user to javafx.fxml;

    exports edu.esprit;
}
