package Controller;

import Request.Request;
import Request.Partial;

/**
 * Work in progress
 * Parse strings into requests to be executed. Parsing checks for errors
 * in invalid format.
 *
 * @author Michael Kha
 */
public class RequestParser{

    private static final String DELIMITER = ",";
    private static final String TERMINATOR = ";";

    private String partial;

    public RequestParser() {
        partial = "";
    }

    public String processRequest(String request) {
        Request command = determineRequest(request);
        return command.execute();
    }

    public Request determineRequest(String request) {
        // Check if partial request first
        if (!request.endsWith(TERMINATOR) || !partial.isEmpty()) {
            partial += request;
            if (request.endsWith(TERMINATOR)) {
                request = partial;
                partial = "";
                return helpCreateRequest(request);
            }
            // return partial request
            return new Partial();
        }
        else {
            return helpCreateRequest(request);
        }
    }

    private Request helpCreateRequest(String request) {
        // Break request into a command and its parameters
        String[] parts = request.split(DELIMITER, 2);
        if (parts.length != 2) {
            // TODO: throw exception
        }
        // Determine which request
        return createRequest(parts[0], parts[1]);
    }

    /**
     *
     * @param command
     * @param params
     * @return
     */
    private Request createRequest(String command, String params) {
        switch (command) {
            case "register":
                break;
            case "arrive":
                break;
            case "depart":
                break;
            case "info":
                break;
            case "borrow":
                break;
            case "borrowed":
                break;
            case "return":
                break;
            case "pay":
                break;
            case "search":
                break;
            case "buy":
                break;
            case "advance":
                break;
            case "datetime":
                break;
            case "report":
                break;
            default:
                break;
        }
        return null;
    }
}
