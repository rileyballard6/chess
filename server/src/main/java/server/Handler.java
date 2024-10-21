package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import service.UserService;
import spark.*;
import model.*;


public class Handler {
    private static final UserDAO userDAO = new UserDAO();
    private static final AuthDAO authDAO = new AuthDAO();
    private static final UserService userService = new UserService(userDAO, authDAO);

    public static Object RegisterHandler(Request req, Response res) {
        var body = getBody(req, model.UserData.class);
        //Check if any UserData is missing, if so return bad request
        if (body.password() == null || body.email() == null || body.username() == null) {
            res.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        }
        //Service will throw an error if the user already exists, if not, returns AuthData
        try {
            AuthData newUserAuth = userService.registerNewUser(body);

            if (newUserAuth != null) {
                res.type("application/json");
                res.status(200);
                return new Gson().toJson(newUserAuth);
            } else {
                res.status(500);
                return "Error: Unable to register user";
            }
        } catch (Exception e) {
            res.status(403);
            return "{ \"message\": \"Error: already taken\" }";
        }

    }

    public static Object LoginHandler(Request req, Response res) {
        String method = req.requestMethod();

        if (method.equals("POST")) {
            //LOGIN
            var body = getBody(req, model.UserData.class);
            //Service will throw error if user doesn't exist, else it checks for the password, after which will return AuthData
            //if correct, or 401 if wrong password
            try {
                AuthData authInfo = userService.loginUser(body);

                if (authInfo != null) {
                    res.type("application/json");
                    res.status(200);
                    return new Gson().toJson(authInfo);
                } else {
                    res.status(401);
                    return "{ \"message\": \"Error: Unauthorized\" }";
                }
            } catch (Exception e) {
                res.status(401);
                return "{ \"message\": \"Error: User doesnt exist\" }";
            }
        } else if (method.equals("DELETE")) {
            //LOGOUT
            var body = getBody(req, model.UserData.class);
            System.out.println("Login request received");
            res.type("application/json");
            return new Gson().toJson(body);
        }

        return null;
    }

    public static Object GameHandler(Request req, Response res) {
        var body = getBody(req, model.GameData.class);
        System.out.println("GameHandler request received");
        res.type("application/json");
        return new Gson().toJson(body);
    }


    public static Object DeleteHandler(Request req, Response res) {
        System.out.println("Delete request received");
        return "{}";
    }



    private static <T> T getBody(Request request, Class<T> clazz) {
        var body = new Gson().fromJson(request.body(), clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }
}
