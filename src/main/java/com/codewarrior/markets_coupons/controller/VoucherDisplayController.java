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
    private TableColumn<Voucher, String> colCode;

    @FXML
    private Pane back;

    @FXML
    private ImageView voucherImage;


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
        colCode.setCellValueFactory(new PropertyValueFactory<>("Code"));
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
            System.out.println("added voucher: ");
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
