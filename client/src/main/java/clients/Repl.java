package clients;

import model.AuthData;

import java.net.URISyntaxException;
import java.util.Scanner;

public class Repl {
    private final Scanner scanner = new Scanner(System.in);
    private boolean loggedIn = false;
    private boolean running = true;

    final private preLoginClient preLoginClient = new preLoginClient();
    final private postLoginClient postLoginClient = new postLoginClient();

    AuthData userAuth = null;

    public Repl() throws URISyntaxException {
    }

    public void run() throws Exception {
        while (running) {
            String command = readInput();
            if (!loggedIn) {
                evaluateInput(command);
            } else {
                evaluateInputAuthenticated(command);
            }
        }
    }

    private String readInput() {
        if (loggedIn) {
            System.out.print("\n[LOGGED_IN} >>> ");
        } else {
            System.out.print("\n[LOGGED_OUT] >>> ");
        }
        return scanner.nextLine().trim().toLowerCase();
    }

    private void evaluateInputAuthenticated(String input) throws Exception {
        return;
    }

    private void evaluateInput(String input) throws Exception {
        String[] inputArray = input.split(" ");
        switch (inputArray[0]) {
            case "register":
                Object registerResponse = preLoginClient.registerClient(inputArray);
                if (registerResponse == null) {
                    System.out.println("Unable to login user at this time.");
                } else if (registerResponse instanceof AuthData) {
                    AuthData authData = (AuthData) registerResponse;
                    System.out.println(authData.username() + " logged in.");
                    userAuth = authData;
                    loggedIn = true;
                } else if (registerResponse instanceof String) {
                    System.out.println((String) registerResponse);
                }
                break;

            case "login":
                Object loginResponse = preLoginClient.loginClient(inputArray);

                if (loginResponse == null) {
                    System.out.println("Unable to login user at this time.");
                } else if (loginResponse instanceof AuthData) {
                    AuthData authData = (AuthData) loginResponse;
                    System.out.println(authData.username() + " logged in.");
                    userAuth = authData;
                    loggedIn = true;
                } else if (loginResponse instanceof String) {
                    System.out.println((String) loginResponse);
                }
                break;
            case "help":
                printHelp();
                break;
            case "quit":
            case "exit":
                quit();
                break;
            case "":
                break;
            default:
                System.out.println("Unknown command. Type 'help' for available commands.");
                break;
        }
    }

    private void printHelp() {
        if (loggedIn) {
            System.out.println("\nAvailable options:");
            System.out.println("- create <NAME>            : create a chess game");
            System.out.println("- list                     : shows available games");
            System.out.println("- join <ID> [WHITE|BLACK]  : join a game");
            System.out.println("- observe <ID>             : watch a game");
            System.out.println("- logout                   : log out");
            System.out.println("- quit                     : quit playing chess");
            System.out.println("- help                     : view possible commands");
        } else {
            System.out.println("\nAvailable options:");
            System.out.println("- register <USERNAME> <PASSWORD> <EMAIL>  : create an account");
            System.out.println("- login <USERNAME> <PASSWORD>             : log in and play chess");
            System.out.println("- quit                                    : stop playing chess");
            System.out.println("- help                                    : view possible commands");
        }
    }

    private void quit() {
        System.out.println("Thanks for playing!");
        running = false;
    }
}