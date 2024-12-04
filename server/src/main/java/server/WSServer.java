package server;
import com.google.gson.Gson;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
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
    private AuthDAO authDAO = new AuthDAO();
    private GameDAO gameDAO = new GameDAO();


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

        boolean authExists = authDAO.findAuthSQL(body.getAuthToken());
        boolean gameExists = gameDAO.gameExistsSQL(body.getGameID());

        if (!authExists) {
            ServerMessage newMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Auth does not exist");
            session.getRemote().sendString(new Gson().toJson(newMessage));
            return;
        }

        if (!gameExists) {
            ServerMessage newMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Game does not exist");
            session.getRemote().sendString(new Gson().toJson(newMessage));
            return;
        }

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

                gameDAO.clearOneGame(body.getGameID());
            }

            case MAKE_MOVE -> {
                ServerMessage loadMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, "Player has made a move");
                for (Session activeSession : activeSessions) {
                    activeSession.getRemote().sendString(new Gson().toJson(loadMessage));
                }

                ServerMessage newMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "This is a notification");

                for (Session activeSession : activeSessions) {
                    if (!activeSession.equals(session) && activeSession.isOpen()) {
                        activeSession.getRemote().sendString(new Gson().toJson(newMessage));
                    }
                }


            }
        }
    }


    private boolean confirmGameID() {
        return false;
    }

    private static <T> T getBody(String message, Class<T> clazz) {
        var body = new Gson().fromJson(message, clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }
}
