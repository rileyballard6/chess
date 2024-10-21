package server;

import com.google.gson.Gson;
import service.RegisterService;
import spark.*;
import model.*;


public class Handler {
    private static final RegisterService registerService = new RegisterService();

    public static Object RegisterHandler(Request req, Response res) {
        var body = getBody(req, model.UserData.class);
        try {
            UserData newUser = registerService.registerNewUser(body);

            if (newUser != null) {
                res.type("application/json");
                res.status(200);
                return new Gson().toJson(newUser);
            } else {
                res.status(500);
                return "Error: Unable to register user";
            }
        } catch (Exception e) {
            res.status(500);
            return "Server error: " + e.getMessage();
        }

    }

    public static Object LoginHandler(Request req, Response res) {
        var body = getBody(req, model.UserData.class);
        System.out.println("Login request received");
        res.type("application/json");
        return new Gson().toJson(body);
    }

    public static Object GameHandler(Request req, Response res) {
        var body = getBody(req, model.GameData.class);
        System.out.println("GameHandler request received");
        res.type("application/json");
        return new Gson().toJson(body);
    }


    public static Object DeleteHandler(Request req, Response res) {
        var body = getBody(req, model.UserData.class);
        System.out.println("Delete request received");
        res.type("application/json");
        return new Gson().toJson(body);
    }



    private static <T> T getBody(Request request, Class<T> clazz) {
        var body = new Gson().fromJson(request.body(), clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }
}
