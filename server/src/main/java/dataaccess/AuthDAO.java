package dataaccess;
import model.AuthData;

import java.util.ArrayList;
import java.util.UUID;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AuthDAO {
    private final ArrayList<AuthData> authTokens = new ArrayList<>();


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
