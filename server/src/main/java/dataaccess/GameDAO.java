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


    public boolean updateGameSQL(JoinGameData gameRequest, AuthData playerAuth) throws DataAccessException {
        String sqlQuery = "SELECT gameID, whiteUsername, blackUsername FROM GameData WHERE gameID = ?";
        GameData game = null;

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlQuery)) {
                preparedStatement.setInt(1, gameRequest.gameID());
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    game = new GameData(rs.getInt("gameID"),
                            rs.getString("whiteUsername"),
                            rs.getString("blackUsername"),
                            null, null);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println(game);
        System.out.println(gameRequest);

        if (game.whiteUsername() == null && Objects.equals(gameRequest.playerColor(), "WHITE")) {
            return addPlayerToGameSQL(game, playerAuth, "WHITE");
        } else if (game.blackUsername() == null && Objects.equals(gameRequest.playerColor(), "BLACK")) {
            return addPlayerToGameSQL(game, playerAuth, "BLACK");
        }
        System.out.println("That didnt work...");

        return false;
    }

    public boolean addPlayerToGameSQL(GameData game, AuthData authData, String color) throws DataAccessException {
        String sqlQuery = Objects.equals(color, "WHITE")
                ? "UPDATE GameData SET whiteUsername = ? WHERE gameID = ?"
                : "UPDATE GameData SET blackUsername = ? WHERE gameID = ?";
        System.out.println(authData);

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlQuery)) {
                preparedStatement.setString(1, authData.username());
                preparedStatement.setInt(2, game.gameID());
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public boolean clearGames() throws DataAccessException {
        String sqlQuery = "DELETE FROM GameData";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlQuery)) {
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
    }


    /**
     * BELOW ARE NON SQL FUNCTIONS FOR DATA ACCESS
     * */


    //Generate randomID, assign it to game and add it to arraylist
    public int createGame(GameData game) {
        Random rand = new Random();
        game = game.addId(rand.nextInt(1000));
        gameData.add(game);

        return game.gameID();
    }

    //Return array list
    public ArrayList<GameData> getGames() {
        return gameData;
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
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return gameData.isEmpty();
    }
}
