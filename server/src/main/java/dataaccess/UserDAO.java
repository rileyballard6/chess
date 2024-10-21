package dataaccess;
import model.*;
import java.util.ArrayList;
import java.util.Objects;


public class UserDAO {
    private final ArrayList<UserData> users = new ArrayList<>();

    private void createUser(UserData userData) {
        this.users.add(userData);
        System.out.println(userData.username());
    }

    private UserData getUser(String username) {
        UserData user = null;
        for (int i = 0; i < this.users.size(); i++) {
            UserData currentUser = this.users.get(i);
            if (Objects.equals(currentUser.username(), username)) {
                user = currentUser;
                break;
            }
        }
        return user;
    }

}
