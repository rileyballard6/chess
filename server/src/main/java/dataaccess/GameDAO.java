package dataaccess;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import model.*;

public class GameDAO {
    private final ArrayList<GameData> gameData = new ArrayList<>();

    public int createGame(GameData game) {
        Random rand = new Random();
        game = game.addId(rand.nextInt(1000));
        gameData.add(game);

        return game.gameID();
    }

    public ArrayList<GameData> getGames() {
        return gameData;
    }

    public boolean gameExists(int gameID) {
        for (GameData game : gameData) {
            if (game.gameID() == gameID) {
                return true;
            }
        }
        return false;
    }

    public boolean updateGame(JoinGameData gameRequest, AuthData playerAuth) throws DataAccessException {
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
