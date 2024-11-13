package clients;

import com.google.gson.Gson;
import facade.ServerFacade;
import model.AuthData;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;

public class postLoginClient {

    ServerFacade serverFacade = new ServerFacade();
    private String authToken = null;

    public postLoginClient() throws URISyntaxException {
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String logoutClient() {
        if (authToken == null) {
            return "User not logged in";
        }

        try {
            String returnedData = serverFacade.logoutCall(authToken);
            if (Objects.equals(returnedData, "{}")) {
                return "User logged out successfully";
            }

        } catch (Exception e) {
            return "An error occurred logging out";
        }
        return "Unknown error.";
    }

    public String createGameClient(String gameName) {
        return "";
    }

    public String listGamesClient() {
        return "";
    }

    public String joinGameClient(String gameId, String playerColor) {
        return "";
    }

    public String observeGameClient(String gameId) {
        return "";
    }
}
