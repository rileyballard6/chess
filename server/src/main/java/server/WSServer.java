package server;
import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import spark.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebSocket
public class WSServer {
    private static final Map<Integer, Set<Session>> GAME_SESSSIONS = new HashMap<>();
    private AuthDAO authDAO = new AuthDAO();
    private GameDAO gameDAO = new GameDAO();
    private ChessGame sampleGame = null;


    public static void main(String[] args) {
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.webSocket("/ws", WSServer.class);
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));

        Spark.awaitInitialization();
        return Spark.port();
    }


    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("New connection added");
        sampleGame = new ChessGame();
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        removeSession(session);
        System.out.println("Connection closed");
    }

    // Main method to calculate which message to send back to the client
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {

        var body = getBody(message, websocket.commands.UserGameCommand.class);

        addSessionToGame(body.getGameID(), session);

        boolean authExists = authDAO.findAuthSQL(body.getAuthToken());
        boolean gameExists = gameDAO.gameExistsSQL(body.getGameID());

        if (!authExists) {
            sendMessage(session, ServerMessage.ServerMessageType.ERROR, "Auth invalid");
            return;
        }

        AuthData authData = authDAO.getAuthDataSQL(body.getAuthToken());

        if (!gameExists) {
            sendMessage(session, ServerMessage.ServerMessageType.ERROR, "Game invalid");
            return;
        }

        Set<Session> sessions = GAME_SESSSIONS.computeIfAbsent(body.getGameID(), k -> new HashSet<>());

        switch (body.getCommandType()) {
            case CONNECT -> {

                sendMessage(session, ServerMessage.ServerMessageType.LOAD_GAME, "Game loaded");

                for (Session activeSession : sessions) {
                    if (!activeSession.equals(session) && activeSession.isOpen()) {
                        sendMessage(activeSession, ServerMessage.ServerMessageType.NOTIFICATION, "New Player joined game");
                    }
                }
            }
            case LEAVE -> {
                for (Session activeSession : sessions) {
                    if (!activeSession.equals(session) && activeSession.isOpen()) {
                        sendMessage(activeSession, ServerMessage.ServerMessageType.NOTIFICATION, "Player left game");
                    }
                }

                removeSession(session);

                leaveGame(authData.username(), body.getGameID());
            }

            case RESIGN -> {

                if (!isUserInGame(authData.username(), body.getGameID())) {
                    sendMessage(session, ServerMessage.ServerMessageType.ERROR, "Game does not exist");
                    return;
                }

                for (Session activeSession : sessions) {
                    sendMessage(activeSession, ServerMessage.ServerMessageType.NOTIFICATION, "Player has resigned");
                }

                clearOneGame(body.getGameID());
            }

            case MAKE_MOVE -> {

                System.out.println(body.getMove());

                if (!isUserInGame(authData.username(), body.getGameID())) {
                    sendMessage(session, ServerMessage.ServerMessageType.ERROR, "Player not in game");
                    return;
                }

                if (getTeamColor(authData.username(), body.getGameID()) != sampleGame.getTeamTurn()) {
                    sendMessage(session, ServerMessage.ServerMessageType.ERROR, "Not your turn");
                    return;
                }

                try {
                    System.out.println("Team turn: " + sampleGame.getTeamTurn());
                    sampleGame.makeMove(body.getMove());
                } catch (InvalidMoveException e) {
                    sendMessage(session, ServerMessage.ServerMessageType.ERROR, "Invalid Move or not your turn");
                    return;
                }

                for (Session activeSession : sessions) {
                    sendMessage(activeSession, ServerMessage.ServerMessageType.LOAD_GAME, "Game loaded");
                }

                for (Session activeSession : sessions) {
                    if (!activeSession.equals(session) && activeSession.isOpen()) {
                        sendMessage(activeSession, ServerMessage.ServerMessageType.NOTIFICATION, "Notification");
                    }
                }

            }
        }
    }

    //Loops through the map to find the session and removes it
    public void removeSession(Session session) {
        Iterator<Map.Entry<Integer, Set<Session>>> iterator = GAME_SESSSIONS.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, Set<Session>> entry = iterator.next();
            Set<Session> sessions = entry.getValue();

            if (sessions.contains(session)) {
                sessions.remove(session);

                if (sessions.isEmpty()) {
                    iterator.remove();
                }

                break;
            }
        }
    }

    //Adds a session to a game, or creates a new set based on the gameID
    public void addSessionToGame(int gameID, Session session) {
        Set<Session> sessions = GAME_SESSSIONS.computeIfAbsent(gameID, k -> new HashSet<>());

        sessions.add(session);
    }

    //Helper function to send the message
    private void sendMessage(Session session, ServerMessage.ServerMessageType messageType, String message) throws IOException {
        ServerMessage messageToSend = new ServerMessage(messageType, message);
        session.getRemote().sendString(new Gson().toJson(messageToSend));
    }

    //Clears the game from the database
    public boolean clearOneGame(int gameID) throws DataAccessException {
        String sqlQuery = "DELETE FROM GameData WHERE gameID = ?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlQuery)) {
                preparedStatement.setInt(1, gameID);
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
    }
    // Removes a user from game in the database
    public boolean leaveGame(String username, int gameID) throws DataAccessException {
        String sqlQuery = "UPDATE GameData " +
                "SET whiteUsername = CASE WHEN whiteUsername = ? THEN NULL ELSE whiteUsername END, " +
                "    blackUsername = CASE WHEN blackUsername = ? THEN NULL ELSE blackUsername END " +
                "WHERE gameID = ?";

        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, username);
            preparedStatement.setInt(3, gameID);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    //Gets what team the player is on based on their username and gameID
    public ChessGame.TeamColor getTeamColor(String username, int gameID) throws DataAccessException {
        String sqlQuery = "SELECT whiteUsername, blackUsername FROM GameData WHERE gameID = ?";

        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, gameID);

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String whiteUsername = resultSet.getString("whiteUsername");
                    String blackUsername = resultSet.getString("blackUsername");

                    if (username.equals(whiteUsername)) {
                        return ChessGame.TeamColor.WHITE;
                    } else if (username.equals(blackUsername)) {
                        return ChessGame.TeamColor.BLACK;
                    }
                }
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }


    //Checks to make sure a user is actually in a game
    public boolean isUserInGame(String username, int gameID) throws DataAccessException {
        String sqlQuery = "SELECT whiteUsername, blackUsername FROM GameData WHERE gameID = ?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlQuery)) {
                preparedStatement.setInt(1, gameID);
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    if (Objects.equals(rs.getString("whiteUsername"), username) || Objects.equals(rs.getString("blackUsername"), username)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    //Get the body from WS message
    private static <T> T getBody(String message, Class<T> clazz) {
        var body = new Gson().fromJson(message, clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }
}
