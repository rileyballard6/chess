package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import org.junit.jupiter.api.*;
import model.*;


import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private final UserDAO testUserDAO = new UserDAO();
    private final AuthDAO testAuthDAO = new AuthDAO();
    private final UserService userService = new UserService(testUserDAO, testAuthDAO);


    @Test
    @DisplayName("Register User test")
    //Checks if username is correct for returned auth data, and that the auth token exists and is not null
    public void registerTestSuccess() throws DataAccessException {
        UserData testData = new UserData("testUsername", "testPassword", "testEmail");

        AuthData registerAnswer = userService.registerNewUser(testData);
        assertEquals("testUsername", registerAnswer.username());
        assertNotNull(registerAnswer.authToken());
        assertFalse(registerAnswer.authToken().isEmpty());
    }

    @Test
    @DisplayName("Register User fail")
    //Checks that the Service is ensuring all fields are there
    public void registerTestFail() throws DataAccessException {
        UserData testData = new UserData("testUsername", null, "testEmail");

        assertThrows(DataAccessException.class, () -> userService.registerNewUser(testData));

    }

    @Test
    @DisplayName("Login User success")
    //Registers a new user, then ensures that logging in will return correct authData
    public void loginTestSuccess() throws DataAccessException {
        UserData testData = new UserData("testLogin", "testPassword", "testEmail");
        userService.registerNewUser(testData);
        UserData loginTest = new UserData("testLogin", "testPassword", null);
        AuthData loginSuccess = userService.loginUser(loginTest);

        assertEquals("testLogin", loginSuccess.username());

        assertNotNull(loginSuccess.authToken());
        assertFalse(loginSuccess.authToken().isEmpty());

    }

    @Test
    @DisplayName("Login User Fail")
    //Ensure error is thrown if user doesn't exist, returns null if the wrong password is entered
    public void loginTestFail() throws DataAccessException {
        UserData testData = new UserData("testLogin", "testPassword", "testEmail");
        userService.registerNewUser(testData);
        UserData loginTest = new UserData("testUser", "testPassword", null);
        UserData loginTestWrongPassword = new UserData("testLogin", "asdjoijw", null);

        assertThrows(DataAccessException.class, () -> userService.loginUser(loginTest));
        assertNull(userService.loginUser(loginTestWrongPassword));
    }

    @Test
    @DisplayName("Logout Test Success")
    // Checks that logging out is returning true if successful logout, and false if auth token not found
    public void logoutTestSuccess() throws DataAccessException {
        UserData testData = new UserData("testLogin", "testPassword", "testEmail");
        userService.registerNewUser(testData);
        UserData loginTest = new UserData("testLogin", "testPassword", null);
        AuthData loginSuccess = userService.loginUser(loginTest);

        assertTrue(userService.logoutUser(loginSuccess.authToken()));
        assertFalse(userService.logoutUser("asoidjaoijdw"));
    }

    @Test
    @DisplayName("Logout Test Fail")

    public void logoutTestFail() throws DataAccessException {
        UserData testData = new UserData("testLogin", "testPassword", "testEmail");
        userService.registerNewUser(testData);
        UserData loginTest = new UserData("testLogin", "testPassword", null);
        AuthData loginSuccess = userService.loginUser(loginTest);

        assertThrows(DataAccessException.class, () -> userService.logoutUser(null));
        assertFalse(userService.logoutUser("asidjow"));
    }
}
