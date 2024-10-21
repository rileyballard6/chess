package dataaccess;
import model.AuthData;

import java.util.ArrayList;
import java.util.UUID;


public class AuthDAO {
    private final ArrayList<AuthData> authTokens = new ArrayList<>();

    public AuthData createAuth(String username) {
        String newAuthToken = generateToken();
        AuthData newAuthData = new AuthData(newAuthToken, username);
        authTokens.add(newAuthData);
        return newAuthData;
    }

    public boolean findAuth(String authToken) {
        for (AuthData currentAuth : authTokens) {
            if (currentAuth.authToken().equals(authToken)) {
                return true;
            }
        }
        return false;
    }

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


    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
