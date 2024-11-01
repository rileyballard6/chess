package model;
import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public GameData addGame() {
        return new GameData(gameID, whiteUsername, blackUsername, gameName, new ChessGame());
    }

    public GameData addId(int id) {
        return new GameData(id, whiteUsername, blackUsername, gameName, game);
    }

    //This method is used when returning all games, so the response doesnt include all the Chess game info
    public GameData removeGame() {
        return new GameData(gameID, whiteUsername, blackUsername, gameName, null);
    }

}
