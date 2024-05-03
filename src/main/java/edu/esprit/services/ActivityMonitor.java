package edu.esprit.services;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

import java.util.Timer;
import java.util.TimerTask;

public class ActivityMonitor {
    private static final long MAX_REQUESTS_PER_MINUTE = 10; // Limite d'activité intense par minute
    private long requestCount = 0;
    private long startTime = System.currentTimeMillis();
    private Timer timer = new Timer();
    private boolean alertScheduled = false;

    public synchronized void recordActivity() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - startTime > 60000) { // 1 minute
            startTime = currentTime;
            requestCount = 0;
            if (alertScheduled) {
                timer.cancel(); // Annuler le timer précédent
                timer = new Timer(); // Créer un nouveau timer pour la prochaine période
                alertScheduled = false;
            }
        }
        requestCount++;
        if (requestCount > MAX_REQUESTS_PER_MINUTE && !alertScheduled) {
            scheduleAlert();
            alertScheduled = true;
        }
    }

    private void scheduleAlert() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Alerte d'Activité Élevée");
                    alert.setHeaderText("Utilisation Haute Détectée");
                    alert.setContentText("Une activité anormalement élevée a été détectée.");

                    // Styliser l'alerte
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.setStyle("-fx-background-color: black; -fx-text-fill: white;"); // Couleur de fond et texte de l'alerte

                    // Styliser les boutons
                    dialogPane.getButtonTypes().stream()
                            .map(dialogPane::lookupButton)
                            .forEach(button -> button.setStyle("-fx-background-color: darkgray; -fx-text-fill: black;"));

                    alert.showAndWait();
                });
            }
        }, 60000); // Délai de 60 000 millisecondes (1 minute)
}}
