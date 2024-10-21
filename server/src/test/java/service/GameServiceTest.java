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
        AuthData authData = setUp();
        GameData newGame = new GameData(0, null, null, "gameName", null);

        assertThrows(DataAccessException.class, () -> gameService.createGame(newGame, null));

    }

    @Test
    @DisplayName("List Games Success")
    //Adds two games, then calls the getAllGames to ensure the data structure recieved is correct length
    public void listGamesSuccess() throws DataAccessException {
        AuthData authData = setUp();
        GameData newGame = new GameData(0, null, null, "gameName", null);
        GameData newGame2 = new GameData(0, null, null, "gameName2", null);


        int gameId = gameService.createGame(newGame, authData.authToken());
        int gameId2 = gameService.createGame(newGame2, authData.authToken());
        ArrayList<GameData> games = gameService.getAllGames(authData.authToken());

        assertFalse(games.isEmpty());
        assertEquals(2, games.size());
    }

    @Test
    @DisplayName("List Games fail")
    //Adds two games, then tries to retrieve them without a valid auth token twice
    public void listGameFail() throws DataAccessException {
        AuthData authData = setUp();
        GameData newGame = new GameData(0, null, null, "gameName", null);
        GameData newGame2 = new GameData(0, null, null, "gameName2", null);

        int gameId = gameService.createGame(newGame, authData.authToken());
        int gameId2 = gameService.createGame(newGame2, authData.authToken());
        assertThrows(DataAccessException.class, () -> gameService.getAllGames(null));
        assertThrows(DataAccessException.class, () -> gameService.getAllGames("abcsdjhasidh"));
    }

    @Test
    @DisplayName("Join Games Success")
    //Creates new game, then requests to join that game based on the provided gameID
    public void joinGameSuccess() throws DataAccessException {
        AuthData authData = setUp();
        GameData newGame = new GameData(0, null, null, "gameName", null);

        int gameId = gameService.createGame(newGame, authData.authToken());
        JoinGameData joinGameRequest = new JoinGameData("BLACK", gameId);
        boolean status = gameService.joinGame(joinGameRequest, authData.authToken());

        assertTrue(status);
    }

    @Test
    @DisplayName("Join Games fail")
    //Creates game and joins. Assert an exception is thrown when joining with no authToken, assert false
    //When game is tried to join when team is already occupied
    public void joinGameFail() throws DataAccessException {
        AuthData authData = setUp();
        GameData newGame = new GameData(0, null, null, "gameName", null);

        int gameId = gameService.createGame(newGame, authData.authToken());
        JoinGameData joinGameRequest = new JoinGameData("BLACK", gameId);
        boolean status = gameService.joinGame(joinGameRequest, authData.authToken());

        assertThrows(DataAccessException.class, () -> gameService.joinGame(joinGameRequest, null));

        boolean statusRejoin = gameService.joinGame(joinGameRequest, authData.authToken());

        assertFalse(statusRejoin);
    }


}
