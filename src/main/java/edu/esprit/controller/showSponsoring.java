package edu.esprit.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import edu.esprit.entities.Sponsoring;
import edu.esprit.services.SponsoringService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class showSponsoring {

    private static final int POSTS_PER_ROW = 3;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField tfSearch;

    @FXML
    private void initialize() throws SQLException {
        fnReloadData();
        tfSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            for (Node child : anchorPane.getChildren()) {
                if (child instanceof HBox) {
                    HBox postEntry = (HBox) child;
                    // Add your filtering logic here
                    boolean isVisible = postEntry.getChildren().stream()
                            .filter(Label.class::isInstance)
                            .map(Label.class::cast)
                            .anyMatch(label -> label.getText().toLowerCase().contains(newValue.toLowerCase()));
                    postEntry.setVisible(isVisible);
                    postEntry.setManaged(isVisible);
                }
            }
        });
    }

    @FXML
    private void fnReloadData() throws SQLException {
        int postIndex = 1; // Start post index from 1 to accommodate the header row
        anchorPane.getChildren().clear();
        List<Sponsoring> postList = loadDataFromDatabase();

        // Create header row
        HBox headerRow = new HBox();
        headerRow.setSpacing(8);
        headerRow.setPrefWidth(1050);
        headerRow.setStyle("-fx-background-color: #6b9eef; -fx-padding: 10px; -fx-spacing: 10px;");

        // Header labels
        Label titleLabel = new Label("NAME");
        titleLabel.setPrefWidth(140);
        titleLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(titleLabel);

        Label dateLabel = new Label("Date");
        dateLabel.setPrefWidth(140);
        dateLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(dateLabel);

        Label localLabel = new Label("Description");
        localLabel.setPrefWidth(140);
        localLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(localLabel);

        Label sizeLabel = new Label("Type");
        sizeLabel.setPrefWidth(140);
        sizeLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(sizeLabel); // Add bold style

        Label desLabel = new Label("Contrat");
        desLabel.setPrefWidth(140);
        desLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(desLabel);

        Label userLabel = new Label("image");
        userLabel.setPrefWidth(140);
        userLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(userLabel);



        // Add header row to the AnchorPane
        anchorPane.getChildren().add(headerRow);
        AnchorPane.setTopAnchor(headerRow, 50.0); // Adjust vertical position as needed

        for (Sponsoring post : postList) {
            HBox postEntry = createPostEntry(post);
            anchorPane.getChildren().add(postEntry);
            AnchorPane.setTopAnchor(postEntry, 50.0 + (postIndex * 50.0)); // Adjust vertical position as needed
            postIndex++;
        }
    }

    private HBox createPostEntry(Sponsoring post) {
        HBox hbox = new HBox();
        hbox.setSpacing(15);
        hbox.setPrefWidth(1050);


        Label titleLabel = new Label( post.getName());
        titleLabel.setPrefWidth(140);
        titleLabel.setPrefHeight(70);
        titleLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: #6b9eef;"); // Add text color styling
        hbox.getChildren().add(titleLabel);


        Label dateLabel = new Label( post.getDate().toString());
        dateLabel.setPrefWidth(140);
        dateLabel.setPrefHeight(70);
        dateLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: #6b9eef;");
        hbox.getChildren().add(dateLabel);

        Label localLabel = new Label( post.getDescription());
        localLabel.setPrefWidth(140);
        localLabel.setPrefHeight(70);
        localLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: #6b9eef;"); // Add text color styling
        hbox.getChildren().add(localLabel);

        Label sizeLabel = new Label( post.getType().toString());
        sizeLabel.setPrefWidth(140);
        sizeLabel.setPrefHeight(70);
        sizeLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; " + "-fx-font-weight: bold;"); // Add bold style

        if ("DESACTIVE".equals(post.getType().toString())) {
            sizeLabel.setStyle(sizeLabel.getStyle() + "-fx-text-fill: red;");
        } else {
            sizeLabel.setStyle(sizeLabel.getStyle() + "-fx-text-fill: green;");
        }

        hbox.getChildren().add(sizeLabel);

        Label desLabel = new Label( post.getContrat().toString());
        desLabel.setPrefWidth(150);
        desLabel.setPrefHeight(70);
        desLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px;");
        hbox.getChildren().add(desLabel);



        ImageView imageView = new ImageView(new Image("http://localhost:8000/uploads/" + post.getImage()));
        imageView.setFitWidth(50); // Définit la largeur de l'image

        imageView.setPreserveRatio(true); // Garde le ratio de l'image
        HBox.setMargin(imageView, new Insets(10)); // Marges autour de l'image

        hbox.getChildren().add(imageView);

// Applique le style à la HBox
        hbox.setStyle("-fx-background-color: #FEFFFD; -fx-background-radius: 10px; -fx-padding: 5px; -fx-spacing: 10px;");
        // Add background color and padding





        HBox hbox1 = new HBox();

        Button editButton = new Button("Edit");
        editButton.setOnAction(event -> openEditSponsoringWindow(post));
        editButton.getStyleClass().add("button-edit");
        editButton.setPrefHeight(20);
        editButton.setPadding(new Insets(20, 20, 20, 10));
        hbox1.getChildren().add(editButton);


        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> deleteSponsoring(post));
        deleteButton.getStyleClass().add("button-delete");
        deleteButton.setPadding(new Insets(20, 20, 20, 10));

        hbox1.getChildren().add(deleteButton);


        hbox.getChildren().add(hbox1);



        String qrCodeData = "Welcome ,The name of sponsor " + post.getName() + ", " +
                post.getDescription() + " created on " +
                post.getDate().toString() + " and now is  " +
                post.getType()+ " sign underneath  " ;

        Image qrCodeImage = generateQRCodeImage(qrCodeData, 140, 140); // Generate QR code image

        Button qrCodeButton = new Button("Show QR Code");
        qrCodeButton.setOnAction(event -> showQRCode(qrCodeImage));
        hbox.getChildren().add(qrCodeButton);


        return  hbox;
    }
    private Image generateQRCodeImage(String data, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix;
        try {
            bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            ByteArrayInputStream bis = new ByteArrayInputStream(pngOutputStream.toByteArray());
            return new Image(bis);
        } catch (Exception e) {
            throw new RuntimeException("Error in generating QR code", e);
        }
    }

    private void showQRCode(Image qrCodeImage) {
        Stage qrStage = new Stage();
        qrStage.setTitle("QR Code");
        ImageView qrView = new ImageView(qrCodeImage);
        qrView.setFitHeight(300);  // Set height as per your requirement
        qrView.setFitWidth(300);   // Set width as per your requirement

        AnchorPane qrPane = new AnchorPane();
        qrPane.getChildren().add(qrView);
        qrPane.setStyle("-fx-background-color: white;");  // Ensures the background is white
        AnchorPane.setTopAnchor(qrView, 10.0);
        AnchorPane.setLeftAnchor(qrView, 10.0);

        Scene qrScene = new Scene(qrPane, 320, 320);  // Adjust size as needed
        qrStage.setScene(qrScene);
        qrStage.show();
    }


    private List<Sponsoring> loadDataFromDatabase() throws SQLException {
        SponsoringService postCRUD = new SponsoringService();
        return postCRUD.afficherSponsoring();
    }
    private void openEditSponsoringWindow(Sponsoring sponsoring) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierSponsoring.fxml"));
            Parent root = loader.load();

            modifierSponsoring controller = loader.getController();
            controller.initData(sponsoring);

            Stage stage = new Stage();
            stage.setTitle("Edit Sponsoring");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadDataFromDatabase(); // Refresh list after modifications
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteSponsoring(Sponsoring sponsoring) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this sponsoring?", ButtonType.YES, ButtonType.NO);
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    SponsoringService service = new SponsoringService();
                    service.supprimerSponsoring(sponsoring.getId());
                    loadDataFromDatabase(); // Refresh list
                } catch (SQLException e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Error deleting sponsoring: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
        });
    }
}