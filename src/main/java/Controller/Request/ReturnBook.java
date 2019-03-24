package Controller.Request;

import Model.Book.BookDB;
import Model.Checkout.CheckoutDB;
import Model.Library.TimeKeeper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Return book request to return the specified books of a visitor.
 *
 * @author Michael Kha
 */
public class ReturnBook implements Request {
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

    /**
     * Create a new return book request given the checkout database
     * and the parameters for the request.
     * @param checkoutDB The checkout database
     * @param bookDB The book database
     * @param params The parameters that follow a request command
     */
    public ReturnBook(CheckoutDB checkoutDB, BookDB bookDB, TimeKeeper timeKeeper, String params) {
        this.checkoutDB = checkoutDB;
        this.bookDB = bookDB;
        this.timeKeeper = timeKeeper;
        this.params = params;
    }

    /**
     * Check the parameters to validate that the request is
     * @return If the parameters are correct
     */
    @Override
    public boolean checkParams() {
        String[] parts = params.split(DELIMITER);
        if (parts.length > 1) {
            visitorID = parts[0];
            bookIDs = new ArrayList<>();
            bookIDs.addAll(Arrays.asList(parts).subList(1, parts.length));
            return true;
        }
        return false;
    }

    /**
     * Execute the return book command which returns a string.
     * @return String indicating that the book has been returned successfully
     *         and possibly fines for books.
     */
    @Override
    public String execute() {
        if (!checkParams()) {
            return PARAM_MESSAGE;
        }
        return checkoutDB.returnBooks(visitorID, bookIDs, bookDB, timeKeeper);
    }
}
