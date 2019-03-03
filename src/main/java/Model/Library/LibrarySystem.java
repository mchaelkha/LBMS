package Model.Library;
import Controller.Request.RequestUtil;
import Model.Book.BookDB;
import Model.Book.BookInfo;
import Model.Checkout.CheckoutDB;
import Model.Visitor.VisitorDB;

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
     * The hour that the library becomes open
     */
    private static final int OPEN_HOUR = 9;
    /**
     * The hour that the library becomes closed
     */
    private static final int CLOSE_HOUR = 12+9;

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
        libraryStates.put(OPEN_STATE, new LibraryOpen(timeKeeper, checkoutDB, visitorDB));
        currentLibraryState = libraryStates.get(OPEN_STATE);
    }

    /**
     * Gives the status of the library
     * @return Whether the library is open.
     */
    public boolean isOpen() {
        return timeKeeper.isLibraryOpen(OPEN_HOUR, CLOSE_HOUR);
    }

    /**
     * Delegates registerVisitor command to VisitorDB
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
        for (String isbn : bookIds) {
            if (!lastBookSearch.containsKey(isbn)) {
                //Response = "borrow,invalid-book-id,{id};"
                String bookIdsString = String.join(",", bookIds);
                return BORROW_REQUEST+DELIMITER+INVALID_BOOK_ID+DELIMITER+bookIdsString+TERMINATOR;
            }
        }
        return currentLibraryState.checkoutBooks(timeKeeper.getClock(),visitorID, bookIds);
    }

    //TODO delegate this request to checkoutDB and get the list of books borrowed by visitorID
    public String findBorrowedBooks(String visitorID){
        return null;
    }

    /**
     * @param visitorID visitor
     * @param visitorDB visitor database
     * @param checkoutDB checkout database
     * @return String whether returnBook command was successful
     */
    public String returnBook(String visitorID, VisitorDB visitorDB, CheckoutDB checkoutDB) {
        return null;
    }

    //TODO delegate to checkoutDB and create method in checkoutDB to pay all or part of an outstanding fine
    public String payFine(String visitorID, int amount){
        return null;
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
        return bookDB.purchase(quantity, bookIDs) + TERMINATOR;
    }

    /**
     * TODO
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

    //TODO
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
        return null;
    }

    /**
     * Called when library changes state to closed.
     */
    public void closeLibrary() {
        //call clearVisitors() in VisitorDB
        currentLibraryState = libraryStates.get(CLOSED_STATE);
    }

}
