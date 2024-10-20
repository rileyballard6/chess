import chess.*;
import com.google.gson.Gson;
import spark.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        // Specify the port you want the server to listen on
        Spark.port(8080);

        // Register a directory for hosting static files
        Spark.staticFiles.location("web");
        // Register handlers for each endpoint using the method reference syntax
    }
}