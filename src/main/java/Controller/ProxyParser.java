package Controller;

import Controller.Request.Illegal;
import Controller.Request.Request;
import Controller.Request.Simple;

import java.util.HashMap;
import java.util.Map;

/**
 * A proxy parser for handling client data and verification before creating
 * an actual request in the request parser.
 * @author Michael Kha
 */
public class ProxyParser implements Parser {

    private static int CLIENT_ID = 0;

    /**
     * List of clients that are connected
     */
    private Map<String, Client> clients;

    /**
     * Parser to send a follow up process request to
     */
    private Parser parser;

    /**
     * Create a proxy parser with another parser.
     * @param parser Another parser to delegate further processing to
     */
    public ProxyParser(Parser parser) {
        clients = new HashMap<>();
        this.parser = parser;
    }

    /**
     * Processes a given request by verifying a valid client ID is provided
     * or client operation is given.
     * @param request Request to process
     * @return A request provided by this proxy or subsequent parsers
     */
    @Override
    public Request processRequest(String request) {
        // No partial requests allowed yet
        if (!request.endsWith(TERMINATOR)) {
            return new Illegal();
        }
        // Check if connect command
        if (request.matches("^connect")) {
            String id = connect();
            return new Simple(CONNECT_REQUEST + DELIMITER + id + TERMINATOR);
        }
        String[] parts = request.split(",", 2);
        String id = parts[0];
        // Check for client ID
        if (!clients.containsKey(id)) {
            return new Simple("invalid-client-id" + TERMINATOR);
        }
        // Check if disconnect command

        // Continue processing request in next parser
        return parser.processRequest(request);
    }

    private String connect() {
        String id = String.valueOf(CLIENT_ID++);
        clients.put(id, new Client(id));
        return id;
    }

    private void disconnect(String clientID) {
        clients.remove(clientID);
    }
}
