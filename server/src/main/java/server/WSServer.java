package server;
import com.google.gson.Gson;

import org.eclipse.jetty.websocket.api.Session;
import spark.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.messages.ServerMessage;

@WebSocket
public class WSServer {
    public static void main(String[] args) {
        new Server().run(8080);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", WSServer.class);
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));

        Spark.awaitInitialization();
        return Spark.port();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        var body = getBody(message, websocket.commands.UserGameCommand.class);
        System.out.println(body.getCommandType());

        switch (body.getCommandType()) {
            case CONNECT, LEAVE, RESIGN -> {
                ServerMessage newMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
                session.getRemote().sendString(new Gson().toJson(newMessage));
            }
            case MAKE_MOVE -> {
                ServerMessage newMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                session.getRemote().sendString(new Gson().toJson(newMessage));
            }
        }
    }

    private static <T> T getBody(String message, Class<T> clazz) {
        var body = new Gson().fromJson(message, clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }
}
