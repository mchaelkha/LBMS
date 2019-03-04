package Model.Library;
import Controller.Request.RequestUtil;
import Model.Book.BookDB;
import Model.Book.BookInfo;
import Model.Checkout.CheckoutDB;
import Model.Checkout.Transaction;
import Model.Visitor.VisitorDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The LibrarySystem acts as a receiver of commands and delegates
 * almost all functionality to other Database and helper classes.
 * @author Hersh Nagpal
 */
public class LibrarySystem implements RequestUtil{
    /**
     * String to map to the open state
     */
    private static final String OPEN_STATE = "LibraryOpen";
    /**
     * String to to map to the closed state
     */
    private static final String CLOSED_STATE = "LibraryClosed";

    /**
     * Collection of library states used during state transitions.
     */
    private Map<String,LibraryState> libraryStates;

    /**
     * Represents the current state of the library (closed or open).
     */
    private LibraryState currentLibraryState;

    /**
     * VisitorDataBase to help perform visitor requests dealing with visits
     */
    private VisitorDB visitorDB;
    /**
     * CheckoutDataBase to help perform visitor requests dealing with transactions
     */
    private CheckoutDB checkoutDB;
    /**
     * BookDataBase to help perform visitor requests dealing with library books
     */
    private BookDB bookDB;
    /**
     * Object used to update library time and to notify library when
     * to close and open (Library state transition)
     */
    private TimeKeeper timeKeeper;
    /**
     * Responsible for the creation of statistical reports
     */
    private ReportGenerator reporter;
    /**
     * Tracks the last book search made by visitor in order to complete borrow book command
     */
    private Map<String,BookInfo> lastBookSearch;

    /**
     * Create the library system that is responsible for knowing about the system's model.
     * @param visitorDB The visitor database
     * @param checkoutDB The checkout database
     * @param bookDB The book database
     * @param timeKeeper The time keeper
     * @param reporter The reporter to create reports
     */
    public LibrarySystem(VisitorDB visitorDB, CheckoutDB checkoutDB, BookDB bookDB,
                         TimeKeeper timeKeeper, ReportGenerator reporter) {
        this.visitorDB = visitorDB;
        this.checkoutDB = checkoutDB;
        this.bookDB = bookDB;
        this.timeKeeper = timeKeeper;
        this.reporter = reporter;
        //Add Library States
        libraryStates = new HashMap<>();
        libraryStates.put(CLOSED_STATE, new LibraryClosed());
        libraryStates.put(OPEN_STATE, new LibraryOpen(timeKeeper, checkoutDB, visitorDB, bookDB));
        currentLibraryState = libraryStates.get(OPEN_STATE);
    }

    /**
     * Gives the status of the library
     * @return Whether the library is open.
     */
    public boolean isOpen() {
        return timeKeeper.isLibraryOpen();
    }

    /**
     * Delegates registerVisitor command to VisitorDB
     * @param firstName First name
     * @param lastName Last name
     * @param address Address
     * @param phoneNumber Phone number
     * @return formatted string regarding the success of the registerVisitor command
     */
    public String registerVisitor(String firstName, String lastName, String address, String phoneNumber) {
        return visitorDB.registerVisitor(firstName, lastName, address, phoneNumber, timeKeeper.readDate());
    }

    /**
     * Delegates beginVisit visitor command to library concrete state
     * @param visitorID the visitor returning the book
     * @return formatted string regarding the success of the beginVisit command
     */
    public String beginVisit(String visitorID){
        return currentLibraryState.beginVisit(visitorID);
    }

    /**
     * Delegates endVisit visitor command to VisitorDB
     * @param visitorID the visitor ending their visit
     * @return formatted string regarding the success of the endVisit command
     */
    public String endVisit(String visitorID) {
        return visitorDB.endVisit(visitorID, timeKeeper.getClock(), timeKeeper.readTime());
    }

    /**
     * Delegates searching books to Book Database
     * @param title Book title
     * @param authors Book author
     * @param isbn Book isbn
     * @param publisher Book publisher
     * @param sort sort order (title, publish-date, book-status)
     * @return Map of (Isbns, BookInfos) representing a book search
     */
    public Map<String, BookInfo> searchBooks(String title, List<String> authors,
                                             String isbn, String publisher, String sort) {
        //Store last book search in library system for Borrow Book command
        lastBookSearch = bookDB.searchBooks(title, authors, isbn, publisher, sort);
        return lastBookSearch;
    }

    /**
     * Delegates checkoutBook visitor command to library concrete state.
     * @param visitorID the visitor borrowing a book
     * @param bookIds the books to be checked out
     * @return formatted string regarding the success of the command
     */
    public String checkoutBooks(String visitorID, List<String> bookIds) {
        //Check that isbns in Borrow Book request match the IDs in the most recent library book search
        for (String id : bookIds) {
            if (!lastBookSearch.containsKey(id)) {
                //Response = "borrow,invalid-book-id,{id};"
                String bookIdsString = String.join(",", bookIds);
                return BORROW_REQUEST+DELIMITER+INVALID_BOOK_ID+DELIMITER+bookIdsString+TERMINATOR;
            }
        }
        return currentLibraryState.checkoutBooks(timeKeeper.getClock(),visitorID, bookIds);
    }

