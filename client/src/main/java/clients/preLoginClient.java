package clients;

import facade.ServerFacade;
import model.AuthData;
import model.UserData;
import com.google.gson.Gson;
import java.net.URISyntaxException;

public class preLoginClient {

    ServerFacade serverFacade = new ServerFacade();

    public preLoginClient() throws URISyntaxException {
    }

    public String registerClient(String[] stringArray) throws Exception {
        if (stringArray.length != 4) {
            return "Incorrect amount of args. Please try again";
        }

        UserData userData = new UserData(stringArray[1], stringArray[2], stringArray[3]);

        try {
            String returnedData = serverFacade.registerCall(userData);
            Gson gson = new Gson();
            AuthData authData = gson.fromJson(returnedData, AuthData.class);
            return authData.username() + " logged in";
        } catch(Exception e) {
            return "Unable to complete request at this time.";
        }
    }

    public String loginClient(String[] stringArray) {
        if (stringArray.length != 3) {
            return "Incorrect amount of args. Please try again";
        }

        UserData userData = new UserData(stringArray[1], stringArray[2], null);

        return userData.username() + " logged in";
    }
}
