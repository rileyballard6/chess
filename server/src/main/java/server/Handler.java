package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.GameService;
import service.UserService;
import spark.*;
import model.*;


public class Handler {
    private static final UserDAO userDAO = new UserDAO();
    private static final AuthDAO authDAO = new AuthDAO();
    private static final GameDAO gameDAO = new GameDAO();
    private static final UserService userService = new UserService(userDAO, authDAO);
    private static final GameService gameService = new GameService(userDAO, authDAO, gameDAO);

    //Register Handler
    public static Object RegisterHandler(Request req, Response res) {
        var body = getBody(req, model.UserData.class);
        //Service will throw an error if the user already exists or if an
        // input field is null, if not, returns AuthData
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
            if (e.getMessage().equals("Username Already Exists")) {
                    System.out.println(e);
                    res.status(400);
                    return "{ \"message\": \"Error: Bad Request\" }";
            } else {
                res.status(400);
                return "{ \"message\": \"Error: Bad Request\" }";
            }

        }

    }

    //Login handler for logging in and logging out
    public static Object LoginHandler(Request req, Response res) throws DataAccessException {
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
            String authToken = getAuthToken(req);
            //userService will return a boolean from the DataAccess layer confirming the authToken was deleted
            try {
                if (userService.logoutUser(authToken)) {
                    res.type("application/json");
                    res.status(200);
                    return "{}";
                } else {
                    res.status(401);
                    return "{ \"message\": \"Error: Auth Token not found\" }";
                }
            } catch (Error e) {
                res.status(500);
                return "{ \"message\": \"Error: Unauthorized\" }";
            }


        }

        return null;
    }

    public static Object GameHandler(Request req, Response res) throws DataAccessException {
        var body = getBody(req, model.GameData.class);
        String authToken = getAuthToken(req);

        try {
            int newGameId = gameService.createGame(body, authToken);
            res.type("application/json");
            res.status(200);
            return "{ \"gameID\": \"" + newGameId + "\" }";
        } catch (Exception e) {
            res.status(401);
            return "{ \"message\": \"Error: Unauthorized\" }";
        }

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

    private static String getAuthToken(Request request) {
        return request.headers("authorization");
    }
}
