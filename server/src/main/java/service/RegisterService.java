package service;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.*;

public class RegisterService {
    private final UserDAO userDAO = new UserDAO(); // Create an instance of UserDAO

    public UserData registerNewUser(UserData newInfo) throws DataAccessException {
        if (userDAO.userExists(newInfo.username())) {
            throw new DataAccessException("Username Already Exists");
        }

        UserData createdUser = userDAO.createUser(newInfo);
        return createdUser;
    }
}
