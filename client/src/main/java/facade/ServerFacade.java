package facade;


import com.google.gson.Gson;
import model.GameData;
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

    public String makePostRequest(URL url, Object data, String authToken, boolean needsAuth) throws Exception {
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
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

    public String makeGetRequest(URL url) throws Exception {
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("GET");
        http.setRequestProperty("Content-Type", "application/json; utf-8");

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

    public String makeDeleteRequest(URL url, String authToken) throws Exception {
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("DELETE");
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
        return makeDeleteRequest(loginURI.toURL(), authToken);
    }

    public String registerCall(UserData registerData) throws Exception {
        return makePostRequest(registerURI.toURL(), registerData, null, false);
    }

    public String loginCall(UserData loginData) throws Exception {
        return makePostRequest(loginURI.toURL(), loginData, null, false);
    }

    public String joinGameCall() throws Exception {
        return null;
    }

    public String listGamesCall() throws Exception {
        return null;
    }

    public String createGameCall(GameData gameData, String authToken) throws Exception {
        return makePostRequest(gameURI.toURL(), gameData, authToken, true);
    }

    public String observeGameCall() throws Exception {
        return null;
    }





}
