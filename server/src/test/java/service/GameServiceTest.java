package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.*;
import org.junit.jupiter.api.*;
import model.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


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
    //Asserts that an Int is returned as the gameID and that the games
    //Array list in the database is not empty
    public void createGameSuccess() throws DataAccessException {
        AuthData authData = setUp();
        GameData newGame = new GameData(0, null, null, "gameName", null);

        int gameId = gameService.createGame(newGame, authData.authToken());
        ArrayList<GameData> games = testGameDAO.getGames();

        assertInstanceOf(Integer.class, gameId, "value not an int");
        assertFalse(games.isEmpty());
    }

    @Test
    @DisplayName("Create Game fail")

    public void createGameFail() throws DataAccessException {

    }
}
