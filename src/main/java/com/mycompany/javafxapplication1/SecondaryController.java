package com.mycompany.javafxapplication1;

import java.io.File;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SecondaryController {

    @FXML
    private Text fileText;

    @FXML
    private Label usernameLabel;

    @FXML
    private TableView<User> dataTableView;

    @FXML
    private Button manageaccountbtn;

    @FXML
    private Button secondaryButton;

    @FXML
    private Button fileBtn;

    private String username;

    @FXML
    private void initialize() {
        populateTable();
        if (username != null) {
            fileText.setText(username);
            usernameLabel.setText(username); // Display the username
        }
    }

    @FXML
    private void manageAccountBtnHandler(ActionEvent event) {
        switchScene("manageuser.fxml", "Manage User", manageaccountbtn);
    }

    @FXML
    private void switchToPrimary() {
        String directoryPath = "/home/ntu-user/NetBeansProjects/Java-Coursework/cwk (1)/cwk/JavaFXApplication1/ProgramDirectories/Files";
        deleteDirectory(new File(directoryPath));
        createDirectory(directoryPath);
        switchScene("primary.fxml", "Login", secondaryButton);
    }

    @FXML
    private void switchToFile() {
        switchScene("fileView.fxml", "Files", fileBtn, username);
    }

    public void setUsername(String username) {
        this.username = username;
        if (fileText != null) {
            fileText.setText(username);
        }
        if (usernameLabel != null) {
            usernameLabel.setText(username); // Set the username label
        }
    }

    public void initialize(String username) {
        this.username = username;
        populateTable();
    }

    private void switchScene(String fxmlFile, String title, Button button) {
        Stage currentStage = (Stage) button.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            if (fxmlFile.equals("secondary.fxml")) {
                SecondaryController controller = loader.getController();
                controller.setUsername(username);
            }

            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle(title);
            newStage.show();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Overloaded method to switch scene with username
    private void switchScene(String fxmlFile, String title, Button button, String userName) {
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) button.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            FileController controller = loader.getController();
            controller.initialize(userName);
            
            Scene scene = new Scene(root, 1250, 900);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle(title);
            secondaryStage.show();
            primaryStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void populateTable() {
        DB myObj = new DB();
        ObservableList<User> users = FXCollections.observableArrayList();

        try {
            users.addAll(myObj.getDataFromTable());
        } catch (Exception e) {
            e.printStackTrace();
        }

        TableColumn<User, String> userColumn = new TableColumn<>("User");
        userColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> passColumn = new TableColumn<>("Password");
        passColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        dataTableView.getColumns().clear();
        dataTableView.getColumns().addAll(userColumn, passColumn);
        dataTableView.setItems(users);
    }

    private static void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        } else {
            System.err.println("Directory does not exist: " + directory.getAbsolutePath());
        }
    }

    private static void createDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Directory created: " + directoryPath);
            } else {
                System.err.println("Failed to create directory.");
            }
        }
    }
}