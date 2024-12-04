package dataaccess;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import java.sql.SQLException;

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


        if (game.whiteUsername() == null && Objects.equals(gameRequest.playerColor(), "WHITE")) {
            return addPlayerToGameSQL(game, playerAuth, "WHITE");
        } else if (game.blackUsername() == null && Objects.equals(gameRequest.playerColor(), "BLACK")) {
            return addPlayerToGameSQL(game, playerAuth, "BLACK");
        }

        return false;
    }

    public boolean addPlayerToGameSQL(GameData game, AuthData authData, String color) throws DataAccessException {
        String sqlQuery = Objects.equals(color, "WHITE")
                ? "UPDATE GameData SET whiteUsername = ? WHERE gameID = ?"
                : "UPDATE GameData SET blackUsername = ? WHERE gameID = ?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlQuery)) {
                preparedStatement.setString(1, authData.username());
                preparedStatement.setInt(2, game.gameID());
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
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

}

