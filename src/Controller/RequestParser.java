package Controller;

import Request.RequestUtil;
import Book.BookDB;
import Checkout.CheckoutDB;
import Request.Request;
import Request.Partial;
import Visitor.VisitorDB;

/**
 * Work in progress
 * Parse strings into requests to be executed. Parsing checks for errors
 * in invalid format.
 *
 * @author Michael Kha
 */
public class RequestParser implements RequestUtil {
    /**
     * Database to keep track of books
     */
    private BookDB bookDB;
    /**
     * Database to keep track of visitors
     */
    private VisitorDB visitorDB;
    /**
     * Database to keep track of book checkouts by visitors
     */
    private CheckoutDB checkoutDB;

    /**
     * A partial request
     */
    private String partial;

    public RequestParser(BookDB bookDB, VisitorDB visitorDB, CheckoutDB checkoutDB) {
        this.bookDB = bookDB;
        this.visitorDB = visitorDB;
        this.checkoutDB = checkoutDB;
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
            case REGISTER_REQUEST:
                break;
            case ARRIVE_REQUEST:
                break;
            case DEPART_REQUEST:
                break;
            case INFO_REQUEST:
                break;
            case BORROW_REQUEST:
                break;
            case BORROWED_REQUEST:
                break;
            case RETURN_REQUEST:
                break;
            case PAY_REQUEST:
                break;
            case SEARCH_REQUEST:
                break;
            case BUY_REQUEST:
                break;
            case ADVANCE_REQUEST:
                break;
            case DATE_TIME_REQUEST:
                break;
            case REPORT_REQUEST:
                break;
            default:
                break;
        }
        return null;
    }
}
