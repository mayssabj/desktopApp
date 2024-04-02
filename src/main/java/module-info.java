module com.codewarrior.markets_coupons {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.codewarrior.markets_coupons to javafx.fxml;
    exports com.codewarrior.markets_coupons;
    opens com.codewarrior.markets_coupons.controller to javafx.fxml;
    exports com.codewarrior.markets_coupons.controller;
}