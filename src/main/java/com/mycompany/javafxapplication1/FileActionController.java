package com.mycompany.javafxapplication1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javafx.scene.control.Alert;

public class FileActionController {

    protected String userName;

    // Base path in the Docker container (mapped to the Docker volume)
    private static final String BASE_CONTAINER_PATH = "/home/razataiab/storage"; // Updated to reflect volume mount point

    public void initialize(String userName) {
        this.userName = userName;
        
        // Debug: Check if userName is initialized correctly
        System.out.println("Initializing FileActionController with userName: " + userName);
        
        // Ensure the user's directory exists within the Docker container
        try {
            ensureUserDirectoryExists();
        } catch (IOException e) {
            // Handle any errors during the creation of the directory
            showAlert("Error", "Failed to ensure user directory in Docker volume: " + e.getMessage());
        }
    }

    protected String getUserContainerPath() {
        return BASE_CONTAINER_PATH + "/" + userName;
    }

    private void ensureUserDirectoryExists() throws IOException {
        String userDir = getUserContainerPath();
        String command = String.format("docker exec %s mkdir -p %s", "file-storage", userDir);

        // Debug: Print the command being executed
        System.out.println("Ensuring directory exists with command: " + command);

        ProcessBuilder processBuilder = new ProcessBuilder("sh", "-c", command);
        Process process = processBuilder.start();
        
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                // Read error stream to understand the issue
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    System.err.println("Error: " + errorLine);  // Debugging error output
                }
                throw new IOException("Failed to ensure user directory with exit code: " + exitCode);
            }
            System.out.println("Directory is ensured: " + userDir);
        } catch (InterruptedException e) {
            throw new IOException("Interrupted while ensuring directory existence", e);
        }
    }

    protected void copyFileToVolume(String sourceFilePath, String destinationFileName) throws IOException {
        String destinationPath = getUserContainerPath() + "/" + destinationFileName;

        // Debug: Print file copy paths
        System.out.println("Copying file from: " + sourceFilePath + " to: " + destinationPath);

        Files.copy(Paths.get(sourceFilePath), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
    }

    protected void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
