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

    public String registerCall(UserData registerData) throws Exception {
        HttpURLConnection http = (HttpURLConnection) registerURI.toURL().openConnection();

        http.setRequestMethod("POST");
        http.setRequestProperty("Content-Type", "application/json; utf-8");
        http.setDoOutput(true);

        String jsonInputString = new Gson().toJson(registerData);

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

    public String loginCall() {
        return "";
    }




}
