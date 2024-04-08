package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class dashbord {
    @FXML
    private VBox vboxdash;
    @FXML
    void showpost(MouseEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/showPostAdminController.fxml"));
            Node eventFXML = loader.load();

            vboxdash.getChildren().clear();

            vboxdash.getChildren().add(eventFXML);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}