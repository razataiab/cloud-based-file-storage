package com.mycompany.javafxapplication1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class RenameFileController extends FileActionController {

    @FXML
    private Button renameButton;

    @FXML
    private ListView<String> fileListView;

    // Path to Docker container directory
    private static final String CONTAINER_PATH = "/home/ntu-user/NetBeansProjects";

    private ObservableList<String> userFiles;
    private String userName;

    public void initialize(String userName) {
        this.userName = userName;
        userFiles = FXCollections.observableArrayList();
        loadUserFiles();
        fileListView.setItems(userFiles);
    }

    private void loadUserFiles() {
        // Use the directory path within the Docker-mounted volume
        String userDir = CONTAINER_PATH + "/" + userName;
        
        // Debug: Print the directory being accessed
        System.out.println("Accessing directory: " + userDir);

        // Execute the Docker command to list files
        try {
            // Build and execute the Docker command
            String command = "docker exec ntu-vm-comp20081 ls " + userDir;
            System.out.println("Executing command: " + command);  // Debugging statement
            ProcessBuilder processBuilder = new ProcessBuilder("sh", "-c", command);
            Process process = processBuilder.start();

            // Read the command output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            userFiles.clear(); // Clear current list before adding new items
            while ((line = reader.readLine()) != null) {
                System.out.println("File found: " + line); // Debugging statement
                userFiles.add(line);  // Add file names to the list
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                // Read error stream to understand the issue
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    System.err.println("Error: " + errorLine);  // Debugging error output
                }
                System.err.println("Failed to retrieve files from Docker container with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRenameButton(ActionEvent event) {
        String selectedFile = fileListView.getSelectionModel().getSelectedItem();
        if (selectedFile != null) {
            TextInputDialog dialog = new TextInputDialog(selectedFile);
            dialog.setTitle("Rename File");
            dialog.setHeaderText("Rename File");
            dialog.setContentText("Enter new name:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newName -> {
                renameFileInDocker(selectedFile, newName);
            });
        } else {
            showAlert("Error", "No file selected.");
        }
    }

    private void renameFileInDocker(String oldName, String newName) {
        String userDir = CONTAINER_PATH + "/" + userName;
        String command = String.format(
            "docker exec ntu-vm-comp20081 mv %s/%s %s/%s",
            userDir, oldName, userDir, newName
        );
        System.out.println("Executing rename command: " + command); // Debugging

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("sh", "-c", command);
            Process process = processBuilder.start();

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                showAlert("Success", "File renamed successfully.");
                loadUserFiles(); // Refresh the file list
            } else {
                // Read error stream to understand the issue
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    System.err.println("Error: " + errorLine);  // Debugging error output
                }
                showAlert("Error", "Failed to rename file. Check console for details.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            showAlert("Error", "Exception occurred during rename operation.");
        }
    }

    // Override method with protected access
    @Override
    protected void showAlert(String title, String message) {
        super.showAlert(title, message); // Optionally call super to reuse common behavior
    }

    private void closeWindow() {
        Stage stage = (Stage) renameButton.getScene().getWindow();
        stage.close();
    }
}

