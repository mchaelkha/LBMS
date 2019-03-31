package Controller.Request;

import Model.Book.BookDB;
import Model.Book.BookInfo;
import Model.Checkout.CheckoutDB;
import Model.Client.AccountDB;
import Model.Library.TimeKeeper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Return book request to return the specified books of a visitor.
 *
 * @author Michael Kha
 */
public class ReturnBook extends AccessibleRequest {
    /**
     * Message for missing parameters
     */
    private static final String PARAM_MESSAGE = String.format(MISSING_PARAM,
            ARRIVE_REQUEST) + DELIMITER + "visitor ID,id[,ids]";
    /**
     * Checkout database used to .
     */
    private CheckoutDB checkoutDB;
    private BookDB bookDB;
    private TimeKeeper timeKeeper;
    /**
     * Params in the command
     */
    private String params;
    /**
     * The visitor ID to return the books for
     */
    private String visitorID;
    /**
     * List of books from their IDs of the most recent find borrowed
     * books search
     */
    private List<String> bookIDs;

    private Map<String, BookInfo> books;
    /**
     * Create a new return book request given the checkout database
     * and the parameters for the request.
     * @param clientID The client making the request
     * @param params The parameters that follow a request command
     */
    public ReturnBook(TimeKeeper timeKeeper, String clientID, String params) {
        super(clientID, false);
        this.checkoutDB = CheckoutDB.getInstance();
        this.bookDB = BookDB.getInstance();
        this.timeKeeper = TimeKeeper.getInstance();
        this.params = params;
    }

    /**
     * Check the parameters to validate that the request is
     * @return If the parameters are correct
     */
    @Override
    public boolean checkParams() {
        String[] parts = params.split(DELIMITER);
        if (parts.length > 0) {
            bookIDs = new ArrayList<>();
            if (parts[0].length() == 10) {
                visitorID = parts[0];
                if (parts.length == 1) {
                    return false;
                }
                bookIDs.addAll(Arrays.asList(parts).subList(1, parts.length));
            }
            else {
                AccountDB accountDB = AccountDB.getInstance();
                visitorID = accountDB.getVisitorIDFromClientID(clientID);
                bookIDs.addAll(Arrays.asList(parts));
            }
            return true;
        }
        return false;
    }

    /**
     * Get the name of the request
     * @return The name
     */
    @Override
    public String getName() {
        return RETURN_REQUEST;
    }

    public void undo(){
        checkoutDB.undoReturn(books, bookIDs, visitorID, bookDB);
    }
    /**
     * Execute the return book command which returns a string.
     * @return String indicating that the book has been returned successfully
     *         and possibly fines for books.
     */
    @Override
    public String execute() {
        if (!checkParams()) {
            return clientID + DELIMITER + PARAM_MESSAGE;
        }
        AccountDB accountDB = AccountDB.getInstance();
        Map<String, BookInfo> search = accountDB.getBorrowedSearch(clientID);
        books = search;
        if (search == null) {
            return clientID + DELIMITER + NOT_AUTHORIZED;
        }
        return clientID + DELIMITER + checkoutDB.returnBooks(search, visitorID,
                bookIDs, bookDB, timeKeeper);
    }
}