    /**
     * Find the borrowed books under a visitor by getting the visitor info.
     * @param visitorID The visitor to find the borrowed books
     * @return The string containing the books borrowed under the visitor
     */
    public String findBorrowedBooks(String visitorID){
        List<Transaction> visitorTransactions = checkoutDB.findBorrowedBooks(visitorID);
        String response = "";
        //For each transaction, call method in visitorDB get book title and add to response string
        int id = 0;
        for(Transaction transaction: visitorTransactions){
            String isbn = transaction.getIsbn();
            String checkoutDate = transaction.getCheckoutDate();
            String title = bookDB.getTitle(isbn);
            response += id+DELIMITER+isbn+DELIMITER+title+DELIMITER+checkoutDate+NEW_LINE;
            id++;
        }
        response += ";";
        return response;
    }

    /**
     * @param visitorID visitor
     * @param bookIDs list of books to be returned
     * @return String whether returnBook command was successful
     */
    public String returnBooks(String visitorID, List<String> bookIDs) {
        double totalFine = 0;
        ArrayList<String> overdue = new ArrayList<>();
        Transaction t;

        for(int i = 0; i < bookIDs.size(); i++){
            t = checkoutDB.returnBook(timeKeeper.getClock(), visitorID, bookIDs.get(i));
            bookDB.returnCopy(bookIDs.get(i));
            if(t.getFineAmount() > 0){
                totalFine = totalFine + t.getFineAmount();
                overdue.add(bookIDs.get(i));
            }
        }
        if(overdue.size() > 0){
            return RETURN_REQUEST + DELIMITER + OVERDUE + DELIMITER + overdue.toString() + TERMINATOR;
        }
        else {
            return RETURN_REQUEST + DELIMITER + SUCCESS + TERMINATOR;
        }
    }

    /**
     * Pay the fines under a visitor by some amount.
     * @param visitorID The visitor to pay their fines for
     * @param amount The amount to add to
     * @return String indicating if successful or not and the new balance
     */
    public String payFine(String visitorID, int amount){
        //Check visitor ID corresponds to a registered visitor
        if(!visitorDB.validRegisteredVisitor(visitorID)){
            return PAY_REQUEST+DELIMITER+INVALID_VISITOR_ID+TERMINATOR;
        }
        else{
            //Get visitor's balance
            int balance = checkoutDB.calculateFine(visitorID);
            //Check for invalid amount
            if (amount < 0 || amount > balance) {
                return PAY_REQUEST+DELIMITER+INVALID_AMOUNT+DELIMITER+amount+balance;
            }
            else{
                int remainingBalance = checkoutDB.calculateFine(visitorID);
                return PAY_REQUEST+SUCCESS+remainingBalance;
            }
        }
    }

    /**
     * Search the book store for books with the given information.
     * @param title The title
     * @param authors The authors
     * @param isbn The isbn
     * @param publisher The publisher
     * @param sort The sort order
     * @return The mapping of hits to a unique ID
     */
    public Map<String, BookInfo> bookStoreSearch(String title,
                                  List<String> authors,
                                  String isbn,
                                  String publisher, String sort) {
        return bookDB.searchStore(title, authors, isbn, publisher, sort);
    }

    /**
     * Purchase the quantity of books from the book IDs.
     * @param quantity Quantity of books
     * @param bookIDs The list of book IDs
     * @return Response for the books purchased
     */
    public String bookPurchase(int quantity, List<String> bookIDs){
        return bookDB.purchase(quantity, bookIDs);
    }

    /**
     * Moves the date forward by a certain number of days.
     * @param days The number of days to move forward.
     * @param hours The number of hours
     */
    public String advanceTime(int days, int hours){
        //delegate this command to timeKeeper object
        timeKeeper.addDays(days);
        timeKeeper.addHours(hours);
        return ADVANCE_REQUEST + DELIMITER + SUCCESS;
    }

    /**
     * Read the current date and time from the time keeper.
     * @return The date and time as a string
     */
    public String currentDateTime(){
        return timeKeeper.readTime() + "," + timeKeeper.readDate() + TERMINATOR;
    }

    /**
     * Create the library statistics report for a given number of days.
     * If the number of days is zero, a report will be generated since the
     * start of simulation.
     * @param days Number of days
     * @return Response containing a report about the library
     */
    public String libraryStatisticsReport(int days){
        reporter.generateInfoReport(days);
        return null;
    }

    /**
     * Called by TimeKeeper to close the library.
     */
    public void closeLibrary() {
        //call clearVisitors() in VisitorDB and transition state
        visitorDB.clearCurrentVisitors();
        currentLibraryState = libraryStates.get(CLOSED_STATE);
    }

    /**
     * Called by TimeKeeper to open the library.
     */
    public void openLibrary(){
        //Transition state to open
        currentLibraryState = libraryStates.get(OPEN_STATE);
    }

}
