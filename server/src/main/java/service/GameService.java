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
import java.util.Objects;

public class GameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    //Check if auth token is valid, then assign game to data and send it to Data Access for storage
    //Data access will send back the gameID generated
    public int createGame(GameData initialData, String authToken) throws DataAccessException {
        if (!authDAO.findAuthSQL(authToken)) {
            throw new DataAccessException("Unauthorized");
        }

        GameData newGame = initialData.addGame();
        return gameDAO.createGameSQL(newGame);

    }

    //Checks Auth Token, then Retrieves a list of all the games from DataAccess,
    // then filters out the actual ChessGame Object
    public ArrayList<GameData> getAllGames(String authToken) throws DataAccessException {
        if (!authDAO.findAuthSQL(authToken)) {
            throw new DataAccessException("Unauthorized");
        }

        ArrayList<GameData> allGames = gameDAO.getGamesSQL();
        ArrayList<GameData> filteredGames = new ArrayList<>();

        for (GameData game : allGames) {
            filteredGames.add(game.removeGame());
        }

        return filteredGames;
    }

    //Checks authToken first, throws error if invalid. Gets auth data, checks if game exists, updates game
    public boolean joinGame(JoinGameData gameRequest, String authToken) throws DataAccessException {
        if (!authDAO.findAuthSQL(authToken)) {
            throw new DataAccessException("Unauthorized");
        }

        AuthData user = authDAO.getAuthDataSQL(authToken);

        if (!gameDAO.gameExistsSQL(gameRequest.gameID())) {
            throw new DataAccessException("Game doesn't exist");
        }


        if (!Objects.equals(gameRequest.playerColor(), "BLACK") &&
                !Objects.equals(gameRequest.playerColor(), "WHITE")) {
            System.out.println("test3");
            throw new DataAccessException("Team color is null");
        } else {
            return gameDAO.updateGameSQL(gameRequest, user);
        }



    }


}
