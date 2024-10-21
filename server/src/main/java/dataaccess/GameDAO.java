package dataaccess;

import java.util.ArrayList;
import java.util.Random;

import model.*;

public class GameDAO {
    private final ArrayList<GameData> gameData = new ArrayList<>();

    public int createGame(GameData game) {
        Random rand = new Random();
        game = game.addId(rand.nextInt(1000));
        gameData.add(game);

        return game.gameId();
    }

    public ArrayList<GameData> getGames() {
        return gameData;
    }
}
