package com.mycompany.javafxapplication1;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
            // Debugging: Print the selected file path
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        } else {
            // Debugging: Print if no file was selected
            System.out.println("No file selected.");
        }
    }

    @FXML
    private void handleUploadButton(ActionEvent event) {
        if (selectedFile != null) {
            try {
                // Save the file to the container's volume path
                copyFileToDockerVolume(selectedFile);
                showAlert("Success", "File uploaded successfully.");
                closeWindow();
            } catch (IOException | InterruptedException e) {
                showAlert("Error", "Failed to upload file: " + e.getMessage());
                // Debugging: Print stack trace for errors
                e.printStackTrace();
            }
        } else {
            showAlert("Error", "No file selected.");
            // Debugging: Print error message
            System.out.println("Upload button clicked but no file was selected.");
        }
    }

    private void copyFileToDockerVolume(File file) throws IOException, InterruptedException {
        // Use the user-specific container path
        String userContainerPath = getUserContainerPath();
        String containerName = "file-storage"; // Ensure this matches your container's name

        // Prepare the Docker command to copy the file into the container's volume path
        String command = String.format("docker cp %s %s:%s/%s",
                file.getAbsolutePath(), containerName, userContainerPath, file.getName());

        // Debugging: Print the Docker command being executed
        System.out.println("Executing command: " + command);

        // Using ProcessBuilder to execute the Docker command
        ProcessBuilder processBuilder = new ProcessBuilder("sh", "-c", command);
        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        // Debugging: Print the exit code of the command
        System.out.println("Command exited with code: " + exitCode);

        if (exitCode != 0) {
            // Read error stream to understand the issue
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println("Error: " + errorLine);  // Debugging error output
            }
            throw new IOException("Failed to copy file to Docker container with exit code: " + exitCode);
        }

        // Debugging: Print the result of the file copy operation
        System.out.println("File copied to container path: " + userContainerPath + "/" + file.getName());
    }

    private void closeWindow() {
        Stage stage = (Stage) uploadButton.getScene().getWindow();
        stage.close();
    }
}