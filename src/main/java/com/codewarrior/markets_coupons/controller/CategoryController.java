package com.codewarrior.markets_coupons.controller;

import com.codewarrior.markets_coupons.model.VoucherCategory;
import com.codewarrior.markets_coupons.service.CategoryDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class CategoryController {

    private final CategoryDAO categoryDAO = new CategoryDAO();

    @FXML
    private Pane goBack;

    @FXML
    private TextArea descriptionField;


    @FXML
    private TextField titleField;

    @FXML
    private Pane display;


    @FXML
    private Label descriptionControll;

    @FXML
    private Label titleControll;

    @FXML
    void addButton(MouseEvent event) throws SQLException {
        boolean isValid = true;
        String title = this.titleField.getText();
        String description = this.descriptionField.getText();
        if(title.isEmpty() || description.isEmpty()) {
            isValid = false;
        }
        isValid = "purchase".equals(title) || "discount".equals(title);

        if (isValid) {
            this.titleControll.setText("");
            this.descriptionControll.setText("");

            VoucherCategory newCategory = new VoucherCategory(title, description);
            categoryDAO.createCategory(newCategory);
            System.out.println("Category added successfully!");
            displayRoute(event);
        } else {
            System.out.println("Title must be either 'purchase' or 'discount'.");
            this.descriptionControll.setText("should not be blank");
            this.titleControll.setText("should not be blank and Title must be either 'purchase' or 'discount'.");

        }
    }

    @FXML
    void displayRoute(MouseEvent event) {
        System.out.println("marzabba");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/codewarrior/markets_coupons/displayCategory-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Category Window");
            newStage.show();

            // Close the current stage (window)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void goBack(MouseEvent event) {
        System.out.println("redirect to Home");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/codewarrior/markets_coupons/dash.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Home Window");
            newStage.show();

            // Close the current stage (window)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
