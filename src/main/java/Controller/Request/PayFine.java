package Controller.Request;

import Model.Checkout.CheckoutDB;
import Model.Checkout.Transaction;
import Model.Client.AccountDB;
import Model.Visitor.VisitorDB;

import java.util.List;

/**
 * Pay fine request to pay the fines a visitor has accumulated from borrowing
 * and returning overdue books.
 *
 * @author Michael Kha
 */
public class PayFine extends AccessibleRequest {
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
     * Params in the command
     */
    private String params;
    /**
     * The visitor ID
     */
    private String visitorID;
    /**
     * The amount the customer is paying
     */
    private double amount;
    /**
     * List of transactions that were modified in CheckOutDB
     */
    private List<Transaction> payableTransactions;

    /**
     * Create a new pay request given the library
     * and the parameters for the request.
     * @param clientID The client making the request
     * @param params The parameters that follow a request command
     */
    public PayFine(String clientID, String params) {
        super(clientID, false);
        this.checkoutDB = CheckoutDB.getInstance();
        this.visitorDB = VisitorDB.getInstance();
        this.params = params;
    }

    /**
     * Check the parameters to validate what the request is.
     * @return If the parameters are correct
     */
    @Override
    public boolean checkParams() {
        String[] parts = params.split(",");
        amount = Integer.parseInt(parts[0]);
        if (parts.length == 1) {
            AccountDB accountDB = AccountDB.getInstance();
            visitorID = accountDB.getVisitorIDFromClientID(clientID);
            return true;
        } else if (parts.length == 2) {
            visitorID = parts[1];
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
        return PAY_REQUEST;
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
            double balance = checkoutDB.calculateFine(visitorID);
            //Check for invalid amount
            if (amount <= 0 || amount > balance) {
                return response+INVALID_AMOUNT+DELIMITER+amount+balance+TERMINATOR;
            }
            else{
                //Store payableTransactions for undo
                payableTransactions = checkoutDB.getPayableTransactions(visitorID, amount);

                double remainingBalance = checkoutDB.payFine(visitorID, amount);

                addToCommandHistory(this,clientID);
                return response+SUCCESS+DELIMITER+String.format("$%.02f", remainingBalance)+TERMINATOR;
            }
        }
    }

    /**
     * Undo a fine payment
     */
    @Override
    public void undo(){
        checkoutDB.undoFine(visitorID, payableTransactions);
    }
}
