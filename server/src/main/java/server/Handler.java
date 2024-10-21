package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.ClearDataService;
import service.GameService;
import service.UserService;
import spark.*;
import model.*;

import java.util.ArrayList;
import java.util.Objects;


public class Handler {
    private static final UserDAO userDAO = new UserDAO();
    private static final AuthDAO authDAO = new AuthDAO();
    private static final GameDAO gameDAO = new GameDAO();
    private static final UserService userService = new UserService(userDAO, authDAO);
    private static final GameService gameService = new GameService(authDAO, gameDAO);
    private static final ClearDataService clearService = new ClearDataService(userDAO, authDAO, gameDAO);

    //Service will throw an error if the user already exists or if an
    // input field is null, if not, returns AuthData
    public static Object RegisterHandler(Request req, Response res) {
        var body = getBody(req, model.UserData.class);

        try {
            AuthData newUserAuth = userService.registerNewUser(body);

            if (newUserAuth != null) {
                res.type("application/json");
                res.status(200);
                return new Gson().toJson(newUserAuth);
            } else {
                res.status(500);
                return "{ \"message\": \"Error: Unable to register\" }";
            }
        } catch (Exception e) {
            if (e.getMessage().equals("Username Already Exists")) {
                    res.status(403);
                    return "{ \"message\": \"Error: already taken\" }";
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
            return LoginHandlerPOST(req, res);
        } else if (method.equals("DELETE")) {
            return LoginHandlerDELETE(req, res);
        }

        return null;
    }

    //Service will throw error if user doesn't exist, else it checks for the password, after which will return AuthData
    //if correct, or 401 if wrong password
    public static Object LoginHandlerPOST(Request req, Response res) throws DataAccessException {
        var body = getBody(req, model.UserData.class);

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
    }

    //userService will return a boolean from the DataAccess layer confirming the authToken was deleted
    public static Object LoginHandlerDELETE(Request req, Response res) throws DataAccessException {
        String authToken = getAuthToken(req);

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

    public static Object GameHandler(Request req, Response res) throws DataAccessException {
        String authToken = getAuthToken(req);
        if (req.requestMethod().equals("GET")) {
            return GameHandlerGET(res, authToken);
        }

        if (req.requestMethod().equals("POST")) {
            var body = getBody(req, model.GameData.class);
            return GameHandlerPOST(res, authToken, body);
        } else if (req.requestMethod().equals("PUT")) {
            var body = getBody(req, model.JoinGameData.class);
            return GameHandlerPUT(res, authToken, body);
        }

        return null;
    }

    //Service checks authToken and retrieves the list of games, or throws an error if unauthorized
    public static Object GameHandlerGET(Response res, String authToken) throws DataAccessException {
        try {
            ArrayList<GameData> allGames = gameService.getAllGames(authToken);
            res.type("application/json");
            res.status(200);
            return "{ \"games\":" + new Gson().toJson(allGames) + "}";
        } catch (Exception e) {
            res.status(401);
            return "{ \"message\": \"Error: Unauthorized\" }";
        }
    }

    //Service checks authToken and creates a new game, or throws an error if unauthorized
    public static Object GameHandlerPOST(Response res, String authToken, GameData body) throws DataAccessException {
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

    //Service checks authToken and joins a game, or throws an error if unauthorized
    public static Object GameHandlerPUT(Response res, String authToken, JoinGameData body ) throws DataAccessException {
        try {
            boolean gameJoined = gameService.joinGame(body, authToken);
            res.type("application/json");
            if (gameJoined) {
                res.status(200);
                return "{}";
            } else {
                res.status(403);
                return "{ \"message\": \"Error: Already taken\" }";
            }

        } catch (Exception e) {
            if (Objects.equals(e.getMessage(), "Unauthorized")) {
                res.status(401);
                return "{ \"message\": \"Error: Unauthorized\" }";
            } else {
                res.status(400);
                return "{ \"message\": \"Error: Bad request\" }";
            }

        }
    }

    //Calls Service delete, which tells all DAO to clear their data structures
    public static Object DeleteHandler(Request req, Response res) {
        try {
            if (clearService.clearAllData()) {
                return "{}";
            } else {
                res.status(500);
                return "{ \"message\": \"Error: Database error\" }";
            }
        } catch (Error e) {
            res.status(500);
            return "{ \"message\": \"Error: Database error\" }";
        }

    }


    //Helper functions for getting data from body and header of request
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
