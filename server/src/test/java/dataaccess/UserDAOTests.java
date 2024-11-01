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
    public void userDAOGetUserPositive() {

    }

    @Test
    public void userDAOGetUserNegative() {

    }

    @Test
    public void userDAOExistsPositive() {

    }

    @Test
    public void userDAOExistsNegative() {

    }

    @Test
    public void userDAOClearPositive() {

    }
}
