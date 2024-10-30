package dataaccess;
import model.*;
import java.util.ArrayList;
import java.util.Objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;


public class UserDAO {
    private final ArrayList<UserData> users = new ArrayList<>();

    //Add userData into users arraylist
    public UserData createUser(UserData userData) {
        this.users.add(userData);
        return userData;
    }

    public UserData createUserSQL(UserData userData) throws DataAccessException {
        String hashedPassword = hashPassword(userData.password());
        System.out.println("request recieved");

        String sqlQuery = "INSERT INTO UserData (username, password, email) VALUES (?, ?, ?)";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlQuery)) {
                preparedStatement.setString(1, userData.username());
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, userData.email());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("error");
            System.out.println(e);
            throw new RuntimeException(e);
        }

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

    //Used to hash password before adding to database
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // compare the given password with the hashed one
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }


}
