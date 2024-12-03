package server;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import org.eclipse.jetty.websocket.api.Session;
import spark.*;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.sql.SQLException;

@WebSocket
public class WSServer {
    public static void main(String[] args) {
        new Server().run(8080);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", WSServer.class);
        System.out.println("WebSocket server initialized at /ws");
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));

        Spark.awaitInitialization();
        return Spark.port();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        session.getRemote().sendString("WebSocket response: " + message);
    }
}
