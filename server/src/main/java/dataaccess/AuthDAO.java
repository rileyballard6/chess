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

    public AuthData getAuth(String authToken) {
        return null;
    }


    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
