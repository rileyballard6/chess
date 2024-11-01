package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class AuthDAOTests {

    AuthDAO testAuth = new AuthDAO();
    UserDAO testUser = new UserDAO();

    @BeforeEach
    public void setUp() throws DataAccessException {
        testAuth.clearAuth();
        testUser.clearUsers();

        UserData newUser = new UserData("test", "test", "test");
        testUser.createUserSQL(newUser);
    }

    @Test
    public void authDAOCreatePositive() throws DataAccessException {
        UserData user = testUser.getUserSQL("test");

        AuthData newAuth = testAuth.createAuthSQL(user.username());

        assertInstanceOf(String.class, newAuth.authToken());
        assertInstanceOf(AuthData.class, newAuth);
    }


    @Test
    public void authDAOCreateNegative() throws RuntimeException, DataAccessException {
        UserData user = testUser.getUserSQL("test");

        AuthData data = testAuth.createAuthSQL(user.username());

        assertNotNull(data.authToken());
    }


    @Test
    public void authDAOFindPositive() throws DataAccessException {
        UserData user = testUser.getUserSQL("test");

        AuthData data = testAuth.createAuthSQL(user.username());

        assertTrue(testAuth.findAuthSQL(data.authToken()));

    }

    @Test
    public void authDAOFindNegative() throws DataAccessException {
        UserData user = testUser.getUserSQL("test");

        AuthData data = testAuth.createAuthSQL(user.username());

        assertFalse(testAuth.findAuthSQL(data.username()));

    }

    @Test
    public void authDAODeletePositive() throws DataAccessException {
        UserData user = testUser.getUserSQL("test");

        AuthData data = testAuth.createAuthSQL(user.username());

        assertTrue(testAuth.deleteAuthDataSQL(data.authToken()));
        assertFalse(testAuth.findAuthSQL(data.username()));
    }

    @Test
    public void authDAODeleteNegative() throws DataAccessException {
        UserData user = testUser.getUserSQL("test");

        AuthData data = testAuth.createAuthSQL(user.username());

        testAuth.deleteAuthDataSQL(data.authToken());

        assertFalse(testAuth.deleteAuthDataSQL(data.authToken()));
    }

    @Test
    public void authDAOGetPositive() throws DataAccessException {
        UserData user = testUser.getUserSQL("test");

        AuthData test = testAuth.createAuthSQL(user.username());
        AuthData data = testAuth.getAuthDataSQL(test.authToken());

        assertInstanceOf(AuthData.class, data);
        assertInstanceOf(String.class, data.authToken());

    }

    @Test
    public void authDAOGetNegative() throws DataAccessException {
        UserData user = testUser.getUserSQL("test");

        AuthData test = testAuth.createAuthSQL(user.username());
        testAuth.deleteAuthDataSQL(test.authToken());
        AuthData data = testAuth.getAuthDataSQL(test.authToken());

        assertNull(data);
    }

    @Test
    public void authDAOClearPositive() throws DataAccessException {
        testAuth.createAuthSQL("test1");
        testAuth.createAuthSQL("test2");
        testAuth.createAuthSQL("test3");

        assertTrue(testAuth.clearAuth());
    }


}
