package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ClearDataServiceTests {
    private final UserDAO testUserDAO = new UserDAO();
    private final AuthDAO testAuthDAO = new AuthDAO();
    private final GameDAO testGameDAO = new GameDAO();
    private final UserService userService = new UserService(testUserDAO, testAuthDAO);
    private final GameService gameService = new GameService(testAuthDAO, testGameDAO);
    private final ClearDataService clearDataService = new ClearDataService(testUserDAO, testAuthDAO, testGameDAO);

    //Fill up memory with some mock data
    public void setUp() throws Exception {
        UserData testData = new UserData("testLogin", "testPassword", "testEmail");
        UserData testData2 = new UserData("testLogin2", "testPassword2", "testEmail2");
        userService.registerNewUser(testData);
        AuthData authData = userService.registerNewUser(testData2);

        GameData newGame = new GameData(0, null, null, "gameName", null);
        GameData newGame2 = new GameData(0, null, null, "gameName", null);

        gameService.createGame(newGame, authData.authToken());
        gameService.createGame(newGame2, authData.authToken());
    }

    @Test
    @DisplayName("Delete Data Test")
    public void testDeleteData() throws Exception {
        setUp();

        assertTrue(clearDataService.clearAllData());


    }
}
