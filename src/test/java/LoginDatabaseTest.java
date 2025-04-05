import static org.junit.jupiter.api.Assertions.*;

import com.example.LoginDatabase;
import org.junit.jupiter.api.*;
import java.util.ArrayList;

public class LoginDatabaseTest {
    private LoginDatabase db;
    private final String testUser = "unitTestUser";
    private final String testPassword = "unitTestPassword";

    @BeforeEach
    public void setUp(){
        db = new LoginDatabase();
        db.deleteUser(testUser);
    }

    @Test
    public void testRegisterUser() {
        //Should register user successfully
        boolean isRegistered = db.registerUser(testUser, testPassword);
        assertTrue(isRegistered, "User should register successfully");

        //Check duplicated user
        boolean IsDuplicate = db.registerUser(testUser, testPassword);
        assertFalse(IsDuplicate, "Duplicated registration should fail.");

    }

    @Test
    public void testAuthenticateUser() {
        // Register user and verify authentication (password)
        db.registerUser(testUser, testPassword);
        assertTrue(db.authenticate(testUser, testPassword), "User should authenticate successfully if credentials are valid");

        //Wrong password
        assertFalse(db.authenticate("wrongPass", testPassword), "User should not authenticate successfully if credentials are invalid");

        //Empty credentials
        assertFalse(db.authenticate("", testPassword), "Empty username should fail.");
        assertFalse(db.authenticate(testUser, ""), "Empty password should fail.");

    }

    @Test
    public void testUpdateUserPassword() {
        //Updating Password
        db.registerUser(testUser, testPassword);
        boolean isUpdated = db.updateUserPassword(testUser, "newPass");
        assertTrue(isUpdated, "User should update successfully");


        // Old pass shouldn't work
        assertFalse(db.authenticate(testUser, testPassword), "Old password should fail.");

        // New password should work
        assertTrue(db.authenticate(testUser, "newPass"), "New password should work.");
    }

    @Test
    public void testDeleteUserAndListUsers(){
        db.registerUser(testUser, testPassword);
        ArrayList<String[]> usersBefore = db.listUsers();
        boolean found = usersBefore.stream().anyMatch(u -> u[1].equals(testUser));
        assertTrue(found, "User should exist");

        boolean isDeleted = db.deleteUser(testUser);
        assertTrue(isDeleted, "User should delete successfully");

        ArrayList<String[]> usersAfter = db.listUsers();
        boolean stillFound = usersAfter.stream().anyMatch(u -> u[1].equals(testUser));
        assertFalse(stillFound, "User should not exist");
    }
}
