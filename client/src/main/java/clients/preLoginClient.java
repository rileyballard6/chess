package clients;

import facade.ServerFacade;
import model.AuthData;
import model.UserData;
import com.google.gson.Gson;
import java.net.URISyntaxException;
import java.util.Map;

public class preLoginClient {

    ServerFacade serverFacade = new ServerFacade();

    public preLoginClient() throws URISyntaxException {
    }

    public Object registerClient(String[] stringArray) throws Exception {
        if (stringArray.length != 4) {
            return "Incorrect number of arguments.";
        }

        UserData userData = new UserData(stringArray[1], stringArray[2], stringArray[3]);

        try {
            String returnedData = serverFacade.registerCall(userData);
            Gson gson = new Gson();

            Map<String, Object> responseMap = gson.fromJson(returnedData, Map.class);

            if (responseMap.containsKey("authToken")) {
                return gson.fromJson(returnedData, AuthData.class);
            } else if (responseMap.containsKey("message")) {
                return responseMap.get("message");
            }
        } catch (Exception e) {
            return "An error occurred during registration";
        }
        return "Unknown error.";
    }

    public Object loginClient(String[] stringArray) {
        if (stringArray.length != 3) {
            return "Incorrect number of arguments.";
        }

        UserData userData = new UserData(stringArray[1], stringArray[2], null);

        try {
            String returnedData = serverFacade.loginCall(userData);
            Gson gson = new Gson();

            Map<String, Object> responseMap = gson.fromJson(returnedData, Map.class);

            if (responseMap.containsKey("authToken")) {
                return gson.fromJson(returnedData, AuthData.class);
            } else if (responseMap.containsKey("message")) {
                return responseMap.get("message");
            }
        } catch (Exception e) {
            return "Unable to log user in";
        }
        return "Unknown error.";
    }


}
