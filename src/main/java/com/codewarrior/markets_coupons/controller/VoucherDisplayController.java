package com.codewarrior.markets_coupons.controller;

import com.codewarrior.markets_coupons.model.Market;
import com.codewarrior.markets_coupons.model.User;
import com.codewarrior.markets_coupons.model.Voucher;
import com.codewarrior.markets_coupons.model.VoucherCategory;
import com.codewarrior.markets_coupons.service.CategoryDAO;
import com.codewarrior.markets_coupons.service.MarketDAO;
import com.codewarrior.markets_coupons.service.VoucherDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class VoucherDisplayController implements Initializable {
    private final VoucherDAO voucherDAO = new VoucherDAO();

    @FXML
    private TableView<Voucher> voucherTable;

    @FXML
    private TableColumn<Voucher, Double> colValue;

    @FXML
    private TableColumn<Voucher, Date> colDate;

    @FXML
    private TableColumn<Voucher, Integer> colUsable;

    @FXML
    private TableColumn<Voucher, Boolean> colValid;

    @FXML
    private TableColumn<Voucher, Boolean> colGiven;

    @FXML
    private TableColumn<Voucher, Market> colMarket;

    @FXML
    private TableColumn<Voucher, VoucherCategory> colCategory;

    @FXML
    private TableColumn<Voucher, User> colUser;

    @FXML
    private Pane back;

    @FXML
    private ImageView voucherImage;

    @FXML
    private TextField updateValue;

    @FXML
    private TextField updateDate;

    @FXML
    private TextField updateUsable;

    @FXML
    private TextField updateValid;

    @FXML
    private TextField updateGiven;

    @FXML
    private TextField updateMarket;

    @FXML
    private TextField updateCategory;

    @FXML
    private TextField updateUser;

    @FXML
    private Label controlValue;

    @FXML
    private Label controlDate;

    @FXML
    private Label controlUsable;

    @FXML
    private Label controlValid;

    @FXML
    private Label controlGiven;

    @FXML
    private Label controlMarket;

    @FXML
    private Label controlCategory;

    @FXML
    private Label controlUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayVouchers();
    }

    public ObservableList<Voucher> getVouchers() {
        return voucherDAO.getAllVouchersOb();
    }

    @FXML
    void getVoucherInfo(MouseEvent event) throws SQLException {
        Voucher selectedVoucher = voucherTable.getSelectionModel().getSelectedItem();
        if (selectedVoucher != null) {
            int id = selectedVoucher.getId();
            double value = selectedVoucher.getValue();
            Date validityDate = selectedVoucher.getExpiration();
            int usable = selectedVoucher.getUsageLimit();
            boolean isValid = selectedVoucher.isValid();
            boolean isGiven = selectedVoucher.isGivenToUser();
            int marketId = selectedVoucher.getMarketRelatedId();
            int categoryId = selectedVoucher.getCategoryId();
            int userId = selectedVoucher.getUserWonId();

            // Assuming you have instances of MarketDAO, CategoryDAO, and UserDAO
            MarketDAO marketDAO = new MarketDAO();
            CategoryDAO categoryDAO = new CategoryDAO();

            // Assuming you have a method to retrieve market, category, and user information
            Market market = marketDAO.getMarketById(marketId);
            VoucherCategory category = categoryDAO.getCategoryById(categoryId);
            User user = new User(1, "salah", "passwoard", "user@gmail.com", "address", "+2165846321", null);

            // Update UI with voucher information
            this.updateValue.setText(String.valueOf(value));
            this.updateDate.setText(validityDate.toString()); // Convert LocalDate to String
            this.updateUsable.setText(String.valueOf(usable));
            this.updateValid.setText(String.valueOf(isValid));
            this.updateGiven.setText(String.valueOf(isGiven));
            this.updateMarket.setText(market.getName()); // Assuming market.getName() returns the market name
            this.updateCategory.setText(category.getTitre()); // Assuming category.getName() returns the category name
            this.updateUser.setText(user.getName()); // Assuming user.getName() returns the user name

            String s = selectedVoucher.toString();
            System.out.println(s);
        }
    }

    public void displayVouchers() {
        ObservableList<Voucher> list = getVouchers();
        voucherTable.setItems(list);
        colValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("expiration"));
        colUsable.setCellValueFactory(new PropertyValueFactory<>("usageLimit"));
        colValid.setCellValueFactory(new PropertyValueFactory<>("valid"));
        colGiven.setCellValueFactory(new PropertyValueFactory<>("givenToUser"));
        colMarket.setCellValueFactory(new PropertyValueFactory<>("marketRelatedId"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("userWonId"));
    }

    @FXML
    void deleteVoucher() {
        Voucher voucherFound = getSelectedVoucher();
        if (voucherFound != null) {
            voucherDAO.deleteVoucher(voucherFound.getId());
            System.out.println("Voucher with id: " + voucherFound.getId() + " deleted!");
            voucherTable.getItems().remove(voucherFound);
        }
    }

    @FXML
    void updateVoucher(MouseEvent event) {
        Voucher selectedVoucher = getSelectedVoucher();
        if (selectedVoucher != null) {
            // Get the updated values from text fields
            double value = Double.parseDouble(updateValue.getText());
            int usable = Integer.parseInt(updateUsable.getText());
            boolean isValid = Boolean.parseBoolean(updateValid.getText());
            boolean isGiven = Boolean.parseBoolean(updateGiven.getText());
            // Update the voucher object
            selectedVoucher.setValue(value);
            selectedVoucher.setUsageLimit(usable);
            selectedVoucher.setValid(isValid);
            selectedVoucher.setGivenToUser(isGiven);
            // Update the database
            voucherDAO.updateVoucher(selectedVoucher);
            // Refresh the table view
            displayVouchers();
            System.out.println("Voucher with id: " + selectedVoucher.getId() + " updated!");
        }
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


    private Voucher getSelectedVoucher() {
        return voucherTable.getSelectionModel().getSelectedItem();
    }
}
