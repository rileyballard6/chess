package model;
import chess.ChessGame;

public record GameData(int gameId, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public GameData addGame() {
        return new GameData(gameId, whiteUsername, blackUsername, gameName, new ChessGame());
    }

    public GameData addId(int id) {
        return new GameData(id, whiteUsername, blackUsername, gameName, game);

    }
}
