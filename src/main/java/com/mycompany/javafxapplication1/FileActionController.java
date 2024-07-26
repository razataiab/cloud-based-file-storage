package com.mycompany.javafxapplication1;

import javafx.scene.control.Alert;
import java.io.IOException;

public class FileActionController {

    protected String userName;

    // Base path in the Docker container
    private static final String BASE_CONTAINER_PATH = "/home/ntu-user/NetBeansProjects";

    public void initialize(String userName) {
        this.userName = userName;
        
        // Debug: Check if userName is initialized correctly
        System.out.println("Initializing FileActionController with userName: " + userName);
        
        // Ensure the user's directory exists within the Docker container
        try {
            createUserDirectoryInContainer(userName);
        } catch (IOException | InterruptedException e) {
            // Handle any errors during the creation of the directory
            showAlert("Error", "Failed to create user directory in Docker container: " + e.getMessage());
        }
    }

    protected String getUserContainerPath() {
        String userContainerPath = BASE_CONTAINER_PATH + "/" + userName;
        
        // Debug: Verify the container path being used
        System.out.println("User container path: " + userContainerPath);
        
        return userContainerPath;
    }

    private void createUserDirectoryInContainer(String userName) throws IOException, InterruptedException {
        String command = String.format("docker exec ntu-vm-comp20081 mkdir -p %s/%s", BASE_CONTAINER_PATH, userName);
        
        // Debug: Print the command being executed
        System.out.println("Executing command: " + command);

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("sh", "-c", command);
        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        // Debug: Check the exit code of the process
        System.out.println("Command exit code: " + exitCode);

        if (exitCode != 0) {
            throw new IOException("Failed to create directory for user " + userName + " in Docker container.");
        } else {
            // Debug: Confirm directory creation success
            System.out.println("Successfully created directory for user " + userName + " in Docker container.");
        }
    }

    // Ensure this method is protected so it can be overridden properly
    protected void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}