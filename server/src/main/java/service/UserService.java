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

    //Checks if user exists, if so throws error, if not creates new User and AuthData
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

    //Checks if user exists, if so checks password and returns authData
    public AuthData loginUser(UserData userInfo) throws DataAccessException {
        if (!userDAO.userExists(userInfo.username())) {
            throw new DataAccessException("User doesnt exist");
        }

        UserData user = userDAO.getUser(userInfo.username());

        //If user is found, create authData and return it to handler
        if (user.password().equals(userInfo.password())) {
            return authDAO.createAuth(user.username());
        } else {
            return null;
        }
    }

    //authDAO searches for authToken and returns either true if it was deleted, or false if it couldn't find it
    public boolean logoutUser(String authToken) {
        return authDAO.deleteAuthData(authToken);
    }

    public void clearData() {
        userDAO.clearUsers();
    }
}
