package Controller;

import Controller.Request.Request;
import Controller.Request.Simple;
import Model.Client.AccountDB;

import java.util.Set;

/**
 * Client parser for handling client data and verification before creating
 * an actual request in the request parser.
 * @author Michael Kha
 */
public class ClientParser implements Parser {

    private static int CLIENT_ID = 0;

    /**
     * List of clients that are connected
     */
    private Set<String> clients;

    /**
     * Parser to send a follow up process request to
     */
    private Parser parser;

    /**
     * Create a proxy parser with another parser.
     * @param parser Another parser to delegate further processing to
     */
    public ClientParser(Parser parser, Set<String> clients) {
        this.clients = clients;
        this.parser = parser;
        clients.add(String.valueOf(1000));
    }

    /**
     * Processes a given request by verifying a valid client ID is provided
     * or client operation is given.
     * @param request Request to process
     * @return A request provided by this proxy or subsequent parsers
     */
    @Override
    public Request processRequest(String request) {
        // Check if connect command
        if (request.equals(CONNECT_REQUEST + TERMINATOR)) {
            String id = connect();
            return new Simple(CONNECT_REQUEST + DELIMITER + id + TERMINATOR);
        }
        String[] parts = request.split(DELIMITER, 2);
        String id = parts[0];
        // Check for client ID
        if (!clients.contains(id)) {
            return new Simple("invalid-client-id" + TERMINATOR);
        }
        // Check if disconnect command
        if (parts[1].equals(DISCONNECT_REQUEST + TERMINATOR)) {
            disconnect(id);
            return new Simple(id + DELIMITER + DISCONNECT_REQUEST + TERMINATOR);
        }
        // Continue processing request in next parser
        return parser.processRequest(request);
    }

    /**
     * Connect to the server by registering a new client ID.
     * @return The newly created client ID
     */
    private String connect() {
        String id = String.valueOf(CLIENT_ID++);
        clients.add(id);
        return id;
    }

    /**
     * Disconnect the given client from the server.
     * @param clientID The client to remove
     */
    private void disconnect(String clientID) {
        AccountDB accountDB = AccountDB.getInstance();
        if (accountDB.isActiveAccount(clientID)) {
            accountDB.logOut(clientID);
        }
        clients.remove(clientID);
    }
}
