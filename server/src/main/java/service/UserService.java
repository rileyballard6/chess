package service;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import model.*;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData registerNewUser(UserData newInfo) throws DataAccessException {
        if (userDAO.userExists(newInfo.username())) {
            throw new DataAccessException("Username Already Exists");
        }
        UserData createdUser = userDAO.createUser(newInfo);

        if (createdUser != null) {
            return authDAO.createAuth(createdUser.username());
        } else {
            return null;
        }
    }

    public AuthData loginUser(UserData userInfo) throws DataAccessException {
        if (!userDAO.userExists(userInfo.username())) {
            throw new DataAccessException("User doesnt exist");
        }

        UserData user = userDAO.getUser(userInfo.username());

        if (user.password().equals(userInfo.password())) {
            return new AuthData("asidjw", user.username());
        } else {
            return null;
        }
    }

    public void clearData() {
        userDAO.clearUsers();
    }
}
