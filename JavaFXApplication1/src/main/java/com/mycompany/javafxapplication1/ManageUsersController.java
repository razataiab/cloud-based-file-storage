// package com.mycompany.javafxapplication1;

// import static com.mycompany.javafxapplication1.PrimaryController.username_;

// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.fxml.FXMLLoader;
// import javafx.scene.Parent;
// import javafx.scene.Scene;
// import javafx.scene.control.Button;
// import javafx.scene.control.TextField;
// import javafx.scene.text.Text;
// import javafx.stage.Stage;

// public class ManageUsersController {
//     @FXML
//     private Text fileText;

//     @FXML
//     private TextField namechangefield;

//     @FXML
//     private TextField namechangefield2;

//     @FXML
//     private TextField changepass1;

//     @FXML
//     private TextField repass;

//     @FXML
//     private TextField oldpass;

//     @FXML
//     private TextField delacctname;

//     @FXML
//     private TextField delacctpass;

//     @FXML
//     private Button confirm;

//     @FXML
//     private Button confirm2;

//     @FXML
//     private Button confirm3;

//     @FXML
//     private Button backButton;

//     @FXML
//     public void initialise() {
//         String getuser_ = username_.getUser();
//         fileText.setText(getuser_);
//     }

//     @FXML
//     private void Backbuttonahndler(ActionEvent event) {
//         String getuser_ = username_.getUser();
//         String getpass = username_.getPass();
//         Stage secondaryStage = new Stage();
//         Stage primaryStage = (Stage) backButton.getScene().getWindow();
//         try {
//             String[] credentials = new String[2];
//             credentials[0] = getuser_;
//             credentials[1] = getpass;
//             FXMLLoader loader = new FXMLLoader();
//             loader.setLocation(getClass().getResource("secondary.fxml"));
//             Parent root = loader.load();
//             Scene scene = new Scene(root, 1250, 900);
//             secondaryStage.setScene(scene);
//             SecondaryController controller = loader.getController();
//             controller.initialise(credentials);
//             controller.initialise2();
//             secondaryStage.setTitle("Home");
//             secondaryStage.show();
//             primaryStage.close();
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     @FXML
//     private void chnageUserNameHandler(ActionEvent event) {
//         try {
//             String getuser_ = username_.getUser();
//             String getpass = username_.getPass();
//             String newName = namechangefield.getText();
//             String enteredPassword = namechangefield2.getText();
//             DB myobj = new DB();

//             if (myobj.nameExists(newName)) {
//                 edialogue("NAME ERROR", "Username Taken");
//                 return;
//             }

//             if (getpass.equals(enteredPassword)) {
//                 if (!newName.equals(getuser_)) {
//                     myobj.updateField("name", newName, getuser_, "name");
//                     // Commented out the lines that reference `File` and `Log`
//                     // File.updateField("userName", newName, getuser_, "userName");
//                     // Log.addLog("User " + getuser_ + " Changed Username to " + newName, "log");
//                     namechangefield.setText("");
//                     namechangefield2.setText("");
//                     changepass1.setText("");
//                     repass.setText("");
//                     oldpass.setText("");
//                     delacctname.setText("");
//                     delacctpass.setText("");
//                     fileText.setText(newName);
//                     username_.setUser(newName);
//                     dialogue("Username Update", "Successful");
//                 } else {
//                     edialogue("Invalid Name", "New name cannot be the same as the current name");
//                 }
//             } else {
//                 edialogue("Invalid Password", "Please try again with the correct password");
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     private void dialogue(String title, String message) {
//         // Implement your dialogue logic here
//         System.out.println("Dialogue: " + title + " - " + message);
//     }

//     private void edialogue(String title, String message) {
//         // Implement your error dialogue logic here
//         System.err.println("Error Dialogue: " + title + " - " + message);
//     }

//     @FXML
//     private void switchToPrimary() {
//         Stage secondaryStage = new Stage();
//         Stage primaryStage = (Stage) confirm3.getScene().getWindow();
//         try {
//             FXMLLoader loader = new FXMLLoader();
//             loader.setLocation(getClass().getResource("primary.fxml"));
//             Parent root = loader.load();
//             Scene scene = new Scene(root, 1250, 900);
//             secondaryStage.setScene(scene);
//             secondaryStage.setTitle("Login");
//             secondaryStage.show();
//             primaryStage.close();
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }
