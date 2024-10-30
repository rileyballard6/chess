package dataaccess;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.*;

public class GameDAO {
    private final ArrayList<GameData> gameData = new ArrayList<>();

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
