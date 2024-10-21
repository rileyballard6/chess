package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameData;

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

    public ArrayList<GameData> getAllGames(String authToken) throws DataAccessException {
        if (!authDAO.findAuth(authToken)) {
            throw new DataAccessException("Unauthorized");
        }

        ArrayList<GameData> allGames = gameDAO.getGames();
        System.out.println(allGames);
        return allGames;
    }


}
