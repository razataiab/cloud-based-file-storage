package com.mycompany.javafxapplication1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileController {

    @FXML
    private TextField filePathField;

    @FXML
    private void handleUploadButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload File");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            filePathField.setText(file.getAbsolutePath());
            saveFileToDirectory(file);
        }
    }

    private void saveFileToDirectory(File file) {
        File destDir = new File(System.getProperty("user.dir"), "UploadedFiles");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File destFile = new File(destDir, file.getName());
        try {
            Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
