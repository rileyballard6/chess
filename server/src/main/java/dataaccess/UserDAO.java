package dataaccess;
import model.*;
import java.util.ArrayList;
import java.util.Objects;


public class UserDAO {
    private final ArrayList<UserData> users = new ArrayList<>();

    public UserData createUser(UserData userData) {
        this.users.add(userData);
        System.out.println(users);
        return userData;
    }

    public UserData getUser(String username) {
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

    public boolean userExists(String username) {
        for (int i = 0; i < this.users.size(); i++) {
            UserData currentUser = this.users.get(i);
            if (Objects.equals(currentUser.username(), username)) {
                return true;
            }
        }
        return false;
    }

    public boolean clearUsers() {
        this.users.clear();
        return true;
    }


}
