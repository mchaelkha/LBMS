package Controller.Request;

import Model.Checkout.CheckoutDB;
import Model.Visitor.VisitorDB;

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
    //TODO comment
    private CheckoutDB checkoutDB;
    private VisitorDB visitorDB;
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
     * TODO finish comment
     * @param params The parameters that follow a request command
     */
    public PayFine(CheckoutDB checkoutDB, VisitorDB visitorDB, String params) {
        this.checkoutDB = checkoutDB;
        this.visitorDB = visitorDB;
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

        //Check visitor ID corresponds to a registered visitor
        if(!visitorDB.validRegisteredVisitor(visitorID)){
            return PAY_REQUEST+DELIMITER+INVALID_VISITOR_ID+TERMINATOR;
        }
        else{
            //Get visitor's balance
            int balance = checkoutDB.calculateFine(visitorID);
            //Check for invalid amount
            if (amount < 0 || amount > balance) {
                return PAY_REQUEST+DELIMITER+INVALID_AMOUNT+DELIMITER+amount+balance+TERMINATOR;
            }
            else{
                double remainingBalance = checkoutDB.payFine(visitorID, amount);
                return PAY_REQUEST+DELIMITER+SUCCESS+DELIMITER+String.format("$%.02f", remainingBalance)+TERMINATOR;
            }
        }
    }
}
