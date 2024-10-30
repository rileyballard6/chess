package dataaccess;
import model.*;
import java.util.ArrayList;
import java.util.Objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDAO {
    private final ArrayList<UserData> users = new ArrayList<>();

    //Add userData into users arraylist
    public UserData createUser(UserData userData) {
        this.users.add(userData);
        return userData;
    }

    //Loop through array to find user based on username, return user
    public UserData getUser(String username) {
        UserData user = null;
        for (UserData currentUser : this.users) {
            if (Objects.equals(currentUser.username(), username)) {
                user = currentUser;
                break;
            }
        }
        return user;
    }

    //Loop through array and if there is a matching username, return true
    public boolean userExists(String username) {
        for (UserData currentUser : this.users) {
            if (Objects.equals(currentUser.username(), username)) {
                return true;
            }
        }
        return false;
    }

    public boolean clearUsers() {
        this.users.clear();
        return true;
    }

    public boolean isEmpty() {
        return this.users.isEmpty();
    }


}
