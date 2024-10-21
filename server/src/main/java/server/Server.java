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


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}
