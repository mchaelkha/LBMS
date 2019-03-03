package Controller;

import Controller.Request.*;
import Model.Library.LibrarySystem;

/**
 * Parse strings into requests to be executed. Parsing checks for errors
 * in invalid format.
 *
 * @author Michael Kha
 */
public class RequestParser implements RequestUtil {
    /**
     * Library system to keep track of library state and system databases
     */
    private LibrarySystem librarySystem;

    /**
     * A partial request
     */
    private String partial;

    /**
     * Creates a new RequestParser
     * @param librarySystem The LibrarySystem containing the visitor, checkout, and book databases.
     */
    public RequestParser(LibrarySystem librarySystem) {
        this.librarySystem = librarySystem;
        partial = "";
    }

    /**
     * Processes a given request and returns the result of the command.
     * @param request A string containing the request (partial or complete)
     * @return the result of the command which was executed.
     */
    public String processRequest(String request) {
        Request command = determineRequest(request);
        return command.execute();
    }

    /**
     * Determines if a request is unfinished or not.
     * @param request The request to be parsed.
     * @return a partial request, or a call to create the request depending on whether the request was complete.
     */
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

    /**
     * Splits the request into the command and its parameters, and calls createRequest with the parts.
     * @param request the request to be split.
     * @return a new request based on the command and parameters given.
     */
    private Request helpCreateRequest(String request) {
        // Break request into a command and its parameters
        String[] parts = request.split(DELIMITER, 2);
        return createRequest(parts);
    }

    /**
     * Creates, executes, and returns the request, given a command, and parameters.
     * @param parts The parts of the request, a command and possibly its parameters
     * @return The request that was executed.
     */
    private Request createRequest(String[] parts) {
        Request request;
        String command = parts[0];
        String params = "";
        if (parts.length == 2) {
            params = parts[1];
            // Remove terminating character
            params = params.substring(0, params.length() - 1);
        } else {
            command = command.substring(0, command.length() - 1);
        }
        switch (command) {
            case REGISTER_REQUEST:
                request = new RegisterVisitor(librarySystem, params);
                break;
            case ARRIVE_REQUEST:
                request = new BeginVisit(librarySystem, params);
                break;
            case DEPART_REQUEST:
                request = new EndVisit(librarySystem, params);
                break;
            case INFO_REQUEST:
                request = new LibraryBookSearch(librarySystem, params);
                break;
            case BORROW_REQUEST:
                request = new BorrowBook(librarySystem, params);
                break;
            case BORROWED_REQUEST:
                request = new FindBorrowedBooks(librarySystem, params);
                break;
            case RETURN_REQUEST:
                request = new ReturnBook(librarySystem, params);
                break;
            case PAY_REQUEST:
                request = new PayFine(librarySystem, params);
                break;
            case SEARCH_REQUEST:
                request = new BookStoreSearch(librarySystem, params);
                break;
            case BUY_REQUEST:
                request = new BookPurchase(librarySystem, params);
                break;
            case ADVANCE_REQUEST:
                request = new AdvanceTime(librarySystem, params);
                break;
            case DATE_TIME_REQUEST:
                request = new CurrentDateTime(librarySystem);
                break;
            case REPORT_REQUEST:
                request = new LibraryStatisticsReport(librarySystem, params);
                break;
            default:
                request = new Illegal();
                break;
        }
        return request;
    }
}
