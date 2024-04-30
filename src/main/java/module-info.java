module com.codewarrior.markets_coupons {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.zxing.javase;
    requires com.google.zxing;
    requires infobip.mobile.messaging.api.java;
    requires java.desktop;
    requires okhttp3;


    opens com.codewarrior.markets_coupons to javafx.fxml;
    exports com.codewarrior.markets_coupons;

    opens com.codewarrior.markets_coupons.controller to javafx.fxml;
    exports com.codewarrior.markets_coupons.controller;

    opens com.codewarrior.markets_coupons.model to javafx.base;
}