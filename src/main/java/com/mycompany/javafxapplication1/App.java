package com.mycompany.javafxapplication1;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Stage secondaryStage = new Stage();
        DB myObj = new DB();
        myObj.log("-------- Simple Tutorial on how to make JDBC connection to SQLite DB ------------");

        myObj.log("\n---------- Create table ----------");
        try {
            myObj.createTable(myObj.getTableName());
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("primary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Primary View");
            secondaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        // Run your exit script when the application stops
        runExitScript();
    }

    private void runExitScript() {
        try {
            // Set the path to your shell script
            String scriptPath = "app_exit.sh";

            // Build the process for executing the shell script
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", scriptPath);

            // Start the process
            Process process = processBuilder.start();

            // Wait for the process to complete if you need synchronous execution
            int exitCode = process.waitFor();
            System.out.println("app_exit.sh executed with exit code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
