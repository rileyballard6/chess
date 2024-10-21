package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ClearDataServiceTests {
    private final UserDAO testUserDAO = new UserDAO();
    private final AuthDAO testAuthDAO = new AuthDAO();
    private final GameDAO testGameDAO = new GameDAO();
    private final UserService userService = new UserService(testUserDAO, testAuthDAO);
    private final GameService gameService = new GameService(testUserDAO, testAuthDAO, testGameDAO);
    private final ClearDataService clearDataService = new ClearDataService(testUserDAO, testAuthDAO, testGameDAO);

    @Test
    @DisplayName("Delete Data Test")
    public void testDeleteData() {
        
    }
}
