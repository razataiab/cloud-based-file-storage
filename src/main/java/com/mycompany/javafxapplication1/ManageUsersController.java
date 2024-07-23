package com.mycompany.javafxapplication1;

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

    private DB db = new DB();

    @FXML
    private void handleBackButtonAction() {
        switchToPrimary();
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

    private void switchToPrimary() {
        Stage primaryStage = new Stage();
        Stage currentStage = (Stage) backButton.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("primary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1250, 900);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            primaryStage.show();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void changeUserPasswordHandler() {
        String userName = delacctname.getText();
        String currentPassword = oldpass.getText();
        String newPassword = changepass1.getText();
        String reEnteredPassword = repass.getText();
        if (userName.isEmpty() || currentPassword.isEmpty() || newPassword.isEmpty() || reEnteredPassword.isEmpty()) {
            showErrorDialog("Input Error", "Username, current password, new password, and re-entered new password must be provided.");
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
            showErrorDialog("Error", "Invalid username or current password.");
        }
    }

    @FXML
    private void deleteAccountBtnHandler() {
        String userNameToDelete = delacctname.getText();
        String password = delacctpass.getText();
        if (userNameToDelete.isEmpty() || password.isEmpty()) {
            showErrorDialog("Input Error", "Username and password must be provided.");
            return;
        }

        if (db.validateUser(userNameToDelete, password)) {
            db.deleteUser(userNameToDelete);
            showDialog("Success", "Account deleted successfully.");
        } else {
            showErrorDialog("Error", "Invalid username or password.");
        }
    }
}
