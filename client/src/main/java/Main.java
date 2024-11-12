import chess.*;
import clients.Repl;

public class Main {

    static final Repl repl = new Repl();

    public static void main(String[] args) {
        System.out.println("♕ Welcome to CS 240 Chess, type 'help' to get started");
        repl.run();
    }
}