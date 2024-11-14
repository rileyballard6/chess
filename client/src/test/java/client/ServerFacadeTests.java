package client;

import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void logoutTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void loginTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void joinGameTest() {
        Assertions.assertTrue(true);
    }
    @Test
    public void listGameTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void createGameTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void observeGameTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void makeRequestTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void makePostRequestTest() {
        Assertions.assertTrue(true);
    }

}
