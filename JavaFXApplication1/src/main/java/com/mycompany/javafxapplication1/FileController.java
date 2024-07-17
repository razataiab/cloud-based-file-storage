// package com.mycompany.javafxapplication1;

// import java.io.File;
// import java.io.FileInputStream;
// import java.io.FileOutputStream;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.io.FileWriter;
// import java.io.IOException;
// import java.nio.file.Files;
// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.scene.control.Button;
// import javafx.scene.control.TextField;
// import javafx.scene.control.TextArea;
// import javafx.stage.Stage;
// import javafx.fxml.FXMLLoader;
// import javafx.scene.Parent;
// import javafx.scene.Scene;
// import javafx.stage.FileChooser;
// import javafx.stage.FileChooser.ExtensionFilter;
// import java.nio.file.StandardCopyOption;
// import java.util.Arrays;
// import com.mycompany.javafxapplication1.ScpTo;

// public class fileController {

//     @FXML
//     private TextField fileNameField;

//     @FXML
//     private TextField fileTextField;

//     @FXML
//     private TextField deleteFileField;

//     @FXML
//     private TextField renameFileField;
    
//     @FXML
//     private TextField uploadFileTextField;

//     @FXML
//     private Button homeBtn;
    
//     @FXML
//     private Button updateFileBtn;
    
//     @FXML
//     private Button downloadFileBtn;

//     @FXML
//     private TextArea warningField;

//     @FXML
//     private File userFile;
    
//     //method for creating a new file
//     @FXML
//     public void createFileHandler(ActionEvent event) {
//         String userInput = fileNameField.getText();
//         String directoryPath = "/home/ntu-user/Downloads/"; // Specify the directory where you want to save the file
//         Path filePath = Paths.get(directoryPath, userInput);
//         userFile = new File(filePath.toString());
        
//         try {
//             userFile = new File(directoryPath, userInput); // Creates a new File object with the specified directory and name
//             if (userFile.exists()) { // Checks if the file already exists
//                 System.out.println("File already exists.");
//                 warningField.setText("File already exists.\n");
            
//             } else {
//                 if (userFile.createNewFile()) { // Attempts to create the new file
//                     System.out.println("File created: " + userFile.getName());
//                     warningField.setText("File created: " + userFile.getName());
                
//                     // No need to upload the file after creation
//                 } else {
//                     System.out.println("File could not be created.");
//                     warningField.setText("File could not be created.\n");
//                 }
//             }
//         } catch (IOException e) {
//             System.out.println("An error occurred.");
//             warningField.setText("An error occurred.\n");
//             warningField.setText(e.toString());
//         }
//     }
    
//     //method for appending content to an existing file
//     @FXML
//     public void changingFile(ActionEvent event) throws IOException {
//         String userInput = fileTextField.getText();

//         if (userFile != null) { //Checks if a file has been specified
//             try (FileWriter writer = new FileWriter(userFile, true)) {
//                 writer.write(userInput + "\n");  // Appends content and add a newline
//                 System.out.println("Content Entered.");
//                 warningField.setText("Content Entered.\n");
//             } catch (IOException e) {
//                 System.out.println("An error occurred while writing to the file.");
//                 warningField.setText("An error occurred while writing to the file.\n");
//                 warningField.setText(e.toString());
//             }
//         } else {
//             System.out.println("No file specified.");
//             warningField.setText("No file specified.\n");
//         }
//     }
    
//     // method for handling file upload
//     @FXML
//     public void uploadFile(ActionEvent event) {
//         FileChooser fileChooser = new FileChooser();
//         fileChooser.setTitle("Choose File to Upload");
        
//         // Set the initial directory for the file chooser
//         fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        
        
//         // Show the file chooser dialog
//         File selectedFile = fileChooser.showOpenDialog(null);
//         if (selectedFile != null) {
//             try {
//                 // Specify the containers where you want to upload the file
//                 String[] containers = {"container1", "container2", "container3", "container4"};
//                 for (String container : containers) {
//                     // Perform upload operations for each container
//                     uploadToContainer(selectedFile, container);
//                 }
//             } catch (Exception e) {
//                 warningField.setText("Error uploading file: " + e.getMessage());
//             }
//         } else {
//             warningField.setText("File selection canceled.");
//         }
//     }
    
