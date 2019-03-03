package main.java.Model.Checkout;

import main.java.Model.Book.BookDB;
import main.java.Model.Visitor.VisitorDB;
import main.java.Model.Checkout.Transaction;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The database that manages all information about checkout and return 
 * transactions and fines performed by visitors to the library.
 * @author Hersh Nagpal
 */
public class CheckoutDB implements Serializable {

    /**
     * The open transaction loans of each visitor
     */
    private Map<String, List<Transaction>> openLoans;
    /**
     * The closed transaction loans of each visitor
     */
    private Map<String, List<Transaction>> closedLoans;
    /**
     * The max number of transactions a visitor can haave.
     */
    private final static int MAX_NUM_OF_TRANSACTIONS = 5;

    /**
     * Create a new checkout database that is empty
     */
    public CheckoutDB(VisitorDB visitorDB, BookDB bookDB) {
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
        if(!this.openLoans.containsKey(visitorID)) {
            this.openLoans.put(visitorID, new ArrayList<Transaction>());
        }
        this.openLoans.get(visitorID).add(transaction);
        return transaction;
    }

    /**
     * Check if visitor has an outstanding fine.
     * @param visitorID Visitor's ID
     * @return true if visitor has an outstanding fine, false otherwise
     */
    public boolean hasOutstandingFine(String visitorID) {
        //Update fines in visitor's transactions and check for outstanding fines
        boolean hasOutstandingFine = false;
        //Iterate through visitor's transactions and check if there is an outstanding fine
        for (Transaction transaction : openLoans.get(visitorID)) {
            transaction.setFine();
            if(transaction.getFineAmount()>0){
                hasOutstandingFine = true;
            }
        }
        return hasOutstandingFine;
    }


    public boolean hasBookLimit(String visitorID) {
        return openLoans.get(visitorID).size()==MAX_NUM_OF_TRANSACTIONS;
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
                fines += t.getFineAmount();
            }
        }

        if(this.closedLoans.containsKey(visitorID)) {
            for (Transaction t : this.closedLoans.get(visitorID)) {
                fines += t.getFineAmount();
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
                fines += t.getFineAmount();
            }
        }

        for (List<Transaction> transactionList : closedLoans.values()) {
            for (Transaction t : transactionList) {
                fines += t.getFineAmount();
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

                    //TODO remove transaction from visitorInfo transactions

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
