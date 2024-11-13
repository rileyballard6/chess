import chess.*;
import clients.Repl;

import java.net.URISyntaxException;

public class Main {

    static final Repl repl;

    static {
        try {
            repl = new Repl();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("♕ Welcome to CS 240 Chess, type 'help' to get started");
        repl.run();
    }
}