    package edu.esprit.controller.Avertissement;

    import edu.esprit.entities.Avertissement;
    import edu.esprit.entities.User;
    import edu.esprit.services.ActivityMonitor;
    import edu.esprit.services.ServiceAvertissement;
    import edu.esprit.services.UserService;

    import javafx.application.Platform;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.Initializable;
    import javafx.scene.Node;
    import javafx.scene.control.*;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.layout.HBox;
    import javafx.scene.layout.VBox;
    import javafx.scene.text.Text;

    import javax.mail.*;
    import javax.mail.internet.InternetAddress;
    import javax.mail.internet.MimeMessage;
    import java.net.URL;
    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.Optional;
    import java.util.Properties;
    import java.util.ResourceBundle;
    import java.util.stream.Collectors;

    public class ListAvertissementController implements Initializable {
        private static final int ITEMS_PER_PAGE = 2;

        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        private ServiceAvertissement serviceAvertissement;

        @FXML
        private Button btnSupprimer;
        @FXML
        private Pagination pagination;
        @FXML
        private ListView<Avertissement> avertissementListView;
        private UserService userService = new UserService();

        @FXML
        private Button btnValider;
        @FXML
        private Button btnsendmessage;

        @FXML
        private TextField searchField;

        @FXML
        void SupprimerAvertissement(ActionEvent event) {

        }

        @FXML
        void envoyerMessage(ActionEvent event) {

        }

        @FXML
        void validerAvertissement(ActionEvent event) {

        }
        private ActivityMonitor activityMonitor = new ActivityMonitor();

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            serviceAvertissement = new ServiceAvertissement();
            setupListView();
            int totalAvertissements = serviceAvertissement.countAvertissements();
            pagination.setPageCount((int) Math.ceil((double) totalAvertissements / ITEMS_PER_PAGE));
            loadAvertissements(0); // Load the warnings for the first page
        }


        private void setupListView() {
            avertissementListView.setCellFactory(listView -> new ListCell<Avertissement>() {
                private ImageView imageView = new ImageView();
                private VBox vbox = new VBox(5);
                private Text raison = new Text();
                private Text reportedUsername = new Text();
                private HBox buttonsBox = new HBox(10);
                private Button validerButton = new Button("Validate");
                private Button supprimerButton = new Button("Delete");

                {
                    activityMonitor.recordActivity();
                    // Styling for the buttons
                    validerButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10 20;");
                    supprimerButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10 20;");

                    // Ajouter des effets de survol
                    validerButton.setOnMouseEntered(e -> validerButton.setStyle("-fx-background-color: #45a049;"));
                    validerButton.setOnMouseExited(e -> validerButton.setStyle("-fx-background-color: #4CAF50;"));
                    supprimerButton.setOnMouseEntered(e -> supprimerButton.setStyle("-fx-background-color: #d32f2f;"));
                    supprimerButton.setOnMouseExited(e -> supprimerButton.setStyle("-fx-background-color: #f44336;"));

                    vbox.getChildren().addAll(imageView, raison, reportedUsername, buttonsBox);
                    buttonsBox.getChildren().addAll(validerButton, supprimerButton);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                }

                @Override
                protected void updateItem(Avertissement item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        Text raison = new Text("Reason: " + item.getRaison());
                        raison.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

                        Text reportedUsername = new Text("Reported User: " + item.getReported_username());
                        reportedUsername.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

                        Text confirmationStatus = new Text(item.getConfirmation() == 1 ? "confirmed" : "Not confirmed");
                        confirmationStatus.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: " + (item.getConfirmation() == 1 ? "green" : "red") + ";");

                        try {
                            Image image = new Image(item.getScreenshot(), true);
                            imageView.setImage(image);
                            imageView.setFitHeight(400);
                            imageView.setFitWidth(1200);
                            imageView.setPreserveRatio(true);
                        } catch (Exception e) {
                            imageView.setImage(null);
                        }

                        // Assuming vbox and buttonsBox are already initialized and styled elsewhere
                        vbox.getChildren().setAll(imageView, raison, reportedUsername, confirmationStatus, buttonsBox);
                        setGraphic(vbox);

                        validerButton.setOnAction(event -> validateAvertissement(event));
                        supprimerButton.setOnAction(event -> handleDelete(item));
                    }
                }


                private void handleDelete(Avertissement avertissement) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Deletion confirmation");
                    alert.setContentText("Are you sure you want to remove this warning?");
                    Optional<ButtonType> response = alert.showAndWait();
                    if (response.isPresent() && response.get() == ButtonType.OK) {
                        try {
                            serviceAvertissement.supprimerAvertissement(avertissement.getId());
                            loadAvertissements(0);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        private void loadAvertissements(int pageIndex) {
            try {
                int offset = pageIndex * ITEMS_PER_PAGE;
                ObservableList<Avertissement> list = serviceAvertissement.afficherAvertissement(offset, ITEMS_PER_PAGE);
                avertissementListView.setItems(list);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @FXML
        void filtrerAvertissements() throws SQLException {
            String searchText = searchField.getText().toLowerCase();
            ObservableList<Avertissement> allWarnings = serviceAvertissement.afficherAvertissement();

            ObservableList<Avertissement> filteredList = allWarnings.stream()
                    .filter(warning -> warning.getReported_username().toLowerCase().contains(searchText))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            avertissementListView.setItems(filteredList);

        }

        private void validateAvertissement(ActionEvent event) {
            activityMonitor.recordActivity();
            Avertissement selected = avertissementListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    // Mettre à jour le statut de confirmation dans la base de données
                    selected.setConfirmation(1); // Marquer comme confirmé
                    serviceAvertissement.updateAvertissement(selected); // Vous devez implémenter cette méthode

                    // Incrémenter le compteur d'avertissements de l'utilisateur
                    User user = userService.getUserByUsername(selected.getReported_username());
                    if (user != null) {
                        userService.incrementAvertissementCount(user.getId());
                        //verifier si l'avertissement cout a ete incrementer



                        sendEmail(user.getEmail(), user.getAvertissements_count());
                        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION,  user.getUsername() + " Warning confirmed.");
                        infoAlert.showAndWait();
                    } else {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR, "User not found.");
                        errorAlert.showAndWait();
                    }
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Error validating the warning.");
                    errorAlert.showAndWait();
                    e.printStackTrace();
                }
            } else {
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "Please select a warning to validate.");
                infoAlert.showAndWait();
            }



        }




        //slwt brqx kzbd lcyq
        public void sendEmail(String to, int avertissementCount) {
            String host = "smtp.gmail.com"; // Serveur SMTP de Gmail
            final String username = "fouedrhaiem882@gmail.com"; // Votre adresse Gmail
            final String password = "slwt brqx kzbd lcyq"; // Votre mot de passe Gmail ou mot de passe d'application

            Properties properties = new Properties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true"); // Activez TLS

            javax.mail.Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                @Override
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                message.setSubject("Notification d'avertissement");
                message.setText("Cher utilisateur, votre nombre total d'avertissements est maintenant de : " + avertissementCount);

                Transport.send(message);
                System.out.println("Email envoyé avec succès !");
            } catch (MessagingException mex) {
                mex.printStackTrace();
            }
        }


        @FXML
        void handlePageChange() {
            int pageIndex = pagination.getCurrentPageIndex();
            loadAvertissements(pageIndex);
        }



    }