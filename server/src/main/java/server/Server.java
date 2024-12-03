package server;

import java.sql.SQLException;
import chess.ChessGame;
import chess.ChessPiece;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import spark.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;


public class Server {

    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        new Server().run(8080);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        new WSServer().run(8080);


        try {
            DatabaseManager.createDatabase();
            try (var conn = DatabaseManager.getConnection()) {

                String createUserDataTable = "CREATE TABLE IF NOT EXISTS UserData (" +
                        "username VARCHAR(50) PRIMARY KEY, " +
                        "password VARCHAR(255) NOT NULL, " +
                        "email VARCHAR(100) NOT NULL);";

                String createGameDataTable = "CREATE TABLE IF NOT EXISTS GameData (" +
                        "gameID INT AUTO_INCREMENT PRIMARY KEY, " +
                        "whiteUsername VARCHAR(50), " +
                        "blackUsername VARCHAR(50), " +
                        "gameName VARCHAR(100), " +
                        "game JSON);";

                String createAuthDataTable = "CREATE TABLE IF NOT EXISTS AuthData (" +
                        "authToken VARCHAR(255) PRIMARY KEY, " +
                        "username VARCHAR(50));";


                // Execute table creation statements
                try (var stmt = conn.createStatement()) {
                    stmt.executeUpdate(createUserDataTable);
                    stmt.executeUpdate(createGameDataTable);
                    stmt.executeUpdate(createAuthDataTable);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (DataAccessException e) {
            System.out.println("Error creating database or tables: " + e);
        }


        //Register new user
        Spark.post("/user", Handler::registerHandler);

        //Login and Logout
        Spark.post("/session", Handler::loginHandler);
        Spark.delete("/session", Handler::loginHandler);

        //List Games, Create game, and Join Game
        Spark.get("/game", Handler::gameHandler);
        Spark.post("/game", Handler::gameHandler);
        Spark.put("/game", Handler::gameHandler);

        //Delete all data
        Spark.delete("/db", Handler::deleteHandler);

        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}
