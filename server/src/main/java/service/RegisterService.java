package service;
import dataaccess.UserDAO;
import model.*;

public class RegisterService {
    private final UserDAO userDAO = new UserDAO(); // Create an instance of UserDAO

    public UserData registerNewUser(UserData newInfo) {
        UserData createdUser = userDAO.createUser(newInfo);
        return createdUser;
    }
}
