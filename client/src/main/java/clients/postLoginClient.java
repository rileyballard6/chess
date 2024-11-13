package clients;

import facade.ServerFacade;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class postLoginClient {

    ServerFacade serverFacade = new ServerFacade();
    private String authToken = null;
    Map<String, String> currentGames = new HashMap<>();

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

    public String createGameClient(String[] gameDetails) throws Exception {
        if (gameDetails.length != 2) {
            return "Incorrect number of args.";
        }

        GameData newGame = new GameData(0, null, null, gameDetails[1], null);

        try {
            String createdGame = serverFacade.createGameCall(newGame, authToken);
            Gson gson = new Gson();

            Map<String, Object> responseMap = gson.fromJson(createdGame, Map.class);

            if (responseMap.containsKey("gameID")) {
                return "Game created with gameID: " + responseMap.get("gameID").toString();
            } else if (responseMap.containsKey("message")) {
                return responseMap.get("message").toString();
            }
        } catch (Exception e) {
            return "Unable to create game.";
        }

        return null;
    }

    public String listGamesClient() {
        try {
            String listGames = serverFacade.listGamesCall(authToken);
            Gson gson = new Gson();

            Map<String, Object> responseMap = gson.fromJson(listGames, Map.class);
            List<Map<String, Object>> games = (List<Map<String, Object>>) responseMap.get("games");

            System.out.println("Games:");

            int count = 1;
            for (Map<String, Object> game : games) {
                currentGames.put(String.valueOf(count), game.get("gameID").toString());
                String gameName = (String) game.getOrDefault("gameName", "Unnamed Game");
                String whitePlayer = (String) game.getOrDefault("whiteUsername", "empty");
                String blackPlayer = (String) game.getOrDefault("blackUsername", "empty");

                System.out.printf("    %d. %s%n", count++, gameName);
                System.out.printf("       White player: %s%n", whitePlayer);
                System.out.printf("       Black player: %s%n", blackPlayer);
            }
            System.out.println(currentGames);
            return "";

        } catch (Exception e) {
            return "User not authorized";
        }
    }

    public String joinGameClient(String gameId, String playerColor) {
        return "";
    }

    public String observeGameClient(String gameId) {
        return "";
    }
}
