module com.example.pidev {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires org.controlsfx.controls; // Corrected module name
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    opens entities to javafx.base;
    opens Services to javafx.fxml;
    exports test;

}
