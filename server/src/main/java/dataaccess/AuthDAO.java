package dataaccess;
import model.AuthData;

import java.util.ArrayList;
import java.util.UUID;


public class AuthDAO {
    private final ArrayList<AuthData> authTokens = new ArrayList<>();

    public void createAuth(String username) {
        String newAuthToken = generateToken();
        AuthData newAuthData = new AuthData(username, newAuthToken);
        authTokens.add(newAuthData);
    }

    public AuthData getAuth(String authToken) {
        return null;
    }


    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
