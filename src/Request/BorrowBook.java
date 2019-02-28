package Request;

import Checkout.CheckoutDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Borrow book request to allow visitors to checkout books.
 *
 * @author Michael Kha
 */
public class BorrowBook implements Request {

    /**
     * The checkout database of the library
     */
    private CheckoutDB checkoutDB;
    /**
     * Params in the command
     */
    private String params;
    /**
     * The visitor ID to borrow with
     */
    private String visitorID;
    /**
     * The list of book IDs to borrow
     */
    private List<String> bookIDs;

    /**
     * Create a new borrow book request given the book database and the
     * parameters for the request.
     * @param checkoutDB The checkout database
     * @param params The parameters that follow a request command
     */
    public BorrowBook(CheckoutDB checkoutDB, String params) {
        this.checkoutDB = checkoutDB;
        this.params = params;
    }

    /**
     * TODO: proper missing parameter checking
     * Check the parameters to validate that the request is
     * @return If the parameters are correct
     */
    @Override
    public String checkParams() {
        String[] parts = params.split(DELIMITER, 2);
        if (parts.length > 1) {
            visitorID = parts[0];
            bookIDs = new ArrayList<>();
            bookIDs.addAll(Arrays.asList(parts).subList(1, parts.length));
            return PROPER_PARAM;
        }
        return PARAM_COUNT;
    }

    /**
     * Execute the borrow book command which returns a string.
     * @return String indicating if book was borrowed by providing a due date
     *         or an error message for invalid input.
     */
    @Override
    public String execute() {
        return null;
    }
}
