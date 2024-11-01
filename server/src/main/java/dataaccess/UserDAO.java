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


    //Add userData into sql server UserData table (first hash the password)
    public UserData createUserSQL(UserData userData) throws DataAccessException {
        System.out.println("uhh");
        String hashedPassword = hashPassword(userData.password());

        String sqlQuery = "INSERT INTO UserData (username, password, email) VALUES (?, ?, ?)";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlQuery)) {
                preparedStatement.setString(1, userData.username());
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, userData.email());
                preparedStatement.executeUpdate();
                return userData;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    //Executes a SQL statment to return info given a username, and turns it into a UserData object
    public UserData getUserSQL(String givenUsername) throws DataAccessException {
        String sqlQuery = "SELECT username, password, email FROM UserData WHERE username = ?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlQuery)) {
                preparedStatement.setString(1, givenUsername);
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    UserData newData = new UserData(rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email"));
                    System.out.println(newData);
                    return newData;
                } else {
                    return null;
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    //Executes a SQL statement to see if a user is found from the UserData table
    public boolean userExistsSQL(String givenUsername) throws DataAccessException {
        String sqlQuery = "SELECT username FROM UserData WHERE username = ?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlQuery)) {
                preparedStatement.setString(1, givenUsername);
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    UserData newData = new UserData(rs.getString("username"), null, null);
                    return Objects.equals(newData.username(), givenUsername);
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean clearUsers() throws DataAccessException {
        String sqlQuery = "DELETE FROM UserData";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlQuery)) {
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * BELOW ARE NON SQL FUNCTIONS FOR DATA ACCESS
     * */



//    public boolean isEmpty() {
//        return this.users.isEmpty();
//    }
//
//    //Add userData into users arraylist
//    public UserData createUser(UserData userData) {
//        this.users.add(userData);
//        return userData;
//    }
//
//    //Loop through array to find user based on username, return user
//    public UserData getUser(String username) {
//        UserData user = null;
//        for (UserData currentUser : this.users) {
//            if (Objects.equals(currentUser.username(), username)) {
//                user = currentUser;
//                break;
//            }
//        }
//        return user;
//    }
//
//    //Loop through array and if there is a matching username, return true
//    public boolean userExists(String username) {
//        for (UserData currentUser : this.users) {
//            if (Objects.equals(currentUser.username(), username)) {
//                return true;
//            }
//        }
//        return false;
//    }

    //Used to hash password before adding to database
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }


}
