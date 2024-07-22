package com.mycompany.javafxapplication1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ManageUsersController {
    @FXML
    private Button backButton;
    @FXML
    private Label statusLabel;

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
    private void changeUserNameHandler() {
        // Your code to handle the username change
        System.out.println("Username change handler triggered");
    }

    @FXML
    private void changeUserPasswordHandler() {
        // Your code to handle the username change
        System.out.println("Password change handler triggered");
    }

    @FXML
    private void deleteAccountBtnHandler() {
        // Your code to handle the username change
        System.out.println("Account delete handler triggered");
    }
}
