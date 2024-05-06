package edu.esprit.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.itextpdf.text.pdf.qrcode.WriterException;
import edu.esprit.entities.Post;
import edu.esprit.entities.User;
import edu.esprit.services.PostCRUD;
import edu.esprit.services.UserService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.awt.Desktop;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class showPostAdminController {

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
        List<Post> postList = loadDataFromDatabase();

        // Create header row
        HBox headerRow = new HBox();
        headerRow.setSpacing(8);
        headerRow.setPrefWidth(1300);
        headerRow.setStyle("-fx-background-color: #6b9eef; -fx-padding: 10px; -fx-spacing: 10px;");

        // Header labels
        Label titleLabel = new Label("Titre");
        titleLabel.setPrefWidth(200);
        titleLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(titleLabel);

        Label dateLabel = new Label("Date");
        dateLabel.setPrefWidth(200);
        dateLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(dateLabel);

        Label localLabel = new Label("Local");
        localLabel.setPrefWidth(200);
        localLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(localLabel);

        Label sizeLabel = new Label("Type");
        sizeLabel.setPrefWidth(200);
        sizeLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(sizeLabel); // Add bold style

        Label desLabel = new Label("Description");
        desLabel.setPrefWidth(200);
        desLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(desLabel);

        Label userLabel = new Label("User");
        userLabel.setPrefWidth(200);
        userLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(userLabel);

        Label Action = new Label("Delete");
        Action.setPrefWidth(200);
        Action.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: white;");
        headerRow.getChildren().add(Action);

        // Add header row to the AnchorPane
        anchorPane.getChildren().add(headerRow);
        AnchorPane.setTopAnchor(headerRow, 50.0); // Adjust vertical position as needed

        for (Post post : postList) {
            HBox postEntry = createPostEntry(post);
            anchorPane.getChildren().add(postEntry);
            AnchorPane.setTopAnchor(postEntry, 50.0 + (postIndex * 50.0)); // Adjust vertical position as needed
            postIndex++;
        }
    }

    private HBox createPostEntry(Post post) {
        HBox hbox = new HBox();
        hbox.setSpacing(8);
        hbox.setPrefWidth(1300);

        Label titleLabel = new Label( post.getTitre());
        titleLabel.setPrefWidth(200);
        titleLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: #6b9eef;"); // Add text color styling
        hbox.getChildren().add(titleLabel);

        Label dateLabel = new Label( post.getDate().toString());
        dateLabel.setPrefWidth(200);
        dateLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: #6b9eef;");
        hbox.getChildren().add(dateLabel);

        Label localLabel = new Label( post.getPlace());
        localLabel.setPrefWidth(200);
        localLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; -fx-text-fill: #6b9eef;"); // Add text color styling
        hbox.getChildren().add(localLabel);

        Label sizeLabel = new Label( post.getType().toString());
        sizeLabel.setPrefWidth(200);
        sizeLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px; " + "-fx-font-weight: bold;"); // Add bold style

        if ("LOST".equals(post.getType())) {
            sizeLabel.setStyle(sizeLabel.getStyle() + "-fx-text-fill: red;");
        } else {
            sizeLabel.setStyle(sizeLabel.getStyle() + "-fx-text-fill: green;");
        }

        hbox.getChildren().add(sizeLabel);

        Label desLabel = new Label( post.getDescription());
        desLabel.setPrefWidth(200);
        desLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px;");
        hbox.getChildren().add(desLabel);

        int id = post.getuser_id();
        UserService userService = new UserService();
        User user = userService.getUserById(id);

        Label nameuser = new Label(user.getEmail().toString());
        nameuser.setPrefWidth(200);
        nameuser.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 14px;");
        hbox.getChildren().add(nameuser);

        Button deleteButton = new Button();
        ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/delete-icon.png")));
        deleteIcon.setFitWidth(16);
        deleteIcon.setFitHeight(16);
        deleteButton.setGraphic(deleteIcon);
        deleteButton.getStyleClass().add("button-delete");
        deleteButton.setFont(new Font("System Bold", 18));
        deleteButton.setTextFill(Color.valueOf("#828282"));
        deleteButton.setOnAction(event -> deletePost(post));
        hbox.getChildren().add(deleteButton);

        hbox.setStyle("-fx-background-color: #FEFFFD; -fx-background-radius: 10px; -fx-padding: 10px; -fx-spacing: 10px;"); // Add background color and padding

        return hbox;
    }

    private void deletePost(Post post) {
        PostCRUD service = new PostCRUD();
        try {
            service.supprimer(post.getId());
            System.out.println("test");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Post> loadDataFromDatabase() throws SQLException {
        PostCRUD postCRUD = new PostCRUD();
        return postCRUD.afficher();
    }



    @FXML
    void handleSaveFile() throws FileNotFoundException, DocumentException, BadElementException, IOException, SQLException, WriterException {
        com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
        PdfWriter.getInstance((com.itextpdf.text.Document) doc, new FileOutputStream("User_list.pdf"));
        ((com.itextpdf.text.Document) doc).open();
        String format = "dd/mm/yy hh:mm";
        SimpleDateFormat formater = new SimpleDateFormat(format);
        java.util.Date date = new java.util.Date();
        Paragraph paragraph = new Paragraph("LOST & FOUND");
        paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        paragraph.setAlignment(BaseColor.BLUE.getBlue());
        ((com.itextpdf.text.Document) doc).add(paragraph);
        ((com.itextpdf.text.Document) doc).add(new Paragraph("\n"));
        ((com.itextpdf.text.Document) doc).add(new Paragraph("All Post information in this table :" + "\n"));
        ((com.itextpdf.text.Document) doc).add(new Paragraph("\n"));
        PdfPTable t = new PdfPTable(5); // Augmentez le nombre de colonnes pour inclure le QR code
        PdfPCell cell = new PdfPCell(new Phrase("titre"));
        cell.setBackgroundColor(BaseColor.ORANGE);
        t.addCell(cell);

        PdfPCell cell1 = new PdfPCell(new Phrase("Description"));
        cell1.setBackgroundColor(BaseColor.ORANGE);
        t.addCell(cell1);

        PdfPCell cell2 = new PdfPCell(new Phrase("place"));
        cell2.setBackgroundColor(BaseColor.ORANGE);
        t.addCell(cell2);

        PdfPCell cell3 = new PdfPCell(new Phrase("type post"));
        cell3.setBackgroundColor(BaseColor.ORANGE);
        t.addCell(cell3);

        PdfPCell cell4 = new PdfPCell(new Phrase("date"));
        cell4.setBackgroundColor(BaseColor.ORANGE);
        t.addCell(cell4);


        // Set the total width of the table to the width of the page
        t.setTotalWidth(((com.itextpdf.text.Document) doc).getPageSize().getWidth() - ((com.itextpdf.text.Document) doc).leftMargin() - ((com.itextpdf.text.Document) doc).rightMargin());


        // Récupérez la liste des utilisateurs
        List<Post> userList = loadDataFromDatabase();
        for (Post post : userList) {
            t.addCell(post.getTitre());
            t.addCell(post.getDescription());
            t.addCell(post.getPlace());
            t.addCell(post.getType().toString());
            t.addCell(post.getDate().toString());
        }
        ((com.itextpdf.text.Document) doc).add(t);
        Desktop.getDesktop().open(new File("User_list.pdf"));
        ((com.itextpdf.text.Document) doc).close();
    }
}