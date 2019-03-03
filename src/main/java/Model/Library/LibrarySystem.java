package main.java.Model.Library;
import main.java.Controller.Request.RequestUtil;
import main.java.Model.Book.BookDB;
import main.java.Model.Book.BookInfo;
import main.java.Model.Checkout.CheckoutDB;
import main.java.Model.Visitor.VisitorDB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The LibrarySystem acts as a receiver of commands and delegates
 * almost all functionality to other Database and helper classes.
 * @author Hersh Nagpal
 */
public class LibrarySystem implements RequestUtil{
    private static int OPEN_HOUR = 9;
    private static int CLOSE_HOUR = 12+9;

    /**
     * Collection of library states used during state transitions.
     */
    private Map<String,LibraryState> libraryStates;

    /**
     * Represents the current state of the library (closed or open).
     */
    public LibraryState currentLibraryState;

    /**
     * Object used to update library time and to notify library when
     * to close and open (Library state transition)
     */
    private TimeKeeper timeKeeper;
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
     * Tracks the last book search made by visitor in order to complete borrow book command
     */
    private Map<String,BookInfo> lastBookSearch;

    public LibrarySystem(VisitorDB visitorDB, CheckoutDB checkoutDB, BookDB bookDB) {
        this.visitorDB = visitorDB;
        this.checkoutDB = checkoutDB;
        this.bookDB = bookDB;

        //Add Library States
        libraryStates = new HashMap<>();
        libraryStates.put("LibraryClosed", new LibraryClosed());
        libraryStates.put("LibraryOpen", new LibraryOpen());
        currentLibraryState = libraryStates.get("LibraryOpen");
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
        return visitorDB.registerVisitor(firstName, lastName, address, phoneNumber);
    }

    /**
     * Delegates beginVisit visitor command to library concrete state
     * @param visitorID the visitor returning the book
     * @return formatted string regarding the success of the beginVisit command
     */
    public String beginVisit(String visitorID){
        return currentLibraryState.beginVisit(visitorID, visitorDB);
    }

    /**
     * Delegates endVisit visitor command to VisitorDB
     * @param visitorID the visitor ending their visit
     * @return formatted string regarding the success of the endVisit command
     */
    public String endVisit(String visitorID) {
        return visitorDB.endVisit(visitorID);
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
        return currentLibraryState.checkoutBooks(timeKeeper.getClock(),visitorID, bookIds, checkoutDB, visitorDB);
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

    //TODO
    public String bookStoreSearch() {
        return null;
    }

    //TODO
    public String bookPurchase(){
        return null;
    }

    /**
     * TODO
     * Moves the date forward by a certain number of days.
     * @param days The number of days to move forward.
     */
    public String advanceTime(int days){
        //delegate this command to timeKeeper object
        return null;
    }

    //TODO
    public String currentDateTime(){
        return null;
    }

    //TODO
    public String libraryStatisticsReport(){
        return null;
    }

    /**
     * Called when library changes state to closed.
     */
    public void closeLibrary() {
        //call clearVisitors() in VisitorDB
    }

}
