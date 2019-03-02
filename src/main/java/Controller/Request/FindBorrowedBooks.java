package Controller.Request;

import Model.Visitor.VisitorDB;

/**
 * Find borrowed books request to check what books a visitor is borrowing.
 *
 * @author Michael Kha
 */
public class FindBorrowedBooks implements Request {

    /**
     * The visitor database of the library
     */
    private VisitorDB visitorDB;
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
     * @param visitorDB
     * @param params
     */
    public FindBorrowedBooks(VisitorDB visitorDB, String params) {
        this.visitorDB = visitorDB;
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
        return PARAM_COUNT;
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
        // TODO: implement by calling right methods in DB
        return null;
    }
}