//     private void uploadToContainer(File file, String container) {
//     // Implement upload logic for each container here
//     // This could involve sending the file to a remote server or another system
//     // You may need to use different APIs or protocols depending on your setup
    
//     // For demonstration purposes, let's print a message indicating the upload
//     System.out.println("Uploading file " + file.getName() + " to container " + container);
    
//     // Replace the following placeholders with your actual connection details and commands
//     String REMOTE_HOST = "172.19.0.0"; // Replace with the IP address of your Docker container
//     String USERNAME = "root"; // Replace with the username for SSH connection
//     String PASSWORD = "ntu-user"; // Replace with the password for SSH connection
//     int REMOTE_PORT = 22; // Replace with the port for SSH connection
    
//     try {
//         // Call the static dockerConnect method from the ScpTo class to upload the file
//         ScpTo.dockerConnect(file.getAbsolutePath(), container + "/" + file.getName(), REMOTE_HOST, USERNAME);
        
//         System.out.println("File uploaded successfully to container " + container);
//     } catch (Exception e) {
//         System.out.println("Error uploading file to container " + container + ": " + e.getMessage());
//     }
// }
    
//     //method for deleting a file
//     @FXML
//     public void deleteFile(ActionEvent event) throws IOException {
//         String userInput = deleteFileField.getText();   
//         if (userFile != null && userInput.equals(userFile.getName())) { // Checks if a file has been specified and if the specified file matches the userFile
//             try {
//                 // Checks if the file exists before attempting to delete
//                 if (userFile.exists()) {
//                     if (userFile.delete()) { // Attempts to delete the file
//                         System.out.println("Deleted " + userFile.getName());
//                         warningField.setText("Deleted " + userFile.getName());
//                     } else {
//                         System.out.println("Failed to delete " + userFile.getName());
//                         warningField.setText("Failed to delete " + userFile.getName());
//                     }
//                 } else {
//                     System.out.println("File does not exist: " + userFile.getName());
//                     warningField.setText("File does not exist: " + userFile.getName());
//                 }
//             } catch (SecurityException e) {
//                 System.out.println("Security exception while deleting the file: " + e.getMessage()); // Handles security exceptions related to file deletion
//                 warningField.setText("Security exception while deleting the file: " + e.getMessage());
//             }
//         }
//     }
   
//     @FXML
//     public static void splitFileIntoChunks(String filePath, int numberOfChunks, String directoryPath) {
//         try (FileInputStream fis = new FileInputStream(filePath)) {
//             File file = new File(filePath);
//             long fileSize = file.length();
//             long chunkSize = fileSize / numberOfChunks;
//             String[] ipAddress = {"172.18.0.4", "172.18.0.6", "172.18.0.3", "172.18.0.4"};
            
//             for (int i = 1; i <= numberOfChunks; i++) {
//                 try (FileOutputStream fos = new FileOutputStream(directoryPath + "/chunk" + i + ".bin")) {
//                     byte[] buffer = new byte[(int) chunkSize];
//                     int bytesRead;
                    
//                     // Read from the input stream
//                     if ((bytesRead = fis.read(buffer)) != -1) {
//                         // If the last chunk is smaller, adjust the buffer size
//                         if (i == numberOfChunks && bytesRead < chunkSize) {
//                             buffer = Arrays.copyOf(buffer, bytesRead);
//                         }

//                         // Write to the output stream
//                         fos.write(buffer);

//                         // Perform other operations here, e.g., SCP and file deletion
//                         ScpTo.dockerConnect("chunk" + i + ".bin", "Vchunk" + i + ".bin", ipAddress[i], "create");
//                         deleteChunkFile(directoryPath + "/chunk" + i + ".bin");
//                     }
//                 }
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

    
//     public static void main(String[] args) {
//         String filePath = "/home/ntu-user/NetBeansProjects/test/";
//         int numberOfChunks = 4;
//         String directoryPath = "/directory/to/save/chunks";

