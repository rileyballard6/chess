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


    public boolean findAuthSQL(String authToken) throws DataAccessException {
        String sqlQuery = "SELECT authToken FROM AuthData WHERE authToken = ?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlQuery)) {
                preparedStatement.setString(1, authToken);
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    AuthData newData = new AuthData(rs.getString("authToken"), null);
                    return Objects.equals(newData.authToken(), authToken);
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean deleteAuthDataSQL(String authToken) throws DataAccessException {
        String sqlQuery = "DELETE FROM AuthData WHERE authToken = ?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlQuery)) {
                preparedStatement.setString(1, authToken);

                int rowsAffected = preparedStatement.executeUpdate();

                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            return false;
        }

    }

    public AuthData getAuthDataSQL(String authToken) throws DataAccessException {
        String sqlQuery = "SELECT username, authToken FROM AuthData WHERE authToken = ?";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(sqlQuery)) {
                preparedStatement.setString(1, authToken);
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    AuthData newData = new AuthData(rs.getString("authToken"), rs.getString("username"));
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

    public boolean clearAuth() throws DataAccessException {
        String sqlQuery = "DELETE FROM AuthData";

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


    //Insert AuthData into array
    public AuthData createAuth(String username) {
        String newAuthToken = generateToken();
        AuthData newAuthData = new AuthData(newAuthToken, username);
        authTokens.add(newAuthData);
        return newAuthData;
    }

    //Loop through array and return true if auth is found
    public boolean findAuth(String authToken) {
        for (AuthData currentAuth : authTokens) {
            if (currentAuth.authToken().equals(authToken)) {
                return true;
            }
        }
        return false;
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


    //Loop through array and return the AuthData
    public AuthData getAuthData(String authToken) {
        for (AuthData currentAuth : authTokens) {
            if (currentAuth.authToken().equals(authToken)) {
                return currentAuth;
            }
        }
        return null;
    }


    public boolean isEmpty() {
        return authTokens.isEmpty();
    }


    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
