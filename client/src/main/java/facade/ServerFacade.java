package facade;


import com.google.gson.Gson;
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

    public String makePostRequest(URL url, Object data) throws Exception {
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setRequestProperty("Content-Type", "application/json; utf-8");
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

    public String makeDeleteRequest(URL url, Object data) throws Exception {
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("DELETE");
        http.setRequestProperty("Content-Type", "application/json; utf-8");
        http.setDoOutput(true);

        // If data is provided, convert it to JSON and write it to the output stream
        if (data != null) {
            String jsonInputString = new Gson().toJson(data);
            try (OutputStream os = http.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
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



    public String logoutCall() throws Exception {
        return "";
    }


    public String registerCall(UserData registerData) throws Exception {
        return makePostRequest(registerURI.toURL(), registerData);
    }

    public String loginCall(UserData loginData) throws Exception {
        return makePostRequest(loginURI.toURL(), loginData);
    }





}
