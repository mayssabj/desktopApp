package com.codewarrior.markets_coupons.controller;

import com.codewarrior.markets_coupons.model.Market;
import com.codewarrior.markets_coupons.model.User;
import com.codewarrior.markets_coupons.model.Voucher;
import com.codewarrior.markets_coupons.model.VoucherCategory;
import com.codewarrior.markets_coupons.service.CategoryDAO;
import com.codewarrior.markets_coupons.service.MarketDAO;
import com.codewarrior.markets_coupons.service.VoucherDAO;
import com.codewarrior.markets_coupons.service.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.codewarrior.markets_coupons.util.RandomStringGenerator.generateRandomString;

public class VoucherController {

    private final MarketDAO marketDAO = new MarketDAO();
    private  final CategoryDAO categoryDAO = new CategoryDAO();
    private  final VoucherDAO voucherDAO = new VoucherDAO();
    private final UserDAO userDAO = new UserDAO();

    @FXML
    private Pane goBack;

    @FXML
    private ComboBox<String> categoryField;

    @FXML
    private DatePicker dateField;

    @FXML
    private CheckBox isGIvenBox;

    @FXML
    private ComboBox<String> marketField;

    @FXML
    private TextArea typeField;

    @FXML
    private ComboBox<Integer> usableField;

    @FXML
    private ComboBox<String> userField;

    @FXML
    private CheckBox validityBox;

    @FXML
    private TextField valueField;

    @FXML
    private Label controlValue;

    @FXML
    private Label dateControll;

    @FXML
    public void initialize() throws SQLException {
        loadUsers();
        loadMarkets();
        loadCategories();
        usableField.getItems().add(1);
    }

    private void loadUsers() {
        // This should be replaced with a database call=
        List<User> listOfUser = userDAO.getAllUsers();
        List<String> listOfEmails = new ArrayList<>();
        for (User user : listOfUser) {
            listOfEmails.add(user.getEmail());
        }
        ObservableList<String> items = FXCollections.observableArrayList(listOfEmails);
        userField.setItems((ObservableList<String>) items);
    }

    private void loadMarkets() {
        // This should be replaced with a database call
        List<Market> markets = marketDAO.getAllMarkets();
        List<String> listOfNames = new ArrayList<>();
        for (Market market : markets) {
            listOfNames.add(market.getName());
        }
        ObservableList<String> items = FXCollections.observableArrayList(listOfNames);
        marketField.setItems((ObservableList<String>) items);
    }

    private void loadCategories() throws SQLException {
        // This should be replaced with a database call
        List<VoucherCategory> categories = categoryDAO.getAllCategories();
        List<String> listOfNames = new ArrayList<>();
        for (VoucherCategory category : categories) {
            listOfNames.add(category.getTitre());
        }
        ObservableList<String> items = FXCollections.observableArrayList(listOfNames);
        categoryField.setItems((ObservableList<String>) items);
    }
    @FXML
    void addVoucher(MouseEvent event) throws IOException, SQLException {

        boolean isValidity = true;
        LocalDate currentDate = LocalDate.now();


        if (valueField.getText().isBlank()) {
            System.out.println("ena fil if mtaa controll");
            controlValue.setText("require a value and contain only numbers");
            isValidity = false;
        }
        if (dateField.getValue().isAfter(currentDate.plusDays(3))) {
            dateControll.setText("at least 2 days from this date");
            isValidity = false;
        }
        if(isValidity) {
            double value = Double.parseDouble(valueField.getText());
            LocalDate localDate = dateField.getValue();
            Date selectedDate = Date.valueOf(localDate);
            Integer usable = usableField.getSelectionModel().getSelectedItem();
            boolean isValid = validityBox.isSelected();
            boolean isGiven = isGIvenBox.isSelected();
            User userFound = userDAO.getUserByEmail(userField.getValue());
            User user = userFound;
            Market marketFound = marketDAO.getMarketByName(marketField.getValue());
            Market market = marketFound;
            VoucherCategory categoryFound = categoryDAO.getCategoryByTitle(categoryField.getValue());
            VoucherCategory category = categoryFound;

            String code = generateRandomString();
            Voucher voucher = new Voucher(value, code , selectedDate,usable,isValid,isGiven,market.getId(),category.getId(),user.getId());
            voucherDAO.addVoucher(voucher);
            System.out.println(voucher.toString());
            displayRoute(event);
        }


    }

    @FXML
    void displayRoute(MouseEvent event) {

        System.out.println("marzabba");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/codewarrior/markets_coupons/displayVoucher-view.fxml"));
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
    void getCategory(MouseEvent event) {

    }

    @FXML
    void getMarket(MouseEvent event) {

    }

    @FXML
    void getUser(MouseEvent event) {

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
