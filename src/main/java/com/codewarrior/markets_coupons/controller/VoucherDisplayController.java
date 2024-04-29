package com.codewarrior.markets_coupons.controller;

import com.codewarrior.markets_coupons.model.Market;
import com.codewarrior.markets_coupons.model.User;
import com.codewarrior.markets_coupons.model.Voucher;
import com.codewarrior.markets_coupons.model.VoucherCategory;
import com.codewarrior.markets_coupons.service.CategoryDAO;
import com.codewarrior.markets_coupons.service.MarketDAO;
import com.codewarrior.markets_coupons.service.UserDAO;
import com.codewarrior.markets_coupons.service.VoucherDAO;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class VoucherDisplayController implements Initializable {
    private final VoucherDAO voucherDAO = new VoucherDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final UserDAO userDAO = new UserDAO();
    private final MarketDAO marketDAO = new MarketDAO();

    @FXML
    private TableView<Voucher> voucherTable;

    @FXML
    private TableColumn<Voucher, Double> colValue;

    @FXML
    private TableColumn<Voucher, Date> colDate;

    @FXML
    private TableColumn<Voucher, Integer> colUsable;

    @FXML
    private TableColumn<Voucher, String> colValid;

    @FXML
    private TableColumn<Voucher, String> colGiven;

    @FXML
    private TableColumn<Voucher, String> colMarket;

    @FXML
    private TableColumn<Voucher, String> colCategory;

    @FXML
    private TableColumn<Voucher, String> colUser;

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
            UserDAO userDAO = new UserDAO();

            // Assuming you have a method to retrieve market, category, and user information
            Market market = marketDAO.getMarketById(marketId);
            VoucherCategory category = categoryDAO.getCategoryById(categoryId);
            User user = userDAO.getUserById(userId);

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
        colCode.setCellValueFactory(new PropertyValueFactory<>("Code"));
        colMarket.setCellValueFactory(new PropertyValueFactory<>("marketRelatedId"));
        colValid.setCellValueFactory(cellData -> {
            boolean isValid = cellData.getValue().isValid();
            return isValid ? new SimpleStringProperty("valid") : new SimpleStringProperty("not valid");
        });
        colValid.setCellFactory(column -> {
            return new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText("");
                    } else {
                        setText(item);
                        if (item.equals("valid")) {
                            setTextFill(Color.GREEN); // Customize text color for valid vouchers
                        } else {
                            setTextFill(Color.RED); // Customize text color for invalid vouchers
                        }
                    }
                }
            };
        });

        colGiven.setCellValueFactory(cellData -> {
            boolean isGivenToUser = cellData.getValue().isGivenToUser();
            return isGivenToUser ? new SimpleStringProperty("given") : new SimpleStringProperty("not given");
        });

        colGiven.setCellFactory(column -> {
            return new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText("");
                    } else {
                        setText(item);
                        if (item.equals("given")) {
                            setTextFill(Color.GREEN); // Customize text color for valid vouchers
                        } else {
                            setTextFill(Color.RED); // Customize text color for invalid vouchers
                        }
                    }
                }
            };
        });

        colMarket.setCellValueFactory(cellData -> {
            int id = cellData.getValue().getMarketRelatedId();
            System.out.println("market id provided by cell :> { "+id+" }");
            Market market;
            market = marketDAO.getMarketById(id);
            if (market != null) {
                return new SimpleStringProperty(market.getName());
            } else {
                // If user is not found (or any error handling), return an empty string
                return new SimpleStringProperty("");
            }
        });

        colCategory.setCellValueFactory(cellData -> {
            int id = cellData.getValue().getCategoryId();
            System.out.println("category id provided by cell :> { "+id+" }");
            VoucherCategory category = new VoucherCategory();
            try {
                category = categoryDAO.getCategoryById(id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (category != null) {
                // If user is found, return the email as an ObservableValue<String>
                return new SimpleStringProperty(category.getTitre());
            } else {
                // If user is not found (or any error handling), return an empty string
                return new SimpleStringProperty("");
            }
        });

        colUser.setCellValueFactory(cellData -> {
            int id = cellData.getValue().getUserWonId();
            User user = userDAO.getUserById(id);
            if (user != null) {
                // If user is found, return the email as an ObservableValue<String>
                return new SimpleStringProperty(user.getEmail());
            } else {
                // If user is not found (or any error handling), return an empty string
                return new SimpleStringProperty("");
            }
        });

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
            System.out.println("update voucher: ");
        }
    }

    @FXML
    void goBack(MouseEvent event) {
        System.out.println("redirect to Home");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/codewarrior/markets_coupons/voucher-view.fxml"));
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