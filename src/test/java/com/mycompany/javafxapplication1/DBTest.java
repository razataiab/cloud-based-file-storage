package com.mycompany.javafxapplication1;

import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class DBTest {
    private DB db;

    @Before
    public void setUp() {
        db = new DB();
        // Ensure the table is clean before each test
        db.deleteTable(db.getTableName());
        db.createTable(db.getTableName());
    }

    @After
    public void tearDown() {
        db.deleteTable(db.getTableName());
    }

    @Test
    public void testAddDataToDB() {
        String testUser = "testuser";
        String testPassword = "testpassword";

        // Add data to the database
        db.addDataToDB(testUser, testPassword);

        // Verify the user was added
        boolean userExists = db.nameExists(testUser);
        assertTrue("User should exist in the database", userExists);

        // Verify the password is hashed and stored correctly
        boolean isValid = db.validateUser(testUser, testPassword);
        assertTrue("Password should be valid for the user", isValid);
    }
}
