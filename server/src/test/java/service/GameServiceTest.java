package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.*;
import org.junit.jupiter.api.*;
import model.*;


public class GameServiceTest {

    private final UserDAO testUserDAO = new UserDAO();
    private final AuthDAO testAuthDAO = new AuthDAO();
    private final GameDAO testGameDAO = new GameDAO();
    private final UserService userService = new UserService(testUserDAO, testAuthDAO);
    private final GameService gameService = new GameService(testUserDAO, testAuthDAO, testGameDAO);

    public AuthData setUp() throws DataAccessException {
        UserData testData = new UserData("testLogin", "testPassword", "testEmail");
        userService.registerNewUser(testData);
        UserData loginTest = new UserData("testLogin", "testPassword", null);
        return userService.loginUser(loginTest);
    }

    @Test
    @DisplayName("Create Game Success")

    public void createGameSuccess() throws DataAccessException {
        AuthData authData = setUp();
        GameData newGame = new GameData(0, null, null, "gameName", null);

        int gameId = gameService.createGame(newGame, authData.authToken());
        System.out.println(gameId);
    }
}
