package clients;

import java.util.Scanner;

public class Repl {
    private final Scanner scanner = new Scanner(System.in);
    private boolean loggedIn = false;
    private boolean running = true;

    final private preLoginClient preLoginClient = new preLoginClient();
    final private postLoginClient postLoginClient = new postLoginClient();

    public void run() {
        while (running) {
            String command = readInput();
            evaluateInput(command);
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

    private void evaluateInput(String input) {
        String[] inputArray = input.split(" ");
        switch (inputArray[0]) {
            case "register":
                String response = preLoginClient.registerClient(inputArray);
                System.out.println(response);
                loggedIn = true;
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