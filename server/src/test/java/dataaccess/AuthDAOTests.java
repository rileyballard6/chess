package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLIntegrityConstraintViolationException;

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
    public void authDAOFindPositive() {

    }

    @Test
    public void authDAOFindNegative() {

    }

    @Test
    public void authDAODeletePositive() {

    }

    @Test
    public void authDAODeleteNegative() {

    }

    @Test
    public void authDAOGetPositive() {

    }

    @Test
    public void authDAOGetNegative() {

    }

    @Test
    public void authDAOClearPositive() {

    }


}
