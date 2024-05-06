package edu.esprit.controller;

import edu.esprit.services.CategoryDAO;
import edu.esprit.entities.VoucherCategory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

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

    @FXML
    private Label controlDescription;

    @FXML
    private Label controllTitle;

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

    public void displayCategories() {
        ObservableList<VoucherCategory> list = getCategories();
        categoryTable.setItems(list);
        colTitle.setCellValueFactory(new PropertyValueFactory<VoucherCategory,String>("titre"));
        colDescription.setCellValueFactory(new PropertyValueFactory<VoucherCategory,String>("description"));
    }


    @FXML
    void updateCategory(MouseEvent event) throws SQLException {
        VoucherCategory category = new VoucherCategory();
        category.setId(displayCategoryInfo().getId());
        boolean isValid = true;
        if (updateTitle.getText().isEmpty()) {
            controllTitle.setText("title must be discount or purchase");
            isValid = false;
        }
        if(updateDescription.getText().isEmpty()){
            controlDescription.setText("require category description");
            isValid = false;
        }
        if("purchase".equals(updateTitle.getText()) || "discount".equals(updateTitle.getText())){
            isValid = true;
        }else {
            isValid = false;
            controllTitle.setText("title must be purchase or discount");
        }
        if (category !=null && isValid){
            controllTitle.setText("");
            controlDescription.setText("");
            System.out.println("isValid => "+isValid);
            category.setTitre(this.updateTitle.getText());
            category.setDescription(updateDescription.getText());
            System.out.println("Update Button :> "+category.toString());
            categoryDAO.updateCategory(category);
            System.out.println("Category with id: " + category.getId() + " updated !");
            // Refresh the TableView
            displayCategories();
        }
    }




    @FXML
    void goBack(MouseEvent event) {
        System.out.println("redirect to Home");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/codewarrior/markets_coupons/category-view.fxml"));
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

    @FXML
    void addCategory(MouseEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/category-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Home Window");
            newStage.show();

            // Close the current stage (window)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
