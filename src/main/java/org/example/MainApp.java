package org.example; // Use your actual package name

import Controller.AfficherPostController;
import Controller.AjouterPostController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.PostCRUD;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // The leading "/" is important, it means the root of the classpath
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Post.fxml"));
            Parent root = loader.load();
            AfficherPostController controller = loader.getController();// Correctly cast to AjouterPostController
            // If AjouterPostController has a method you want to call, do it here. For example:
            // controller.someMethod();
            Scene scene = new Scene(root);
            primaryStage.setTitle("Lost AND Found");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
