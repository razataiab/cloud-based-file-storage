package com.mycompany.javafxapplication1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class AddFileController extends FileActionController {

    private File selectedFile;

    @FXML
    private Button uploadButton;

    @FXML
    private TextField filePathField;

    @FXML
    private void handleSelectFileButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Upload");
        selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleUploadButton(ActionEvent event) {
        if (selectedFile != null) {
            saveFileToDirectory(selectedFile);
            showAlert("Success", "File uploaded successfully.");
            closeWindow();
        } else {
            showAlert("Error", "No file selected.");
        }
    }

    private void saveFileToDirectory(File file) {
        File userDir = new File(System.getProperty("user.dir"), "UploadedFiles/" + userName);
        if (!userDir.exists()) {
            userDir.mkdirs();
        }
        File destFile = new File(userDir, file.getName());
        try {
            Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) uploadButton.getScene().getWindow();
        stage.close();
    }
}
