package Controller;

import Model.Library.ReportGenerator;
import Model.Library.TimeKeeper;
import Controller.Request.*;
import Model.Book.BookDB;
import Model.Checkout.CheckoutDB;
import Model.Visitor.VisitorDB;

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
     * Keeps time consistent within the system.
     */
    private TimeKeeper timeKeeper;
    /**
     * Report Generator.
     */
    private ReportGenerator reporter;

    /**
     * A partial request
     */
    private String partial;

    /**
     * Creates a new RequestParser
     * @param bookDB The system's database of Books
     * @param visitorDB The system's database of visitors
     * @param checkoutDB The system's database of transactions (checkout, purchase, return)
     */
    public RequestParser(BookDB bookDB, VisitorDB visitorDB, CheckoutDB checkoutDB) {
        this.bookDB = bookDB;
        this.visitorDB = visitorDB;
        this.checkoutDB = checkoutDB;
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
        if (parts.length != 2) {
            // TODO: throw exception
        }
        // Determine which request
        return createRequest(parts[0], parts[1]);
    }

    /**
     * Creates, executes, and returns the request, given a command, and parameters.
     * @param command The request to execute
     * @param params The parameters for the command.
     * @return The request that was executed.
     */
    private Request createRequest(String command, String params) {
        Request request = null;
        // Remove terminating character
        params = params.substring(0, params.length() - 1);
        switch (command) {
            case REGISTER_REQUEST:
                request = new RegisterVisitor(visitorDB, params);
                break;
            case ARRIVE_REQUEST:
                request = new BeginVisit(visitorDB, params);
                break;
            case DEPART_REQUEST:
                request = new EndVisit(visitorDB, params);
                break;
            case INFO_REQUEST:
                request = new LibraryBookSearch(bookDB, params);
                break;
            case BORROW_REQUEST:
                request = new BorrowBook(checkoutDB, params);
                break;
            case BORROWED_REQUEST:
                request = new FindBorrowedBooks(visitorDB, params);
                break;
            case RETURN_REQUEST:
                request = new ReturnBook(checkoutDB, params);
                break;
            case PAY_REQUEST:
                request = new PayFine(checkoutDB, params);
                break;
            case SEARCH_REQUEST:
                request = new BookStoreSearch(bookDB, params);
                break;
            case BUY_REQUEST:
                request = new BookPurchase(bookDB, params);
                break;
            case ADVANCE_REQUEST:
                request = new AdvanceTime(params);
                break;
            case DATE_TIME_REQUEST:
                request = new CurrentDateTime();
                break;
            case REPORT_REQUEST:
                request = new LibraryStatisticsReport(reporter, params);
                break;
            default:
                request = new Illegal();
                break;
        }
        return request;
    }
}
