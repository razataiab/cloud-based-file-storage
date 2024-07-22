package com.mycompany.javafxapplication1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileController {

    @FXML
    private TextField filePathField;

    @FXML
    private TextField renameFilePathField;

    @FXML
    private ListView<String> deleteFileListView;

    private File selectedFile;
    private File fileToRename;
    private List<File> filesToDelete;

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
    private void handleSelectFileToRenameButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Rename");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir"), "UploadedFiles"));
        fileToRename = fileChooser.showOpenDialog(new Stage());
        if (fileToRename != null) {
            renameFilePathField.setText(fileToRename.getAbsolutePath());
        }
    }

    @FXML
    private void handleSelectFileToDeleteButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Files to Delete");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir"), "UploadedFiles"));
        filesToDelete = fileChooser.showOpenMultipleDialog(new Stage());
        deleteFileListView.getItems().clear();
        if (filesToDelete != null) {
            for (File file : filesToDelete) {
                deleteFileListView.getItems().add(file.getAbsolutePath());
            }
        }
    }

    @FXML
    private void handleUploadButton(ActionEvent event) {
        if (selectedFile != null) {
            saveFileToDirectory(selectedFile);
            showAlert("Success", "File uploaded successfully.");
            filePathField.clear();
            selectedFile = null;
        } else {
            showAlert("Error", "No file selected.");
        }
    }

    @FXML
    private void handleRenameButton(ActionEvent event) {
        if (fileToRename != null) {
            File destDir = new File(System.getProperty("user.dir"), "UploadedFiles");
            if (fileToRename.getParentFile().equals(destDir)) {
                TextInputDialog dialog = new TextInputDialog(fileToRename.getName());
                dialog.setTitle("Rename File");
                dialog.setHeaderText("Rename File");
                dialog.setContentText("Enter new name:");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(newName -> {
                    File newFile = new File(destDir, newName);
                    if (fileToRename.renameTo(newFile)) {
                        showAlert("Success", "File renamed successfully.");
                        renameFilePathField.clear();
                        fileToRename = null;
                    } else {
                        showAlert("Error", "Failed to rename file.");
                    }
                });
            } else {
                showAlert("Error", "Can only rename files in the UploadedFiles directory.");
            }
        } else {
            showAlert("Error", "No file selected.");
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
            } else {
                showAlert("Error", "Failed to delete some files.");
            }
        } else {
            showAlert("Error", "No files selected.");
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


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
