package edu.esprit.utils;

import javafx.stage.FileChooser;
import java.io.File;
import java.util.List;

public class FileChooserUtil {
    public static List<File> openFileChooser(boolean allowMultiple) {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter for image files
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(imageFilter);

        // Set to user's directory or go to the default C drive if cannot access
        String userDirectoryString = System.getProperty("user.home") + "\\Pictures";
        File userDirectory = new File(userDirectoryString);
        if (!userDirectory.canRead()) {
            userDirectory = new File("c:/");
        }
        fileChooser.setInitialDirectory(userDirectory);

        // Show open file dialog
        if (allowMultiple) {
            return fileChooser.showOpenMultipleDialog(null);
        } else {
            File selectedFile = fileChooser.showOpenDialog(null);
            return selectedFile != null ? List.of(selectedFile) : List.of();
        }
    }
}
