package model;
import chess.ChessGame;

//Need to add game GameData
public record GameData(int gameId, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
}
