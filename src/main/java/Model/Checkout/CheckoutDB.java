package Model.Checkout;

import Controller.Request.RequestUtil;
import Model.Book.BookDB;
import Model.Book.BookInfo;
import Model.Library.TimeKeeper;

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
 * @author Luis Gutierrez
 */
public class CheckoutDB implements Serializable,RequestUtil {

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
     * Tracks the last find borrowed books for a visitor query
     */
    private Map<String,BookInfo> lastBorrowedBooks;
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
     * @param bookInfos The books to checkout
     * @return The new transaction if it was valid, otherwise null.
     */
    public List<Transaction> checkout(LocalDateTime checkoutDate, String visitorID, List<BookInfo> bookInfos) {
        transactionsInProgress.clear();
        for (BookInfo bookInfo : bookInfos) {
            Transaction transaction = new Transaction(checkoutDate, bookInfo);
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
     * @param amount amount of books
     * @return true if adding these books will reach visitor book limit
     */
    public boolean willReachBookLimit(String visitorID, int amount){
        List<Transaction> transactions = openLoans.get(visitorID);
        if (transactions == null) {
            return false;
        }
        return (transactions.size()+amount)>=MAX_NUM_OF_TRANSACTIONS;
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
        for (Transaction t : this.openLoans.get(visitorID)) {
            fines += t.getFineAmount();
        }

        return fines;
    }

    /**
     * Return the books from the list of book IDs from the most recent
     * borrowed books search.
     * @param visitorID visitor
     * @param bookIDs list of books to be returned
     * @return String whether returnBook command was successful
     */
    public String returnBooks(String visitorID, List<String> bookIDs, BookDB bookDB, TimeKeeper timeKeeper) {
        double totalFine = 0;
        List<String> overdue = new ArrayList<>();
        Transaction t;

        for(int i = 0; i < bookIDs.size(); i++){
            String id = bookIDs.get(i);
            BookInfo book = lastBorrowedBooks.get(id);
            if (book == null) {
                return RETURN_REQUEST + DELIMITER + INVALID_BOOK_ID +
                        DELIMITER + String.join(DELIMITER, bookIDs) + TERMINATOR;
            }
            String isbn = book.getIsbn();
            t = returnBook(timeKeeper.getClock(), visitorID, isbn);
            bookDB.returnCopy(isbn);
            if(t.getFineAmount() > 0){
                totalFine = totalFine + t.getFineAmount();
                overdue.add(id);
            }
        }
        if(overdue.size() > 0){
            return RETURN_REQUEST + DELIMITER + OVERDUE + DELIMITER +
                    String.format("$%.02f", totalFine) + DELIMITER +
                    String.join(DELIMITER, overdue) + TERMINATOR;
        }
        else {
            return RETURN_REQUEST + DELIMITER + SUCCESS + TERMINATOR;
        }
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

    /**
     * Find the borrowed books under a visitor by getting the visitor info.
     * @param visitorID The visitor to find the borrowed books
     * @return The string containing the books borrowed under the visitor
     */
    public String findBorrowedBooks(String visitorID){
        lastBorrowedBooks = new HashMap<>();
        List<Transaction> visitorTransactions = openLoans.get(visitorID);
        String response = BORROWED_REQUEST + DELIMITER + visitorTransactions.size() + DELIMITER;
        //For each transaction, call method in visitorDB get book title and add to response string
        int id = 0;
        for(Transaction transaction: visitorTransactions){
            response += NEW_LINE;
            String isbn = transaction.getIsbn();
            String checkoutDate = transaction.getCheckoutDate();
            String title = transaction.getTitle();
            response += id+DELIMITER+isbn+DELIMITER+title+DELIMITER+checkoutDate;
            lastBorrowedBooks.put(String.valueOf(id), transaction.getBookInfo());
            id++;
        }
        response += TERMINATOR;
        return response;
    }

    /**
     * Pays all or part of an outstanding fine
     * @param visitorID visitor's id
     * @param amount amount that visitor is paying
     * @return remaining balance of fines due for visitor
     */
    public int payFine(String visitorID, int amount){
        List<Transaction> transactions = openLoans.get(visitorID);
        for (Transaction transaction : transactions) {
            int fineAmount = transaction.getFineAmount();
            if (fineAmount > 0) {
                //Amount greater than transaction.fine -> clear fine and decrease amount
                if(amount > fineAmount){
                    amount -= fineAmount;
                    transaction.clearFine();
                }
                //Amount less than fine (or equal)-> clear amount and decrease fine
                else {
                    transaction.decreaseFineAmount(amount);
                    amount = 0;
                }
            }
        }
        return calculateFine(visitorID);
    }

}
