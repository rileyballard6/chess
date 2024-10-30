package dataaccess;
import model.AuthData;
import model.UserData;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AuthDAO {
    private final ArrayList<AuthData> authTokens = new ArrayList<>();


    //Insert AuthData into array
//    public AuthData createAuth(String username) {
//        String newAuthToken = generateToken();
//        AuthData newAuthData = new AuthData(newAuthToken, username);
//        authTokens.add(newAuthData);
//        return newAuthData;
//    }

    //Creates AuthData object and runs a SQL statement to insert it into the AuthData table
    public AuthData createAuthSQL(String username) {
        String newAuthToken = generateToken();
        AuthData newAuthData = new AuthData(newAuthToken, username);

        String sqlQuery = "INSERT INTO AuthData (username, authToken) VALUES (?, ?)";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlQuery)) {
                preparedStatement.setString(1, newAuthData.username());
                preparedStatement.setString(2, newAuthData.authToken());
                preparedStatement.executeUpdate();
                return newAuthData;
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    //Loop through array and return true if auth is found
//    public boolean findAuth(String authToken) {
//        for (AuthData currentAuth : authTokens) {
//            if (currentAuth.authToken().equals(authToken)) {
//                return true;
//            }
//        }
//        return false;
//    }

    public boolean findAuthSQL(String authToken) throws DataAccessException {
        String sqlQuery = "SELECT authToken FROM AuthData WHERE authToken = ?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlQuery)) {
                preparedStatement.setString(1, authToken);
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    UserData newData = new UserData(rs.getString("username"), null, null);
                    return Objects.equals(newData.username(), authToken);
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Loop through array and return true when AuthData is removed
    public boolean deleteAuthData(String authToken) {
        for (int i = 0; i < authTokens.size(); i++) {
            AuthData currentAuth = authTokens.get(i);
            if (currentAuth.authToken().equals(authToken)) {
                authTokens.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean deleteAuthDataSQL(String authToken) throws DataAccessException {
        String sqlQuery = "DELETE FROM AuthData WHERE authToken = ?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlQuery)) {
                preparedStatement.setString(1, authToken);
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    UserData newData = new UserData(rs.getString("username"), null, null);
                    return Objects.equals(newData.username(), authToken);
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //Loop through array and return the AuthData
    public AuthData getAuthData(String authToken) {
        for (AuthData currentAuth : authTokens) {
            if (currentAuth.authToken().equals(authToken)) {
                return currentAuth;
            }
        }
        return null;
    }

    public boolean clearAuth() {
        this.authTokens.clear();
        return true;
    }

    public boolean isEmpty() {
        return authTokens.isEmpty();
    }


    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
