package dataaccess;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;

public class GameDAO {
    private final ArrayList<GameData> gameData = new ArrayList<>();

    //Generate randomID, assign it to game and add it to arraylist
//    public int createGame(GameData game) {
//        Random rand = new Random();
//        game = game.addId(rand.nextInt(1000));
//        gameData.add(game);
//
//        return game.gameID();
//    }

    public int createGameSQL(GameData game) throws DataAccessException {
        Random rand = new Random();
        game = game.addId(rand.nextInt(1000));

        Gson gson = new Gson();
        String gameJson = gson.toJson(game.game());
        String sqlQuery = "INSERT INTO GameData " +
                "(gameID, whiteUsername, blackUsername, gameName, game) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlQuery)) {
                preparedStatement.setInt(1, game.gameID());
                preparedStatement.setString(2, game.whiteUsername());
                preparedStatement.setString(3, game.blackUsername());
                preparedStatement.setString(4, game.gameName());
                preparedStatement.setString(5, gameJson);
                preparedStatement.executeUpdate();
                return game.gameID();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Return array list
//    public ArrayList<GameData> getGames() {
//        return gameData;
//    }

    public ArrayList<GameData> getGamesSQL() throws DataAccessException {
        String sqlQuery = "SELECT * FROM GameData";
        ArrayList<GameData> allGames = new ArrayList<>();

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlQuery)) {
                var rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    GameData newData = new GameData(rs.getInt("gameID"),
                            rs.getString("whiteUsername"),
                            rs.getString("blackUsername"),
                            rs.getString("gameName"), null);
                    allGames.add(newData);
                }
                return allGames;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Loop through array and find requested game with ID, return true if found
    public boolean gameExists(int gameID) {
        for (GameData game : gameData) {
            if (game.gameID() == gameID) {
                return true;
            }
        }
        return false;
    }

    public boolean gameExistsSQL(int gameID) throws DataAccessException {
        String sqlQuery = "SELECT gameID FROM GameData where gameID = ?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlQuery)) {
                preparedStatement.setInt(1, gameID);
                var rs = preparedStatement.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Loop through array and update the GameData with username in place of team
    public boolean updateGame(JoinGameData gameRequest, AuthData playerAuth) {
        String teamColor = gameRequest.playerColor();

        for (int i = 0; i < gameData.size(); i++) {
            GameData game = gameData.get(i);
            if (game.gameID() == gameRequest.gameID()) {
                if (Objects.equals(teamColor, "WHITE") && game.whiteUsername() == null) {
                    game = game.addPlayerWhite(playerAuth.username());
                } else if (Objects.equals(teamColor, "BLACK") && game.blackUsername() == null) {
                    game = game.addPlayerBlack(playerAuth.username());
                } else {
                    return false;
                }
                gameData.set(i, game);
                System.out.println(game);
                return true;

            }
        }
        return false;
    }

    public boolean clearGames() {
        this.gameData.clear();
        return true;
    }

    public boolean isEmpty() {
        return gameData.isEmpty();
    }
}