//         splitFileIntoChunks(filePath, numberOfChunks, directoryPath);
//     }

//     private static void deleteChunkFile(String filePath) {
//         try {
//             Files.deleteIfExists(Paths.get(filePath));
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
    
//     //method for renaming a file
//     @FXML
//     public void ReturnHome() throws IOException {
//         Stage secondaryStage = new Stage();
//         Stage primaryStage = (Stage) homeBtn.getScene().getWindow(); // Retrieve the primary stage from the home button's scene
//         try {
//             FXMLLoader loader = new FXMLLoader(); // Loads the FXML file for the home screen
//             loader.setLocation(getClass().getResource("secondary.fxml"));
//             Parent root = loader.load();
//             Scene scene = new Scene(root, 640, 480);
//             secondaryStage.setScene(scene);
//             secondaryStage.setTitle("Login");
//             secondaryStage.show();
//             primaryStage.close();
//         } catch (Exception e) {
//             warningField.setText(e.toString());
//             e.printStackTrace();
//         }
//     }

//     //method for renaming a file
//     public void renameFile(ActionEvent event) {
//         try {
//             String userInput = renameFileField.getText();
//             if (userFile != null) {
//                 File renamedFile = new File(userFile.getParent(), userInput); //Create a new File object with the specified name in the same directory
//                 if (userFile.renameTo(renamedFile)) {
//                     System.out.println("File renamed to: " + userInput);
//                     warningField.setText("File renamed to: " + userInput);
//                     userFile = renamedFile; // Update userFile reference to the renamed file
//                 } else {
//                     System.out.println("Failed to rename the file.");
//                     warningField.setText("Failed to rename the file.");
//                 }
//             } else {
//                 System.out.println("No file specified for renaming.");
//                 warningField.setText("No file specified for renaming.");
//             }
//         } catch (Exception e) {
//             System.out.println("An error occurred during file renaming.");
//             warningField.setText("An error occurred during file renaming.");
//             e.printStackTrace();
//         }
//     }
    
//     @FXML
//     public void updateFile(ActionEvent event) {
//         try {
//             // Check if a file has been specified
//             if (userFile != null) {
//                 String updatedContent = fileTextField.getText();
                
//                 // Append the updated content to the file
//                 try (FileWriter writer = new FileWriter(userFile, true)) {
                    
//                     writer.write(updatedContent + "\n"); // Append content and add a newline
                    
//                     System.out.println("File content updated.");
//                     warningField.setText("File content updated.\n");
                
//                 } catch (IOException e) {
//                     System.out.println("An error occurred while updating file content.");
                    
//                     warningField.setText("An error occurred while updating file content.\n");
//                     warningField.setText(e.toString());
//                 }
            
//             } else {
//                 System.out.println("No file specified for updating content.");
//             }
//             warningField.setText("No file specified for updating content.\n");
        
//         } catch (Exception e) {
//             System.out.println("An unexpected error occurred.");
//             warningField.setText("An unexpected error occurred.");
//             e.printStackTrace();
//         }
//     }
    
//      @FXML
//     public void downloadFile(ActionEvent event) {
//         // Check if a file has been specified
//         if (userFile != null) {
//             FileChooser fileChooser = new FileChooser();
//             fileChooser.setTitle("Save File As");

//             // Set the initial directory for the file chooser
//             fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

//             // Set the default file name
//             fileChooser.setInitialFileName(userFile.getName());

//             // Show the file save dialog
//             File selectedFile = fileChooser.showSaveDialog(null);

//             if (selectedFile != null) {
//                 try {
//                     // Copy the specified file to the chosen destination
//                     Files.copy(userFile.toPath(), selectedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

//                     warningField.setText("File downloaded to: " + selectedFile.getAbsolutePath());
//                 } catch (IOException e) {
//                     warningField.setText("Error downloading file: " + e.getMessage());
//                 }
//             } else {
//                 warningField.setText("File download canceled.");
//             }
//         } else {
//             System.out.println("No file specified for download.");
//             warningField.setText("No file specified for download.");
//         }
//     }
// }