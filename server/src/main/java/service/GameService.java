package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.JoinGameData;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class GameService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    //Check if auth token is valid, then assign game to data and send it to Data Access for storage
    //Data access will send back the gameID generated
    public int createGame(GameData initialData, String authToken) throws DataAccessException {
        if (!authDAO.findAuth(authToken)) {
            throw new DataAccessException("Unauthorized");
        }

        GameData newGame = initialData.addGame();
        int gameId = gameDAO.createGame(newGame);
        System.out.println(gameId);

        return gameId;

    }

    //Checks Auth Token, then Retrieves a list of all the games from DataAccess,
    // then filters out the actual ChessGame Object
    public ArrayList<GameData> getAllGames(String authToken) throws DataAccessException {
        if (!authDAO.findAuth(authToken)) {
            throw new DataAccessException("Unauthorized");
        }

        ArrayList<GameData> allGames = gameDAO.getGames();
        ArrayList<GameData> filteredGames = new ArrayList<>();

        for (GameData game : allGames) {
            filteredGames.add(game.removeGame());
        }

        return filteredGames;
    }

    public boolean joinGame(JoinGameData gameRequest, String authToken) throws DataAccessException {
        if (!authDAO.findAuth(authToken)) {
            throw new DataAccessException("Unauthorized");
        }

        AuthData user = authDAO.getAuthData(authToken);

        if (!gameDAO.gameExists(gameRequest.gameID())) {
            throw new DataAccessException("Game doesn't exist");
        }

        return gameDAO.updateGame(gameRequest, user);

    }


}
