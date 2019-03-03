package Controller.Request;

import Model.Library.LibrarySystem;

/**
 * Pay fine request to pay the fines a visitor has accumulated from borrowing
 * and returning overdue books.
 *
 * @author Michael Kha
 */
public class PayFine implements Request {
    /**
     * Message for missing parameters
     */
    private static final String PARAM_MESSAGE = String.format(MISSING_PARAM,
            PAY_REQUEST) + DELIMITER + "visitor ID,amount";
    /**
     * The library
     */
    private LibrarySystem library;
    /**
     * Params in the command
     */
    private String params;
    /**
     * The visitor ID
     */
    private String visitorID;
    /**
     * The amount to pay back for the fines
     */
    private int amount;

    /**
     * Create a new pay request given the library
     * and the parameters for the request.
     * @param library The library
     * @param params The parameters that follow a request command
     */
    public PayFine(LibrarySystem library, String params) {
        this.library = library;
        this.params = params;
    }

    /**
     * Check the parameters to validate what the request is.
     * @return If the parameters are correct
     */
    @Override
    public String checkParams() {
        String[] parts = params.split(",");
        if (parts.length == 2) {
            visitorID = parts[0];
            amount = Integer.parseInt(parts[1]);
            return PROPER_PARAM;
        }
        return PARAM_MESSAGE;
    }

    /**
     * Executes the pay fine command to pay a visitor's fines.
     * @return String indicating that the execution has succeeded.
     */
    @Override
    public String execute() {
        String check = checkParams();
        if (!check.equals(PROPER_PARAM)) {
            return check;
        }
        return library.payFine(visitorID, amount);
    }
}
