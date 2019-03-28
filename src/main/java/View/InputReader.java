package View;

import Controller.Parser;
import Controller.Request.Request;
import Controller.Request.RequestUtil;

import java.util.Scanner;

/**
 * Read input from a view implementation and pass to an appropriate parser
 * controller for interpretation.
 */
public class InputReader implements RequestUtil {

    private static InputReader instance;

    /**
     * Command to shutdown the program by first saving
     */
    private static final String SHUTDOWN = "/shutdown";
    /**
     * Command to stop the program immediately
     */
    private static final String EXIT = "/exit";

    /**
     * The server to send system requests to
     */
    private LBServer server;
    /**
     * The parser to send requests to
     */
    private Parser parser;

    /**
     * Create an input reader that works with the server and parser to
     * interpret user input.
     * @param server The server to receive system requests
     * @param parser The parser to pass requests to
     */
    private InputReader(LBServer server, Parser parser) {
        this.server = server;
        this.parser = parser;
    }

    public static InputReader getInstance() {
        if (instance == null) {
            return null;
        }
        return instance;
    }

    public static InputReader init(LBServer server, Parser parser) {
        if (instance == null) {
            instance = new InputReader(server, parser);
        }
        return instance;
    }

    /**
     * Start reading input until user enters an exit or shutdown request.
     * Special commands:
     * 1. /shutdown FILE - Shutdowns the program by first saving
     * 2. /exit - Exit the program without saving
     * 3. Client connections: connect and disconnect
     * 4. requests in csv format - commands to run through the parser
     * @param next The next line of input
     * @return If the program should continue running
     */
    public Request read(String next) {
        String[] parts;
        // Check for special commands
        if (next.matches("^" + SHUTDOWN + "\\s[\\w].*")) {
            parts = next.split(" ");
            System.out.println("Saving system state...");
            server.shutdown(parts[1]);
            System.out.println("Shutdown complete. Now exiting...");
            server.exit();
            return null;
        }
        if (next.matches("^" + EXIT)) {
            System.out.println("Now exiting...");
            server.exit();
            return null;
        }
        // Next line must be a request to be processed
        Request request = parser.processRequest(next);
        // TODO: make accounts execute
        // TODO: make requests with optional visitorID param use clientID instead to find account/visitor
        // TODO: Add performed commands only if valid to stacks in accounts
        System.out.println(request.execute());
        return request;
    }

}
