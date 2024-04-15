package com.codewarrior.markets_coupons.controller;

import com.codewarrior.markets_coupons.model.VoucherCategory;
import com.codewarrior.markets_coupons.service.CategoryDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class CategoryDisplayController implements Initializable {
    private final CategoryDAO categoryDAO = new CategoryDAO();
    @FXML
    private Pane back;


    @FXML
    private TableColumn<VoucherCategory, String> colTitle;

    @FXML
    private TableColumn<VoucherCategory, String> colDescription;

    @FXML
    private TableView<VoucherCategory> categoryTable;

    @FXML
    private TextField updateTitle;

    @FXML
    private TextArea updateDescription;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayMarkets();
    }

    public ObservableList<VoucherCategory> getCategories(){
        ObservableList<VoucherCategory> categories ;
        return categories = categoryDAO.getAllCategoryOb();
    }

    public void displayMarkets(){
        ObservableList<VoucherCategory> list = getCategories();
        this.categoryTable.setItems(list);
        this.colTitle.setCellValueFactory(new PropertyValueFactory<VoucherCategory,String>("titre"));
        this.colDescription.setCellValueFactory(new PropertyValueFactory<VoucherCategory,String>("description"));
    }

    @FXML
    VoucherCategory getCategoryInfo(MouseEvent event) {
        VoucherCategory selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            int id = selectedCategory.getId();
            String title = selectedCategory.getTitre();
            String description = selectedCategory.getDescription();

            VoucherCategory category = new VoucherCategory(id, title, description);
            this.updateTitle.setText(category.getTitre());
            this.updateDescription.setText(category.getDescription());

            String s = category.toString();
            System.out.println(s);

        }
        return selectedCategory;
    }

    private VoucherCategory getSelectedCategory() {
        return categoryTable.getSelectionModel().getSelectedItem();
    }

    // Method to display market information and set image
    @FXML
    VoucherCategory displayCategoryInfo() {
        VoucherCategory selectedCategory = getSelectedCategory();
        if (selectedCategory != null) {
            int id = selectedCategory.getId();
            String title = selectedCategory.getTitre();
            String description = selectedCategory.getDescription();

            VoucherCategory category = new VoucherCategory(id, title, description);
            String s = category.toString();
            System.out.println(s);
            return category;
        }
        return null;
    }

    // Method to delete a market
    @FXML
    void deleteCategory() throws SQLException {
        VoucherCategory categoryFound = getSelectedCategory();
        if (categoryFound != null) {
            // Delete the market from the database
            categoryDAO.deleteCategory(categoryFound.getId());
            System.out.println("category with id: " + categoryFound.getId() + " deleted !");

            // Remove the deleted market from the table view
            categoryTable.getItems().remove(categoryFound);
        }
    }


    @FXML
    void updateCategory(MouseEvent event) {

    }



    @FXML
    void goBack(MouseEvent event) {
        System.out.println("redirect to Home");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/codewarrior/markets_coupons/hello-view.fxml"));
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
