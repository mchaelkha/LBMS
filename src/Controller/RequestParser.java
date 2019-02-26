package Controller;

import Request.*;
import Book.BookDB;
import Checkout.CheckoutDB;
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
     * Creates, executes, and returns the request, given a command, and parameters.
     * @param command The request to execute
     * @param params The parameters for the command.
     * @return The request that was executed.
     */
    private Request createRequest(String command, String params) {
        switch (command) {
            case REGISTER_REQUEST:
                RegisterVisitor reg = new RegisterVisitor();
                reg.execute();
                break;
            case ARRIVE_REQUEST:
                BeginVisit beg = new BeginVisit();
                beg.execute();
                break;
            case DEPART_REQUEST:
                EndVisit end = new EndVisit();
                end.execute();
                break;
            case INFO_REQUEST:
                LibraryStatisticsReport libstat = new LibraryStatisticsReport();
                libstat.execute();
                break;
            case BORROW_REQUEST:
                BorrowBook borrow = new BorrowBook();
                borrow.execute();
                break;
            case BORROWED_REQUEST:
                FindBorrowedBooks findb = new FindBorrowedBooks();
                findb.execute();
                break;
            case RETURN_REQUEST:
                ReturnBook rtn = new ReturnBook();
                rtn.execute();
                break;
            case PAY_REQUEST:
                PayFine pay = new PayFine();
                pay.execute();
                break;
            case SEARCH_REQUEST:
                LibraryBookSearch libsearch = new LibraryBookSearch();
                libsearch.execute();
                break;
            case BUY_REQUEST:
                BookPurchase purchase = new BookPurchase();
                purchase.execute();
                break;
            case ADVANCE_REQUEST:
                AdvanceTime adv = new AdvanceTime();
                adv.execute();
                break;
            case DATE_TIME_REQUEST:
                CurrentDateTime date = new CurrentDateTime();
                date.execute();
                break;
            case REPORT_REQUEST:
                break;
            default:
                break;
        }
        return null;
    }
}
