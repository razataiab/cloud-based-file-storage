package com.mycompany.javafxapplication1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DB {
    private static final String DATABASE_URL = "jdbc:sqlite:comp20081.db";
    private static final int TIMEOUT = 30;
    private static final String TABLE_NAME = "Users";
    private static final int SALT_LENGTH = 30;
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private Connection connection;
    private String saltValue;
    private final Random random = new SecureRandom();

    public DB() {
        try {
            File saltFile = new File(".salt");
            if (!saltFile.exists()) {
                saltValue = getSaltValue(SALT_LENGTH);
                try (FileWriter writer = new FileWriter(saltFile)) {
                    writer.write(saltValue);
                }
            } else {
                try (Scanner reader = new Scanner(saltFile)) {
                    if (reader.hasNextLine()) {
                        saltValue = reader.nextLine();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTable(String tableName) {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE, password TEXT)";
        executeUpdate(sql);
    }

    public void deleteTable(String tableName) {
        String sql = "DROP TABLE IF EXISTS " + tableName;
        executeUpdate(sql);
    }

    private void executeUpdate(String sql) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DATABASE_URL);
            try (var statement = connection.createStatement()) {
                statement.setQueryTimeout(TIMEOUT);
                statement.executeUpdate(sql);
            }
        } catch (SQLException | ClassNotFoundException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, "Error executing update", e);
        } finally {
            closeConnection();
        }
    }

    public void addDataToDB(String user, String password) {
        if (nameExists(user)) {
            Logger.getLogger(DB.class.getName()).log(Level.INFO, "User with name {0} already exists", user);
            return;
        }

        String sql = "INSERT INTO " + TABLE_NAME + " (name, password) VALUES (?, ?)";
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DATABASE_URL);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, user);
                statement.setString(2, generateSecurePassword(password));
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, "Error adding data to DB", e);
        } catch (ClassNotFoundException | InvalidKeySpecException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, "Error adding data to DB", e);
        } finally {
            closeConnection();
        }
    }

    public ObservableList<User> getDataFromTable() {
        ObservableList<User> result = FXCollections.observableArrayList();
        String sql = "SELECT * FROM " + TABLE_NAME;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DATABASE_URL);
            try (var statement = connection.createStatement()) {
                statement.setQueryTimeout(TIMEOUT);
                try (ResultSet rs = statement.executeQuery(sql)) {
                    while (rs.next()) {
                        result.add(new User(rs.getString("name"), rs.getString("password")));
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, "Error retrieving data from table", e);
        } finally {
            closeConnection();
        }
        return result;
    }

    public boolean validateUser(String user, String pass) {
        boolean isValid = false;
        String sql = "SELECT name, password FROM " + TABLE_NAME + " WHERE name = ?";
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DATABASE_URL);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, user);
                try (ResultSet rs = statement.executeQuery()) {
                    String hashedPassword = generateSecurePassword(pass);
                    while (rs.next()) {
                        if (user.equals(rs.getString("name")) && hashedPassword.equals(rs.getString("password"))) {
                            isValid = true;
                            break;
                        }
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException | InvalidKeySpecException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, "Error validating user", e);
        } finally {
            closeConnection();
        }
        return isValid;
    }

    public void deleteUser(String username) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE name = ?";
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DATABASE_URL);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.executeUpdate();
            }
        } catch (SQLException | ClassNotFoundException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, "Error deleting user", e);
        } finally {
            closeConnection();
        }
    }

    public void updatePassword(String existingPassword, String newPassword) {
        String sql = "UPDATE " + TABLE_NAME + " SET password = ? WHERE password = ?";
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DATABASE_URL);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, generateSecurePassword(newPassword));
                statement.setString(2, generateSecurePassword(existingPassword));
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Password updated successfully.");
                } else {
                    System.out.println("No matching records found to update.");
                }
            }
        } catch (SQLException | ClassNotFoundException | InvalidKeySpecException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, "Error updating password", e);
        } finally {
            closeConnection();
        }
    }

    public void updateField(String fieldName, String newValue, String whereValue, String whereField) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + fieldName + " = ? WHERE " + whereField + " = ?";
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DATABASE_URL);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, newValue);
                statement.setString(2, whereValue);
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Field updated successfully.");
                } else {
                    System.out.println("No matching records found to update.");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, "Error updating field", e);
        } finally {
            closeConnection();
        }
    }

    public void addLog(String message, String tableName) {
        String sql = "INSERT INTO " + tableName + " (log) VALUES (?)";
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DATABASE_URL);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, message);
                statement.executeUpdate();
            }
        } catch (SQLException | ClassNotFoundException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, "Error adding log", e);
        } finally {
            closeConnection();
        }
    }

    public boolean nameExists(String name) {
        boolean exists = false;
        String sql = "SELECT COUNT(*) AS count FROM " + TABLE_NAME + " WHERE name = ?";
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DATABASE_URL);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        exists = rs.getInt("count") > 0;
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, "Error checking name existence", e);
        } finally {
            closeConnection();
        }
        return exists;
    }

    private String getSaltValue(int length) {
        StringBuilder salt = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            salt.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return salt.toString();
    }

    private byte[] hash(char[] password, byte[] salt) throws InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError("Error while hashing password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    public String generateSecurePassword(String password) throws InvalidKeySpecException {
        byte[] hashedPassword = hash(password.toCharArray(), saltValue.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    private void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, "Error closing connection", e);
            }
        }
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public void log(String message) {
        System.out.println(message);
    }

    public String getUserDirectory(String username) {
        File userDir = new File(System.getProperty("user.dir"), "UploadedFiles/" + username);
        if (!userDir.exists()) {
            userDir.mkdirs();
        }
        return userDir.getAbsolutePath();
    }
}
