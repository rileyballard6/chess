package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class ClearDataService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public ClearDataService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    //Accesses the clear method on each data access object. Removes all elements from data structure
    public boolean clearAllData() throws DataAccessException {
        boolean authStatus = authDAO.clearAuth();
        boolean userStatus = userDAO.clearUsers();
        boolean gameStatus = gameDAO.clearGames();
        System.out.println(authStatus);
        System.out.println(userStatus);
        System.out.println(gameStatus);
        return authStatus && userStatus && gameStatus;
    }
}
