package com.mycompany.javafxapplication1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class FileController {

    @FXML
    private TableView<FileInfo> fileTableView;  // Changed to FileInfo

    @FXML
    private TableColumn<FileInfo, String> fileNameColumn;  // Changed to FileInfo

    @FXML
    private Button addFileButton;

    @FXML
    private Button renameFileButton;

    @FXML
    private Button deleteFileButton;

    @FXML
    private Button goBackButton;

    private ObservableList<FileInfo> userFiles;

    private String userName;

    private static final String CONTAINER_NAME = "file-storage"; // Make sure this matches your container's name

    private static final String BASE_CONTAINER_PATH = "/home/razataiab/storage"; // Path in the container

    public void initialize(String userName) {
        this.userName = userName;
        userFiles = FXCollections.observableArrayList();
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        checkAndInitializeUserDirectory();
        loadUserFiles();
        fileTableView.setItems(userFiles);
    }

    private void checkAndInitializeUserDirectory() {
        String userDir = BASE_CONTAINER_PATH + "/" + userName;

        try {
            // Command to ensure the directory exists
            String checkDirCommand = String.format("docker exec %s mkdir -p %s", CONTAINER_NAME, userDir);
            System.out.println("Checking/creating user directory: " + userDir);
            ProcessBuilder processBuilder = new ProcessBuilder("sh", "-c", checkDirCommand);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("User directory is ready: " + userDir);
            } else {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    System.err.println("Error: " + errorLine);  // Debugging error output
                }
                System.err.println("Failed to ensure user directory with exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loadUserFiles() {
        String userDir = BASE_CONTAINER_PATH + "/" + userName;

        // Debug: Print the directory being accessed
        System.out.println("Accessing directory: " + userDir);

        // Execute the Docker command to list files
        try {
            String command = String.format("docker exec %s ls %s", CONTAINER_NAME, userDir);
            System.out.println("Executing command: " + command);  // Debugging statement
            ProcessBuilder processBuilder = new ProcessBuilder("sh", "-c", command);
            Process process = processBuilder.start();

            // Read the command output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int fileCount = 0;  // Counter for debugging
            userFiles.clear(); // Clear current list before adding new items
            while ((line = reader.readLine()) != null) {
                System.out.println("File found: " + line); // Debugging statement
                userFiles.add(new FileInfo(line));  // Use FileInfo
                fileCount++;  // Increment file count
            }

            // Debug: Print total number of files found
            System.out.println("Total files retrieved: " + fileCount);

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
            controller.initialize(userName);

            Stage stage = (Stage) goBackButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openFileActionView(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Pass userName to the action controller
            FileActionController controller = loader.getController();
            controller.initialize(userName);

            // Create a new stage for the file operation window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);

            // Set up an event handler to refresh the TableView after the window is closed
            stage.setOnHiding((WindowEvent event) -> {
                System.out.println(title + " window closed, refreshing file list...");
                loadUserFiles();  // Refresh the file list when the window is closed
            });

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
