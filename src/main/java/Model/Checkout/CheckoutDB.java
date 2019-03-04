package Model.Checkout;

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
     * Transactions to be added to openLoans if all books being
     * borrowed by a visitor are valid
     */
    private List<Transaction> transactionsInProgress;
    /**
     * The max number of transactions a visitor can haave.
     */
    private final static int MAX_NUM_OF_TRANSACTIONS = 5;

    /**
     * Create a new checkout database that is empty
     */
    public CheckoutDB() {
        openLoans = new HashMap<>();
        closedLoans = new HashMap<>();
        transactionsInProgress = new ArrayList<>();
    }

    /**
     * Create a checkout transaction using a visitor ID and a book's ISBN.
     * @param checkoutDate the date and time of the transaction 
     * @param visitorID The visitor ID
     * @param bookIds Book isbns
     * @return The new transaction if it was valid, otherwise null.
     */
    public List<Transaction> checkout(LocalDateTime checkoutDate, String visitorID, List<String> bookIds) {
        transactionsInProgress.clear();
        for (String isbn : bookIds) {
            Transaction transaction = new Transaction(checkoutDate, isbn);
            transactionsInProgress.add(transaction);
        }
        addTransactionsInProcess(visitorID);
        return transactionsInProgress;
    }

    /**
     * Check if visitor currently has book borrowing limit
     * @param visitorID visitor borrowing
     * @return true if visitor has book borrowing limit
     */
    public boolean hasBookLimit(String visitorID) {
        List<Transaction> transactions = openLoans.get(visitorID);
        if (transactions == null) {
            return false;
        }
        return transactions.size()==MAX_NUM_OF_TRANSACTIONS;
    }

    /**
     * Check if borrowing the books in a BookBorrow command will reach the book limit
     * @param visitorID visitor doing the BookBorrow command
     * @param bookIds books being borrowed
     * @return true if adding these books will reach visitor book limit
     */
    public boolean willReachBookLimit(String visitorID, List<String> bookIds){
        List<Transaction> transactions = openLoans.get(visitorID);
        if (transactions == null) {
            return false;
        }
        return (transactions.size()+bookIds.size())>=MAX_NUM_OF_TRANSACTIONS;
    }

    /**
     * Used to add all the book transactions of a borrow book request
     * if all single book checkout requests were valid.
     */
    public void addTransactionsInProcess(String visitorID){
        //Visitor's first time making a transaction
        if(!openLoans.containsKey(visitorID)){
            openLoans.put(visitorID,this.transactionsInProgress);
        }
        else{
            openLoans.get(visitorID).addAll(transactionsInProgress);
        }
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
        List<Transaction> transactions = openLoans.get(visitorID);
        if (transactions == null) {
            return false;
        }
        for (Transaction transaction : transactions) {
            transaction.setFine();
            if(transaction.getFineAmount()>0){
                hasOutstandingFine = true;
            }
        }
        return hasOutstandingFine;
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
                    if(t.getFineAmount() == 0) {
                        this.openLoans.get(visitorID).remove(t);

                        //TODO remove transaction from visitorInfo transactions

                        if (!this.closedLoans.containsKey(visitorID)) {
                            this.closedLoans.put(visitorID, new ArrayList<Transaction>());
                        }
                        this.closedLoans.get(visitorID).add(t);
                    }

                    return t;
                }
            }
        }
        return null;
    }

}
