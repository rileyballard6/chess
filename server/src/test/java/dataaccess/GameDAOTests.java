package dataaccess;

import model.AuthData;
import model.GameData;
import model.JoinGameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameDAOTests {

    GameDAO testGame = new GameDAO();
    AuthDAO testAuth = new AuthDAO();
    UserDAO testUser = new UserDAO();

    @BeforeEach
    public void setUp() throws DataAccessException {
        testGame.clearGames();
        testAuth.clearAuth();
        testUser.clearUsers();

    }

    @Test
    public void gameDAOCreatePositive() throws DataAccessException {
        GameData newGame = new GameData(123, null, null, "name", null);
        int gameId = testGame.createGameSQL(newGame);

        assertInstanceOf(Integer.class, gameId);
    }

    @Test
    public void gameDAOCreateNegative() throws DataAccessException {

    }

    @Test
    public void gameDAOGetPositive() throws DataAccessException {
        GameData newGame = new GameData(123, null, null, "name", null);
        GameData newGame2 = new GameData(123, null, null, "name2", null);

        testGame.createGameSQL(newGame);
        testGame.createGameSQL(newGame2);

        ArrayList<GameData> games = testGame.getGamesSQL();

        assertInstanceOf(ArrayList.class, games);
        assertTrue(games.size() > 1);
    }

    @Test
    public void gameDAOGetNegative() throws DataAccessException {
        GameData newGame = new GameData(123, null, null, "name", null);
        GameData newGame2 = new GameData(123, null, null, "name2", null);

        testGame.createGameSQL(newGame);
        testGame.createGameSQL(newGame2);

        ArrayList<GameData> games = testGame.getGamesSQL();

        assertFalse(games.size() > 3);

    }

    @Test
    public void gameDAOExistsPositive() throws DataAccessException {
        GameData newGame = new GameData(123, null, null, "name", null);
        GameData newGame2 = new GameData(124, null, null, "name2", null);

        int gameId = testGame.createGameSQL(newGame);
        int gameId2 = testGame.createGameSQL(newGame2);

        assertTrue(testGame.gameExistsSQL(gameId));
        assertTrue(testGame.gameExistsSQL(gameId2));
    }

    @Test
    public void gameDAOExistsNegative() throws DataAccessException {
        GameData newGame = new GameData(123, null, null, "name", null);
        GameData newGame2 = new GameData(124, null, null, "name2", null);

        testGame.createGameSQL(newGame);
        testGame.createGameSQL(newGame2);

        assertFalse(testGame.gameExistsSQL(232342334));
        assertFalse(testGame.gameExistsSQL(34234234));

    }

    @Test
    public void gameDAOUpdatePositive() throws DataAccessException {

    }

    @Test
    public void gameDAOUpdateNegative() throws DataAccessException {

    }

    @Test
    public void gameDAOAddPlayerPositive() throws DataAccessException {
        GameData newGame = new GameData(123, null, null, "name", null);
        AuthData testAuth = new AuthData("abc", "testUser");

        int gameId = testGame.createGameSQL(newGame);

        JoinGameData testJoin = new JoinGameData("WHITE", gameId);

        assertTrue(testGame.updateGameSQL(testJoin, testAuth));

    }

    @Test
    public void gameDAOAddPlayerNegative() throws DataAccessException {
        GameData newGame = new GameData(123, null, null, "name", null);
        AuthData testAuth = new AuthData("abc", "testUser");

        int gameId = testGame.createGameSQL(newGame);

        JoinGameData testJoin = new JoinGameData("BLACK", gameId);

        testGame.updateGameSQL(testJoin, testAuth);

        assertFalse(testGame.updateGameSQL(testJoin, testAuth));
    }

    @Test
    public void gameDAOClearPositive() throws DataAccessException {
        GameData newGame = new GameData(123, null, null, "name", null);
        GameData newGame2 = new GameData(1234, null, null, "name2", null);
        GameData newGame3 = new GameData(1245, null, null, "name3", null);

        testGame.createGameSQL(newGame);
        testGame.createGameSQL(newGame2);
        testGame.createGameSQL(newGame3);

        assertTrue(testGame.clearGames());

    }
}
