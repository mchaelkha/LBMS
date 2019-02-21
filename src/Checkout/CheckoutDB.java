package Checkout;

import Book.BookDB;
import Visitor.VisitorDB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class CheckoutDB {

    /**
     * The open transaction loans of each visitor
     */
    private Map<String, List<Transaction>> openLoans;
    /**
     * The closed transaction loans of each visitor
     */
    private Map<String, List<Transaction>> closedLoans;
    /**
     * The visitor database
     */
    private VisitorDB visitorDB;
    /**
     * The book database
     */
    private BookDB bookDB;

    /**
     * Create a new checkout database that is empty
     */
    public CheckoutDB(VisitorDB visitorDB, BookDB bookDB) {
        this.visitorDB = visitorDB;
        this.bookDB = bookDB;
        openLoans = new HashMap<>();
        closedLoans = new HashMap<>();
    }

    /**
     * Create a checkout transaction using a visitor ID and a book's ISBN.
     * @param visitorID The visitor ID
     * @param book The book's ISBN
     * @return The new transaction
     */
    public Transaction checkout(String visitorID, String book) {
        return null;
    }

    /**
     * Calculate the fines accrued by a visitor.
     * @param visitorID The visitor ID to check for
     * @return The fine amount under a visitor ID
     */
    public int calculateFine(String visitorID) {
        return 0;
    }

    /**
     * Calculate the fines accrued by all visitors.
     * @return The total fine amount
     */
    public int calculateTotalFines() {
        return 0;
    }

    /**
     * Return the book of the last transaction.
     * @param visitorID The visitor ID to return books for
     */
    public void returnBook(String visitorID) {

    }

}
