package server;

import com.google.gson.Gson;
import spark.*;
import model.*;


public class Handler {
    public static Object RegisterHandler(Request req, Response res) {
        var body = getBody(req, model.UserData.class);
        res.type("application/json");
        return new Gson().toJson(body);
    }

    public Object LoginHandler(Request req, Response res) {
        return null;
    }

    public Object GameHandler(Request req, Response res) {
        return null;
    }



    private static <T> T getBody(Request request, Class<T> clazz) {
        var body = new Gson().fromJson(request.body(), clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }
}
