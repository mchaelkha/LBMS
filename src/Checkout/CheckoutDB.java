package Checkout;

import Book.BookDB;
import Visitor.VisitorDB;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
     * @param checkoutDate the date and time of the transaction 
     * @param visitorID The visitor ID
     * @param isbn The book's ISBN
     * @return The new transaction if it was valid, otherwise null.
     */
    public Transaction checkout(LocalDateTime checkoutDate, String visitorID, String isbn) {
        Transaction transaction = new Transaction(checkoutDate, isbn);
        if(visitorDB.checkoutBook(visitorID)) {
            if(!this.openLoans.containsKey(visitorID)) {
                this.openLoans.put(visitorID, new ArrayList<Transaction>());
            }
            this.openLoans.get(visitorID).add(transaction);
            return transaction;
        } else {
            return null;
        }
    }

    /**
     * Calculate the fines accrued by a visitor.
     * @param visitorID The visitor ID to check for
     * @return The fine amount under a visitor ID
     */
    public int calculateFine(String visitorID) {
        int fines = 0;
        if(this.openLoans.containsKey(visitorID)) {
            for (Transaction t : this.openLoans.get(visitorID)) {
                fines += t.fineAmount;
            }
        }

        if(this.closedLoans.containsKey(visitorID)) {
            for (Transaction t : this.closedLoans.get(visitorID)) {
                fines += t.fineAmount;
            }
        }

        return fines;
    }

    /**
     * Calculate the fines accrued by all visitors.
     * @return The total fine amount
     */
    public int calculateTotalFines() {
        int fines = 0;
        for (List<Transaction> transactionList : openLoans.values()) {
            for (Transaction t : transactionList) {
                fines += t.fineAmount;
            }
        }

        for (List<Transaction> transactionList : closedLoans.values()) {
            for (Transaction t : transactionList) {
                fines += t.fineAmount;
            }
        }

        return fines;
    }

    /**
     * Return to the Library a given book that the given visitor has checked out by isbn.
     * Returns the completed Transaction if successful.
     * @param returnDate the date and time of the transaction
     * @param visitorID The visitor ID to return books for
     * @param isbn the isbn of the book the visitor has checked out.
     * @return the completed Transaction if successful, null otherwise.
     */
    public Transaction returnBook(LocalDateTime returnDate, String visitorID, String isbn) {
        if(this.openLoans.containsKey(visitorID)) {
            for (Transaction t: this.openLoans.get(visitorID)) {
                if(t.getIsbn().equals(isbn)) {
                    t.returnBook(returnDate);
                    this.openLoans.get(visitorID).remove(t);

                    if(!this.closedLoans.containsKey(visitorID)) {
                        this.closedLoans.put(visitorID, new ArrayList<Transaction>());
                    }
                    this.closedLoans.get(visitorID).add(t);

                    return t;
                }
            }
        }
        return null;
    }

}
