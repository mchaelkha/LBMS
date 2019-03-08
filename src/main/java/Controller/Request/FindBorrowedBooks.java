package Controller.Request;

import Model.Checkout.CheckoutDB;
import Model.Library.LibrarySystem;

/**
 * Find borrowed books request to check what books a visitor is borrowing.
 *
 * @author Michael Kha
 */
public class FindBorrowedBooks implements Request {
    /**
     * Message for missing parameters
     */
    private static final String PARAM_MESSAGE = String.format(MISSING_PARAM,
            ARRIVE_REQUEST) + DELIMITER + "visitor ID";
    /**
     * Checkout database used to find the borrowed books under a visitor
     */
    private CheckoutDB checkoutDB;
    /**
     * Params in the command
     */
    private String params;
    /**
     * The visitor ID to check
     */
    private String visitorID;

    /**
     * Create a new find borrowed books request given the visitor database
     * and the parameters for the request.
     * @param checkoutDB checkout database to find visitors borrowed books
     * @param params The parameters that follow a request command
     */
    public FindBorrowedBooks(CheckoutDB checkoutDB, String params) {
        this.checkoutDB = checkoutDB;
        this.params = params;
    }

    /**
     * Check the parameters to validate what the request is.
     * @return If the parameters are correct
     */
    @Override
    public String checkParams() {
        String[] parts = params.split(DELIMITER);
        if (parts.length == 1) {
            visitorID = parts[0];
            return PROPER_PARAM;
        }
        return PARAM_MESSAGE;
    }

    /**
     * Execute the find borrowed books command which returns a string.
     * @return String displaying the visitor's checked out books
     */
    @Override
    public String execute() {
        String check = checkParams();
        if (!check.equals(PROPER_PARAM)) {
            return check;
        }
        return checkoutDB.findBorrowedBooks(visitorID);
    }
}
