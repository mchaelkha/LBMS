package Controller.Request;

import Model.Checkout.CheckoutDB;
import Model.Client.AccountDB;
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
    /**
     * The checkout database
     */
    private CheckoutDB checkoutDB;
    /**
     * The visitor database
     */
    private VisitorDB visitorDB;
    /**
     * The client that made this request
     */
    private String clientID;
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
     * @param clientID The client making the request
     * @param params The parameters that follow a request command
     */
    public PayFine(String clientID, String params) {
        this.checkoutDB = CheckoutDB.getInstance();
        this.visitorDB = VisitorDB.getInstance();
        this.clientID = clientID;
        this.params = params;
    }

    /**
     * Check the parameters to validate what the request is.
     * @return If the parameters are correct
     */
    @Override
    public boolean checkParams() {
        String[] parts = params.split(",");
        if (parts.length > 0) {
            if (parts[0].length() == 10) {
                visitorID = parts[0];
                amount = Integer.parseInt(parts[1]);
            }
            else {
                AccountDB accountDB = AccountDB.getInstance();
                visitorID = accountDB.getVisitorIDFromClientID(clientID);
                amount = Integer.parseInt(parts[0]);
            }
            return true;
        }
        return false;
    }

    /**
     * Executes the pay fine command to pay a visitor's fines.
     * @return String indicating that the execution has succeeded.
     */
    @Override
    public String execute() {
        if (!checkParams()) {
            return clientID + DELIMITER + PARAM_MESSAGE;
        }
        String response = clientID + DELIMITER + PAY_REQUEST + DELIMITER;
        //Check visitor ID corresponds to a registered visitor
        if(!visitorDB.validRegisteredVisitor(visitorID)){
            return response+INVALID_VISITOR_ID+TERMINATOR;
        }
        else{
            //Get visitor's balance
            int balance = checkoutDB.calculateFine(visitorID);
            //Check for invalid amount
            if (amount < 0 || amount > balance) {
                return response+INVALID_AMOUNT+DELIMITER+amount+balance+TERMINATOR;
            }
            else{
                double remainingBalance = checkoutDB.payFine(visitorID, amount);
                return response+SUCCESS+DELIMITER+String.format("$%.02f", remainingBalance)+TERMINATOR;
            }
        }
    }
}
