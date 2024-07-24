package com.mycompany.javafxapplication1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class DeleteFileController extends FileActionController {

    @FXML
    private ListView<String> deleteFileListView;

    @FXML
    private TextField filePathField;

    private List<File> filesToDelete;

    @FXML
    private void handleSelectFileToDeleteButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Files to Delete");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir"), "UploadedFiles/" + userName));
        filesToDelete = fileChooser.showOpenMultipleDialog(new Stage());
        deleteFileListView.getItems().clear();
        if (filesToDelete != null) {
            for (File file : filesToDelete) {
                deleteFileListView.getItems().add(file.getAbsolutePath());
            }
            if (!filesToDelete.isEmpty()) {
                filePathField.setText(filesToDelete.get(0).getAbsolutePath());
            }
        }
    }

    @FXML
    private void handleDeleteButton(ActionEvent event) {
        if (filesToDelete != null && !filesToDelete.isEmpty()) {
            boolean allDeleted = true;
            for (File file : filesToDelete) {
                if (!file.delete()) {
                    allDeleted = false;
                }
            }
            if (allDeleted) {
                showAlert("Success", "All selected files deleted successfully.");
                deleteFileListView.getItems().clear();
                filesToDelete = null;
                filePathField.clear();
                closeWindow();
            } else {
                showAlert("Error", "Failed to delete some files.");
            }
        } else {
            showAlert("Error", "No files selected.");
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
        Stage stage = (Stage) deleteFileListView.getScene().getWindow();
        stage.close();
    }
}
