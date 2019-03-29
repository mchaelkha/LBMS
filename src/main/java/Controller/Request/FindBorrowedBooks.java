package Controller.Request;

import Model.Checkout.CheckoutDB;
import Model.Client.AccountDB;

/**
 * Find borrowed books request to check what books a visitor is borrowing.
 *
 * @author Michael Kha
 */
public class FindBorrowedBooks extends AccessibleRequest {
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
     * @param clientID The client making the request
     * @param params The parameters that follow a request command
     */
    public FindBorrowedBooks(String clientID, String params) {
        super(clientID, false);
        this.checkoutDB = CheckoutDB.getInstance();
        this.params = params;
    }

    /**
     * Check the parameters to validate what the request is.
     * @return If the parameters are correct
     */
    @Override
    public boolean checkParams() {
        String[] parts = params.split(DELIMITER);
        if (parts.length == 1) {
            if (parts[0].length() == 10) {
                visitorID = parts[0];
            }
            else {
                AccountDB accountDB = AccountDB.getInstance();
                visitorID = accountDB.getVisitorIDFromClientID(clientID);
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
        return BORROWED_REQUEST;
    }

    /**
     * Execute the find borrowed books command which returns a string.
     * @return String displaying the visitor's checked out books
     */
    @Override
    public String execute() {
        if (!checkParams()) {
            return clientID + DELIMITER + PARAM_MESSAGE;
        }
        String response = checkoutDB.findBorrowedBooks(visitorID);
        AccountDB accountDB = AccountDB.getInstance();
        accountDB.setBorrowedSearch(checkoutDB.getLastBorrowedBooks(), clientID);
        return clientID + DELIMITER + response;
    }
}
