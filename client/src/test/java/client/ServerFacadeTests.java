package client;

import com.google.gson.Gson;
import facade.ServerFacade;
import model.AuthData;
import model.GameData;
import model.JoinGameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    static {
        try {
            serverFacade = new ServerFacade();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    public static void initAll() throws IOException {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    //Set up should clear the database each time.
    @BeforeEach
    public void init() throws IOException {

        URL url = new URL("http://localhost:8080/db");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("DELETE");
        http.setRequestProperty("Content-Type", "application/json; utf-8");

        int responseCode = http.getResponseCode();
        System.out.println("Response Code: " + responseCode);

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void registerTestTrue() throws Exception {
        UserData registerTest = new UserData("testuser", "passwordtest", "email@gmail.com");

        String answer = serverFacade.registerCall(registerTest);
        assertTrue(answer.length() > 10);
    }

    @Test
    public void registerTestFalse() throws Exception {
        UserData registerTest = new UserData("testuser", "passwordtest", "email@gmail.com");

        String answer = serverFacade.registerCall(registerTest);
        assertFalse(answer.isEmpty());
    }

    @Test
    public void loginTestTrue() throws Exception {
        UserData registerTest = new UserData("testuser", "passwordtest", "email@gmail.com");

        String answer = serverFacade.registerCall(registerTest);

        String auth = serverFacade.loginCall(new UserData("testuser", "passwordtest", null));
        assertTrue(auth.length() > 10);

    }

    @Test
    public void loginTestFalse() throws Exception {
        UserData registerTest = new UserData("testuser", "passwordtest", "email@gmail.com");

        String answer = serverFacade.registerCall(registerTest);

        assertThrows(IOException.class, () -> serverFacade.loginCall(new UserData("testuser", "password", null)));
    }


    @Test
    public void logoutTestTrue() throws Exception {
        UserData registerTest = new UserData("testuser", "passwordtest", "email@gmail.com");
        String answer = serverFacade.registerCall(registerTest);

        Gson gson = new Gson();
        AuthData authToken = gson.fromJson(answer, AuthData.class);

        String logout = serverFacade.logoutCall(authToken.authToken());
        assertEquals("{}", logout);

    }

    @Test
    public void logoutTestFalse() throws Exception {
        UserData registerTest = new UserData("testuser", "passwordtest", "email@gmail.com");
        serverFacade.registerCall(registerTest);

        assertThrows(IOException.class, () -> serverFacade.logoutCall("123"));

    }

    @Test
    public void createGameTestTrue() throws Exception {
        UserData registerTest = new UserData("testuser", "passwordtest", "email@gmail.com");
        String answer = serverFacade.registerCall(registerTest);

        Gson gson = new Gson();
        AuthData authToken = gson.fromJson(answer, AuthData.class);
        GameData newGame = new GameData(0, null,null, "gameTest", null);

        String gameCreated = serverFacade.createGameCall(newGame, authToken.authToken());

        assertTrue(gameCreated.length() > 0);
    }

    @Test
    public void createGameTestFalse() throws Exception {
        UserData registerTest = new UserData("testuser", "passwordtest", "email@gmail.com");
        String answer = serverFacade.registerCall(registerTest);

        Gson gson = new Gson();
        AuthData authToken = gson.fromJson(answer, AuthData.class);
        GameData newGame = new GameData(0, null,null, null, null);

        assertThrows(IOException.class, () ->serverFacade.createGameCall(newGame,"123"));

    }

    @Test
    public void listGameTestTrue() throws Exception {
        UserData registerTest = new UserData("testuser", "passwordtest", "email@gmail.com");
        String answer = serverFacade.registerCall(registerTest);

        Gson gson = new Gson();
        AuthData authToken = gson.fromJson(answer, AuthData.class);
        GameData newGame = new GameData(0, null,null, "gameTest", null);

        serverFacade.createGameCall(newGame, authToken.authToken());

        String gameList = serverFacade.listGamesCall(authToken.authToken());

        assertTrue(gameList.length() > 10);

    }

    @Test
    public void listGameTestFalse() throws Exception {
        UserData registerTest = new UserData("testuser", "passwordtest", "email@gmail.com");
        String answer = serverFacade.registerCall(registerTest);

        Gson gson = new Gson();
        AuthData authToken = gson.fromJson(answer, AuthData.class);
        GameData newGame = new GameData(0, null,null, "gameTest", null);

        serverFacade.createGameCall(newGame, authToken.authToken());

        assertThrows(IOException.class, () -> serverFacade.listGamesCall("ASDASD"));

    }

    @Test
    public void joinGameTestTrue() throws Exception {
        UserData registerTest = new UserData("testuser", "passwordtest", "email@gmail.com");
        String answer = serverFacade.registerCall(registerTest);

        Gson gson = new Gson();
        AuthData authToken = gson.fromJson(answer, AuthData.class);
        GameData newGame = new GameData(0, null,null, "gameTest", null);

        String gameID = serverFacade.createGameCall(newGame, authToken.authToken());
        GameData gameData = gson.fromJson(gameID, GameData.class);

        JoinGameData joinGame = new JoinGameData("BLACK", gameData.gameID());

        String gameJoined = serverFacade.joinGameCall(joinGame, authToken.authToken());

        assertEquals("{}", gameJoined);

    }

    @Test
    public void joinGameTestFalse() throws Exception {
        UserData registerTest = new UserData("testuser", "passwordtest", "email@gmail.com");
        String answer = serverFacade.registerCall(registerTest);

        Gson gson = new Gson();
        AuthData authToken = gson.fromJson(answer, AuthData.class);
        GameData newGame = new GameData(0, null,null, "gameTest", null);

        String gameID = serverFacade.createGameCall(newGame, authToken.authToken());
        GameData gameData = gson.fromJson(gameID, GameData.class);

        JoinGameData joinGame = new JoinGameData("test", gameData.gameID());

        assertThrows(IOException.class, () -> serverFacade.joinGameCall(joinGame, authToken.authToken()));

    }


    @Test
    public void makeRequestTestTrue() throws Exception {
        UserData registerTest = new UserData("testuser", "passwordtest", "email@gmail.com");
        String answer = serverFacade.registerCall(registerTest);

        Gson gson = new Gson();
        AuthData authToken = gson.fromJson(answer, AuthData.class);
        GameData newGame = new GameData(0, null,null, "gameTest", null);

        serverFacade.createGameCall(newGame, authToken.authToken());
        serverFacade.listGamesCall(authToken.authToken());

        URL url = new URL("http://localhost:8080/game");
        String games = serverFacade.makeRequest(url, authToken.authToken(), "GET");

        assertTrue(games.length() > 10);
    }

    @Test
    public void makeRequestTestFalse() throws Exception {
        UserData registerTest = new UserData("testuser", "passwordtest", "email@gmail.com");
        String answer = serverFacade.registerCall(registerTest);

        Gson gson = new Gson();
        AuthData authToken = gson.fromJson(answer, AuthData.class);
        GameData newGame = new GameData(0, null,null, "gameTest", null);

        serverFacade.createGameCall(newGame, authToken.authToken());
        serverFacade.listGamesCall(authToken.authToken());

        URL url = new URL("http://localhost:8080/game");
        assertThrows(IOException.class, () -> serverFacade.makeRequest(url, "asd", "GET"));
    }

    @Test
    public void makePostRequestTestTrue() throws Exception {
        UserData registerTest = new UserData("testuser", "passwordtest", "email@gmail.com");
        URL url = new URL("http://localhost:8080/user");
        String answer = serverFacade.makePostRequest(url, registerTest, null, false, "POST");

        assertTrue(answer.length() > 10);
    }

    @Test
    public void makePostRequestFalse() throws Exception {
        UserData registerTest = new UserData("testuser", "passwordtest", "email@gmail.com");
        URL url = new URL("http://localhost:8080/wrongurl");
        assertThrows(IOException.class, () -> serverFacade.makePostRequest(url, registerTest, null, false, "POST"));

    }

}
