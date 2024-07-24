package com.mycompany.javafxapplication1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FileController {

    @FXML
    private TableView<File> fileTableView;

    @FXML
    private TableColumn<File, String> fileNameColumn;

    @FXML
    private Button addFileButton;

    @FXML
    private Button renameFileButton;

    @FXML
    private Button deleteFileButton;

    @FXML
    private Button goBackButton;

    private ObservableList<File> userFiles;

    private String userName;

    public void initialize(String userName) {
        this.userName = userName;
        userFiles = FXCollections.observableArrayList();
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        loadUserFiles();
        fileTableView.setItems(userFiles);
    }

    private void loadUserFiles() {
        File userDir = new File(System.getProperty("user.dir"), "UploadedFiles/" + userName);
        if (userDir.exists()) {
            File[] files = userDir.listFiles();
            if (files != null) {
                userFiles.addAll(files);
            }
        }
    }

    @FXML
    private void handleAddFileButton(ActionEvent event) {
        openFileActionView("addFile.fxml", "Add File");
    }

    @FXML
    private void handleRenameFileButton(ActionEvent event) {
        openFileActionView("renameFile.fxml", "Rename File");
    }

    @FXML
    private void handleDeleteFileButton(ActionEvent event) {
        openFileActionView("deleteFile.fxml", "Delete File");
    }

    @FXML
    private void handleGoBackButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
            Parent root = loader.load();
            SecondaryController controller = loader.getController();
            controller.initialize(userName); // Ensure userName is a single String
    
            Stage stage = (Stage) goBackButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openFileActionView(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Pass userName to the action controller
            FileActionController controller = loader.getController();
            controller.initialize(userName);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
