package clients;

import model.UserData;

public class preLoginClient {

    public String registerClient(String[] stringArray) {
        if (stringArray.length != 4) {
            return "Incorrect amount of args. Please try again";
        }

        UserData userData = new UserData(stringArray[1], stringArray[2], stringArray[3]);


        return userData.username() + " logged in";
    }

    public String loginClient(String[] stringArray) {
        if (stringArray.length != 3) {
            return "Incorrect amount of args. Please try again";
        }

        UserData userData = new UserData(stringArray[1], stringArray[2], null);

        return userData.username() + " logged in";
    }
}
