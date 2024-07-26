package com.mycompany.javafxapplication1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {

    @FXML
    private Button registerBtn;

    @FXML
    private Button backLoginBtn;

    @FXML
    private PasswordField passPasswordField;

    @FXML
    private PasswordField rePassPasswordField;

    @FXML
    private TextField userTextField;

    @FXML
    private void registerBtnHandler(ActionEvent event) {
        Stage primaryStage = (Stage) registerBtn.getScene().getWindow();
        DB myObj = new DB();
        try {
            if (passPasswordField.getText().equals(rePassPasswordField.getText())) {
                String username = userTextField.getText();
                String password = passPasswordField.getText();
                
                // Add user to the database
                myObj.addDataToDB(username, password);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root, 1250, 900);

                SecondaryController controller = loader.getController();
                controller.setUsername(username); // Pass the username

                Stage secondaryStage = new Stage();
                secondaryStage.setScene(scene);
                secondaryStage.setTitle("Home");
                secondaryStage.show();
                primaryStage.close();
            } else {
                // Show an error or indication to the user that passwords do not match
                System.out.println("Passwords do not match.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backLoginBtnHandler(ActionEvent event) {
        Stage primaryStage = (Stage) backLoginBtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1250, 900);

            Stage secondaryStage = new Stage();
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Login");
            secondaryStage.show();
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
