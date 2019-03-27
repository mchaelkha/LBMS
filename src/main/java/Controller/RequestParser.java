package Controller;

import Controller.Request.*;
import Model.Book.BookDB;
import Model.Checkout.CheckoutDB;
import Model.Client.AccountDB;
import Model.Library.LibrarySystem;
import Model.Library.ReportGenerator;
import Model.Library.TimeKeeper;
import Model.Visitor.VisitorDB;

import java.util.HashMap;
import java.util.Map;

/**
 * Parse strings into requests to be executed. Parsing checks for errors
 * in invalid format.
 *
 * @author Michael Kha
 */
public class RequestParser implements Parser {

    /**
     * Clients to their possible partial requests. Clients do not have a
     * partial request if the string is empty.
     */
    private Map<String, String> partialRequests;

    /**
     * Library system to keep track of library state and system databases
     */
    private LibrarySystem librarySystem;

    private TimeKeeper timeKeeper;

    private ReportGenerator reportGenerator;

    /**
     * Creates a new RequestParser
     * @param librarySystem The LibrarySystem containing the visitor, checkout, and book databases.
     */
    public RequestParser(LibrarySystem librarySystem, TimeKeeper timeKeeper,
                         ReportGenerator reportGenerator) {
        partialRequests = new HashMap<>();
        this.librarySystem = librarySystem;
        this.timeKeeper = timeKeeper;
        this.reportGenerator = reportGenerator;
    }

    /**
     * Processes a given request and returns the result of the command.
     * @param request A string containing the request (partial or complete)
     * @return the result of the command which was executed.
     */
    @Override
    public Request processRequest(String request) {
        return determineRequest(request);
    }

    /**
     * Checks if the client ID has a partial request stored in the parser.
     * @param clientID The client to check for
     * @return If a partial request exists.
     */
    private boolean hasPartial(String clientID) {
        if (partialRequests.containsKey(clientID)) {
            String partial = partialRequests.get(clientID);
            return !partial.isEmpty();
        }
        return false;
    }

    /**
     * Determines if a request is unfinished or not.
     * @param request The request to be parsed.
     * @return a partial request, or a call to create the request depending on whether the request was complete.
     */
    private Request determineRequest(String request) {
        String[] parts = request.split(DELIMITER, 2);
        String clientID = parts[0];
        String req = parts[1];
        // Check if partial request first
        if (!req.endsWith(TERMINATOR) || hasPartial(clientID)) {
            String partial = partialRequests.get(clientID);
            // Make null value of map always empty string
            if (partial == null) {
                partial = "";
            }
            partial += req;
            if (req.endsWith(TERMINATOR)) {
                partialRequests.put(clientID, "");
                return helpCreateRequest(clientID, partial);
            }
            partialRequests.put(clientID, partial);
            // return partial request
            return new Partial();
        }
        else {
            return helpCreateRequest(clientID, req);
        }
    }

    /**
     * Splits the request into the command and its parameters, and calls createRequest with the parts.
     * @param clientID The client ID to be used by requests
     * @param request the request to be split.
     * @return a new request based on the command and parameters given.
     */
    private Request helpCreateRequest(String clientID, String request) {
        // Break request into a command and its parameters
        String[] parts = request.split(DELIMITER, 2);
        return createRequest(clientID, parts);
    }

    /**
     * Creates, executes, and returns the request, given a command, and parameters.
     * @param clientID The client ID to be used by requests
     * @param parts The parts of the request, a command and possibly its parameters
     * @return The request that was executed.
     */
    private Request createRequest(String clientID, String[] parts) {
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
                request = new RegisterVisitor(timeKeeper, clientID, params);
                break;
            case ARRIVE_REQUEST:
                request = new BeginVisit(librarySystem, clientID, params);
                break;
            case DEPART_REQUEST:
                request = new EndVisit(timeKeeper, clientID, params);
                break;
            case INFO_REQUEST:
                request = new LibraryBookSearch(clientID, params);
                break;
            case BORROW_REQUEST:
                request = new BorrowBook(librarySystem, clientID, params);
                break;
            case BORROWED_REQUEST:
                request = new FindBorrowedBooks(clientID, params);
                break;
            case RETURN_REQUEST:
                request = new ReturnBook(timeKeeper, clientID, params);
                break;
            case PAY_REQUEST:
                request = new PayFine(clientID, params);
                break;
            case SEARCH_REQUEST:
                request = new BookStoreSearch(clientID, params);
                break;
            case BUY_REQUEST:
                request = new BookPurchase(clientID, params);
                break;
            case ADVANCE_REQUEST:
                request = new AdvanceTime(reportGenerator, timeKeeper, clientID, params);
                break;
            case DATE_TIME_REQUEST:
                request = new CurrentDateTime(timeKeeper, clientID);
                break;
            case REPORT_REQUEST:
                request = new LibraryStatisticsReport(reportGenerator, clientID, params);
                break;
            case CREATE_REQUEST:
                request = new CreateAccount(clientID, params);
                break;
            case LOGIN_REQUEST:
                request = null;
                break;
            case LOGOUT_REQUEST:
                request = null;
                break;
            case SERVICE_REQUEST:
                request = new SetBookInfoService(clientID, params);
                break;
            case UNDO_REQUEST:
                request = new Undo(clientID);
                break;
            case REDO_REQUEST:
                request = new Redo(clientID);
                break;
            default:
                request = new Illegal();
                break;
        }
        return request;
    }
}
