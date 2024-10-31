package service;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.crypto.Data;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    //Checks if user exists, if so throws error, check if input field is null, if so throw error,
    // if not creates new User and AuthData
    public AuthData registerNewUser(UserData newInfo) throws DataAccessException {
        if (newInfo.password() == null || newInfo.email() == null || newInfo.username() == null) {
            throw new DataAccessException("Input field is null");
        }

        if (userDAO.userExistsSQL(newInfo.username())) {
            throw new DataAccessException("Username Already Exists");
        }

        UserData createdUser = userDAO.createUserSQL(newInfo);

        if (createdUser != null) {
            return authDAO.createAuthSQL(createdUser.username());
        } else {
            return null;
        }
    }

    //Checks if user exists, if so checks password and returns authData
    //If user is found, create authData and return it to handler
    public AuthData loginUser(UserData userInfo) throws DataAccessException {
        if (!userDAO.userExistsSQL(userInfo.username())) {
            throw new DataAccessException("User doesnt exist");
        }

        UserData user = userDAO.getUserSQL(userInfo.username());

        if (checkPassword(userInfo.password(), user.password())) {
            return authDAO.createAuthSQL(user.username());
        } else {
            return null;
        }
    }

    //authDAO searches for authToken and returns either true if it was deleted, or false if it couldn't find it
    public boolean logoutUser(String authToken) throws DataAccessException {
        if (authToken == null) {
            throw new DataAccessException("Unauthorized");
        }

        return authDAO.deleteAuthDataSQL(authToken);
    }

    // compare the given password with the hashed one
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }

}
