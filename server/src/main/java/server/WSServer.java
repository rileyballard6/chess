package server;
import com.google.gson.Gson;

import org.eclipse.jetty.websocket.api.Session;
import spark.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.messages.ServerMessage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@WebSocket
public class WSServer {
    private static final Set<Session> activeSessions = new CopyOnWriteArraySet<>();


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


    @OnWebSocketConnect
    public void onConnect(Session session) {
        Map<String, List<String>> headers = session.getUpgradeRequest().getHeaders();

        activeSessions.add(session);
        System.out.println("New connection added");
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        activeSessions.remove(session);
        System.out.println("Connection closed");
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        var body = getBody(message, websocket.commands.UserGameCommand.class);

        switch (body.getCommandType()) {
            case CONNECT -> {
                ServerMessage newMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, "Game loaded");
                session.getRemote().sendString(new Gson().toJson(newMessage));

                ServerMessage joinMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "A new player has joined");
                for (Session activeSession : activeSessions) {
                    if (!activeSession.equals(session) && activeSession.isOpen()) {
                        activeSession.getRemote().sendString(new Gson().toJson(joinMessage));
                    }
                }
            }
            case LEAVE -> {
                ServerMessage leaveMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Player has left the game");
                for (Session activeSession : activeSessions) {
                    if (!activeSession.equals(session) && activeSession.isOpen()) {
                        activeSession.getRemote().sendString(new Gson().toJson(leaveMessage));
                    }
                }
                activeSessions.remove(session);
            }

            case RESIGN -> {
                ServerMessage leaveMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Player has resigned!");
                for (Session activeSession : activeSessions) {
                    activeSession.getRemote().sendString(new Gson().toJson(leaveMessage));
                }
            }

            case MAKE_MOVE -> {
                ServerMessage loadMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, "Player has made a move");
                for (Session activeSession : activeSessions) {
                    activeSession.getRemote().sendString(new Gson().toJson(loadMessage));
                }

                ServerMessage newMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "This is a notification");

                for (Session activeSession : activeSessions) {
                    System.out.println(activeSession);
                    if (!activeSession.equals(session) && activeSession.isOpen()) {
                         activeSession.getRemote().sendString(new Gson().toJson(newMessage));
                    }
                }


            }
            default -> {
                ServerMessage newMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error occurred");
                session.getRemote().sendString(new Gson().toJson(newMessage));
            }
        }
    }

    private boolean confirmAuth() { return false; }

    private static <T> T getBody(String message, Class<T> clazz) {
        var body = new Gson().fromJson(message, clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }
}
