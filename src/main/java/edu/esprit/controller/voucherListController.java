package edu.esprit.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.qrcode.WriterException;
import edu.esprit.entities.Market;
import edu.esprit.entities.User;
import edu.esprit.entities.Voucher;
import edu.esprit.entities.VoucherCategory;
import edu.esprit.services.CategoryDAO;
import edu.esprit.services.MarketDAO;
import edu.esprit.services.UserDAO;
import edu.esprit.services.VoucherDAO;
import edu.esprit.utils.Session;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static javax.swing.text.StyleConstants.ALIGN_CENTER;

public class voucherListController implements Initializable {
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
        loadUsers();
        loadMarkets();
        try {
            loadCategories();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Voucher> getVouchers() {
        User user = Session.getInstance().getCurrentUser();
        return voucherDAO.getAllVouchersObForUser(user.getId());
    }

    private void loadUsers() {
        // This should be replaced with a database call=
        List<User> listOfUser = userDAO.getAllUsers();
        List<String> listOfEmails = new ArrayList<>();
        for (User user : listOfUser) {
            listOfEmails.add(user.getEmail());
        }
    }

    private void loadMarkets() {
        // This should be replaced with a database call
        List<Market> markets = marketDAO.getAllMarkets();
        List<String> listOfNames = new ArrayList<>();
        for (Market market : markets) {
            listOfNames.add(market.getName());
        }
    }

    private void loadCategories() throws SQLException {
        // This should be replaced with a database call
        List<VoucherCategory> categories = categoryDAO.getAllCategories();
        List<String> listOfNames = new ArrayList<>();
        for (VoucherCategory category : categories) {
            listOfNames.add(category.getTitre());
        }
    }

    @FXML
    void getVoucherInfo(MouseEvent event) throws SQLException, DocumentException, IOException, WriterException {
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
            String type = selectedVoucher.getType();

            // Assuming you have instances of MarketDAO, CategoryDAO, and UserDAO
            MarketDAO marketDAO = new MarketDAO();
            CategoryDAO categoryDAO = new CategoryDAO();
            UserDAO userDAO = new UserDAO();

            // Assuming you have a method to retrieve market, category, and user information
            Market market = marketDAO.getMarketById(marketId);
            VoucherCategory category = categoryDAO.getCategoryById(categoryId);
            User user = userDAO.getUserById(userId);

            String marketName = market.getName();
            String userEmail = user.getEmail();
            String categoryName = category.getTitre();
            String s = selectedVoucher.toString();
            System.out.println(s);

            //QR CODE API !
            String str = "";
            String voucherCode = selectedVoucher.getCode(); // Example: Get voucher code
            String price = String.valueOf(selectedVoucher.getValue());
            String _userEmail = user.getEmail();
            String _categoryName = category.getTitre();
            String _marketName = market.getName();

            str = "code : " + voucherCode + "\n" + "price :" + price + " DT" + "\n" + "user email : " + _userEmail + "\n" + "category name : " + _categoryName;
            int qrCodeWidth = 200;
            int qrCodeHeight = 200;
            Image qrCode = generateQRCode(str, qrCodeWidth, qrCodeHeight);
            voucherImage.setImage(qrCode);

        }
    }

    @FXML
    void pdf(MouseEvent event) throws SQLException, DocumentException, IOException, WriterException {
        Voucher selectedVoucher = voucherTable.getSelectionModel().getSelectedItem();
        System.out.println(selectedVoucher.toString());
        handleSaveFile(selectedVoucher);

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
            System.out.println("market id provided by cell :> { " + id + " }");
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
            System.out.println("category id provided by cell :> { " + id + " }");
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
    void deleteVoucher() throws SQLException, DocumentException, IOException, WriterException {
        Voucher voucherFound = getSelectedVoucher();
        if (voucherFound != null) {
            voucherDAO.deleteVoucher(voucherFound.getId());
            System.out.println("Voucher with id: " + voucherFound.getId() + " deleted!");
            voucherTable.getItems().remove(voucherFound);
        }
    }

    public static Image generateQRCode(String content, int width, int height) {
        try {
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            return new Image(new ByteArrayInputStream(outputStream.toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    void handleSaveFile(Voucher v) throws IOException, DocumentException, SQLException, WriterException {
        User user = userDAO.getUserById(v.getUserWonId());
        VoucherCategory category = categoryDAO.getCategoryById(v.getCategoryId());
        String fileName = "voucher.pdf";
        Document doc = new Document();
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            PdfWriter.getInstance(doc, fos);
            doc.open();

            // Title
            Paragraph title = new Paragraph("LOST & FOUND");
            title.setAlignment(ALIGN_CENTER);
            doc.add(title);

            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("All Coupons information in this table:"));
            doc.add(new Paragraph("\n"));

            // Table setup
            PdfPTable table = new PdfPTable(4); // Adjust the number of columns as needed
            table.setWidthPercentage(100);

            // Table headers
            addTableCell(table, "Code", BaseColor.ORANGE);
            addTableCell(table, "Type", BaseColor.ORANGE);
            addTableCell(table, "Owner", BaseColor.ORANGE);
            addTableCell(table, "Price", BaseColor.ORANGE);

            // Table data from Voucher object
            addTableCell(table, v.getCode(), null);
            addTableCell(table, category.getTitre(), null);
            addTableCell(table, user.getEmail(), null);
            addTableCell(table, String.valueOf(v.getValue()), null);

            doc.add(table);
            doc.close();

            // Open the generated PDF file
            Desktop.getDesktop().open(new File(fileName));
        }
    }

    private void addTableCell(PdfPTable table, String text, BaseColor backgroundColor) {
        PdfPCell cell = new PdfPCell(new Paragraph(text));
        if (backgroundColor != null) {
            cell.setBackgroundColor(backgroundColor);
        }
        table.addCell(cell);
    }

    private Voucher getSelectedVoucher() throws SQLException, DocumentException, IOException, WriterException {
        Voucher voucher = voucherTable.getSelectionModel().getSelectedItem();
        return voucher;

    }
}
