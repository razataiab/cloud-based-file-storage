package com.mycompany.javafxapplication1;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ManageUsersController {

    @FXML
    private Button backButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Label statusLabel;
    @FXML
    private PasswordField oldpass;
    @FXML
    private PasswordField changepass1;
    @FXML
    private PasswordField repass;
    @FXML
    private TextField delacctname;
    @FXML
    private PasswordField delacctpass;
    @FXML
    private Label usernameLabel; // Ensure this is the label showing the username

    private DB db = new DB();
    private String currentUserName;

    @FXML
    private void initialize() {
        // This is the default initialize method for FXML loading
    }

    public void initializeWithUsername(String username) {
        this.currentUserName = username;
        usernameLabel.setText(username);
        delacctname.setText(username); // To pre-fill the username for account deletion
    }

    @FXML
    private void handleBackButtonAction() {
        switchToSecondary();
    }

    private void showDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void switchToSecondary() {
        Stage secondaryStage = new Stage();
        Stage currentStage = (Stage) backButton.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/javafxapplication1/secondary.fxml"));
            Parent root = loader.load();

            // Pass data to SecondaryController
            SecondaryController controller = loader.getController();
            controller.setUsername(currentUserName); // Pass the actual username

            Scene scene = new Scene(root, 1250, 900);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("User Management");
            secondaryStage.show();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchToPrimary() {
        Stage primaryStage = new Stage();
        Stage currentStage = (Stage) logoutButton.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/javafxapplication1/primary.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1250, 900);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Primary View");
            primaryStage.show();

            // Close the current stage after showing the new stage
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void changeUserPasswordHandler() {
        String userName = currentUserName; // Use currentUserName instead of delacctname.getText()
        String currentPassword = oldpass.getText();
        String newPassword = changepass1.getText();
        String reEnteredPassword = repass.getText();
        if (currentPassword.isEmpty() || newPassword.isEmpty() || reEnteredPassword.isEmpty()) {
            showErrorDialog("Input Error", "Current password, new password, and re-entered new password must be provided.");
            return;
        }

        if (!newPassword.equals(reEnteredPassword)) {
            showErrorDialog("Input Error", "New password and re-entered new password do not match.");
            return;
        }

        if (db.validateUser(userName, currentPassword)) {
            db.updatePassword(currentPassword, newPassword);
            showDialog("Success", "Password changed successfully.");
        } else {
            showErrorDialog("Error", "Invalid current password.");
        }
    }

    @FXML
    private void deleteAccountBtnHandler() {
        String userNameToDelete = currentUserName; // Use currentUserName instead of delacctname.getText()
        String password = delacctpass.getText();
        if (password.isEmpty()) {
            showErrorDialog("Input Error", "Password must be provided.");
            return;
        }

        if (db.validateUser(userNameToDelete, password)) {
            db.deleteUser(userNameToDelete);
            showDialog("Success", "Account deleted successfully.");
            switchToPrimary();
        } else {
            showErrorDialog("Error", "Invalid password.");
        }
    }

    @FXML
    private void handleLogoutButtonAction() {
        // Perform logout and switch to the primary screen
        switchToPrimary();
        
        // Show logout confirmation message after switching to the primary screen
        // Use a Platform.runLater to ensure this runs after the UI has been updated
        javafx.application.Platform.runLater(() -> {
            showDialog("Logout", "You have been logged out successfully.");
        });
    }
}
