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

        Spark.post("/user", Handler::RegisterHandler);
//
//        //Login and Logout
//        Spark.post("/session", this::loginUser);
//        Spark.delete("/session", this::deleteUser);
//
//        //List Games, Create game, and Join Game
//        Spark.get("/game", this::listGames);
//        Spark.post("/game", this::createGame);
//        Spark.put("/game", this::joinGame);
//
//        //Delete
//        Spark.delete("/db", this::deleteGame);


        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}
