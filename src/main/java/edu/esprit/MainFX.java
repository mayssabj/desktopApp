package edu.esprit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainFX extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("/Reclamation/ReclamationForm.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        URL url = getClass().getResource("/style/statStyle.css");
        if (url == null) {
            System.out.println("Le fichier CSS n'a pas été trouvé.");
        } else {
            System.out.println("Le fichier CSS a été trouvé : " + url);
        }


    }



    public static void main(String[] args) {
        launch();
    }
}