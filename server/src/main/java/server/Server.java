package server;

import java.util.UUID;
import chess.ChessGame;
import chess.ChessPiece;
import spark.*;


public class Server {

    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        new Server().run(8080);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        // Register your endpoints and handle exceptions here.

        //Register new user
        Spark.post("/user", Handler::RegisterHandler);

        //Login and Logout
        Spark.post("/session", Handler::LoginHandler);
        Spark.delete("/session", Handler::LoginHandler);

        //List Games, Create game, and Join Game
        Spark.get("/game", Handler::GameHandler);
        Spark.post("/game", Handler::GameHandler);
        Spark.put("/game", Handler::GameHandler);

        //Delete all data
        Spark.delete("/db", Handler::DeleteHandler);


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}
