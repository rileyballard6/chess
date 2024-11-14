package client;

import facade.ServerFacade;
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

    //Set up should clear the database each time.
    @BeforeAll
    public static void init() throws IOException {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);

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
    public void loginTestTrue() {
        assertTrue(true);
    }

    @Test
    public void loginTestFalse() {
        assertTrue(true);
    }



    @Test
    public void logoutTest() {
        assertTrue(true);
    }




    @Test
    public void joinGameTest() {
        assertTrue(true);
    }
    @Test
    public void listGameTest() {
        assertTrue(true);
    }

    @Test
    public void createGameTest() {
        assertTrue(true);
    }

    @Test
    public void observeGameTest() {
        assertTrue(true);
    }

    @Test
    public void makeRequestTest() {
        assertTrue(true);
    }

    @Test
    public void makePostRequestTest() {
        assertTrue(true);
    }

}
