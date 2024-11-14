package facade;


import com.google.gson.Gson;
import model.GameData;
import model.JoinGameData;
import model.UserData;

import java.io.*;
import java.net.*;
import java.util.Map;

public class ServerFacade {

    URI registerURI = new URI("http://localhost:8080/user");
    URI loginURI = new URI("http://localhost:8080/session");
    URI gameURI = new URI("http://localhost:8080/game");
    URI clearURI = new URI("http://localhost:8080/db");

    public ServerFacade() throws URISyntaxException {
    }

    public String makePostRequest(URL url, Object data, String authToken, boolean needsAuth, String type) throws Exception {
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod(type);
        http.setRequestProperty("Content-Type", "application/json; utf-8");
        if (needsAuth) {
            http.setRequestProperty("authorization", authToken);
        }
        http.setDoOutput(true);

        String jsonInputString = new Gson().toJson(data);
        try (OutputStream os = http.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        StringBuilder response = new StringBuilder();
        try (InputStream respBody = http.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(respBody, "utf-8"))) {
            String responseLine;
            while ((responseLine = reader.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        return response.toString();
    }


    public String makeRequest(URL url, String authToken, String type) throws Exception {
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod(type);
        http.setRequestProperty("Content-Type", "application/json; utf-8");
        http.setRequestProperty("authorization", authToken);
        http.setDoOutput(true);

        StringBuilder response = new StringBuilder();
        try (InputStream respBody = http.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(respBody, "utf-8"))) {
            String responseLine;
            while ((responseLine = reader.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        return response.toString();
    }



    public String logoutCall(String authToken) throws Exception {
        return makeRequest(loginURI.toURL(), authToken, "DELETE");
    }

    public String registerCall(UserData registerData) throws Exception {
        return makePostRequest(registerURI.toURL(), registerData, null, false, "POST");
    }

    public String loginCall(UserData loginData) throws Exception {
        return makePostRequest(loginURI.toURL(), loginData, null, false, "POST");
    }

    public String joinGameCall(JoinGameData gameData, String authToken) throws Exception {
        return makePostRequest(gameURI.toURL(), gameData, authToken, true, "PUT");
    }

    public String listGamesCall(String authToken) throws Exception {
        return makeRequest(gameURI.toURL(), authToken, "GET");
    }

    public String createGameCall(GameData gameData, String authToken) throws Exception {
        return makePostRequest(gameURI.toURL(), gameData, authToken, true, "POST");
    }

    public String observeGameCall() throws Exception {
        return null;
    }





}
