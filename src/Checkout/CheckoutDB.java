package Checkout;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class CheckoutDB {

    private Map<String, Transaction> openLoans;

    private Map<String, Transaction> closedLoans;

    public CheckoutDB() {
        openLoans = new HashMap<>();
        closedLoans = new HashMap<>();
    }

    public Transaction checkout(String visitorID, String book) {
        return null;
    }

    public int calculateFine(String book) {
        return 0;
    }

    public void returnBook(String visitorID) {

    }

}
