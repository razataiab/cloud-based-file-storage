package com.mycompany.javafxapplication1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.File;
import java.util.Optional;

public class RenameFileController extends FileActionController {

    private File fileToRename;

    @FXML
    private Button renameButton;

    @FXML
    private TextField filePathField;

    @FXML
    private void handleSelectFileButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Rename");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir"), "UploadedFiles/" + userName));
        fileToRename = fileChooser.showOpenDialog(new Stage());
        if (fileToRename != null) {
            filePathField.setText(fileToRename.getAbsolutePath());
        }
    }

    @FXML
    private void handleRenameButton(ActionEvent event) {
        if (fileToRename != null) {
            TextInputDialog dialog = new TextInputDialog(fileToRename.getName());
            dialog.setTitle("Rename File");
            dialog.setHeaderText("Rename File");
            dialog.setContentText("Enter new name:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newName -> {
                File newFile = new File(fileToRename.getParent(), newName);
                if (fileToRename.renameTo(newFile)) {
                    showAlert("Success", "File renamed successfully.");
                    closeWindow();
                } else {
                    showAlert("Error", "Failed to rename file.");
                }
            });
        } else {
            showAlert("Error", "No file selected.");
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
        Stage stage = (Stage) renameButton.getScene().getWindow();
        stage.close();
    }
}
