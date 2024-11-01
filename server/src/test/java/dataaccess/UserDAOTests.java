package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTests {

    UserDAO testDAO = new UserDAO();

    @BeforeEach
    public void setUp() throws DataAccessException {
        testDAO.clearUsers();
    }

    @Test
    public void userDAOCreatePositive() throws DataAccessException {
        UserData newUser = new UserData("test", "testpassword", "testemail");

        UserData registeredUser = testDAO.createUserSQL(newUser);
        assertEquals(registeredUser, newUser);
    }

    @Test
    public void userDAOCreateNegative() throws DataAccessException {
        UserData newUser = new UserData("test", "testpassword", "testemail");

        testDAO.createUserSQL(newUser);

        assertThrows(RuntimeException.class, () -> testDAO.createUserSQL(newUser));
    }


    @Test
    public void userDAOGetUserPositive() throws DataAccessException {
        UserData newUser = new UserData("test", "testpassword", "testemail");

        testDAO.createUserSQL(newUser);

        UserData foundUser = testDAO.getUserSQL(newUser.username());

        assertEquals(foundUser.username(), newUser.username());
        assertEquals(foundUser.email(), newUser.email());

    }

    @Test
    public void userDAOGetUserNegative() throws DataAccessException {
        UserData newUser = new UserData("test", "testpassword", "testemail");

        testDAO.createUserSQL(newUser);

        UserData foundUser = testDAO.getUserSQL("wrongUser");

        assertNull(foundUser);

    }

    @Test
    public void userDAOExistsPositive() throws DataAccessException {
        UserData newUser = new UserData("test", "testpassword", "testemail");

        testDAO.createUserSQL(newUser);
        Boolean existsSQL = testDAO.userExistsSQL(newUser.username());

        assertTrue(existsSQL);
    }

    @Test
    public void userDAOExistsNegative() throws DataAccessException {
        UserData newUser = new UserData("test", "testpassword", "testemail");

        testDAO.createUserSQL(newUser);
        Boolean existsSQL = testDAO.userExistsSQL("wrongUser");

        assertFalse(existsSQL);
    }

    @Test
    public void userDAOClearPositive() {

    }
}
