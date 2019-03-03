package main.java.Model.Library;
import main.java.Model.Book.BookDB;
import main.java.Model.Checkout.CheckoutDB;
import main.java.Model.Visitor.VisitorDB;

import java.util.Map;

/**
 * The LibrarySystem acts as a receiver of commands and delegates
 * almost all functionality to other Database and helper classes.
 * @author Hersh Nagpal
 */
public class LibrarySystem {
    private static int OPEN_HOUR = 9;
    private static int CLOSE_HOUR = 12+9;

    /**
     * Collection of library states used during state transitions.
     */
    private Map<String,LibraryState> states;

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


    public LibrarySystem(VisitorDB visitorDB, CheckoutDB checkoutDB, BookDB bookDB) {
        this.visitorDB = visitorDB;
        this.checkoutDB = checkoutDB;
        this.bookDB = bookDB;
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
     * Delegates checkoutBook visitor command to library concrete state.
     * @param visitorID the visitor borrowing a book
     * @param isbn the book to be checked out
     * @return formatted string regarding the success of the command
     */
    public String checkoutBook(String visitorID, String isbn) {
        return currentLibraryState.checkoutBook(timeKeeper.getClock(),visitorID, isbn, checkoutDB, visitorDB);
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

    /**
     * Moves the date forward by a certain number of days.
     * @param days The number of days to move forward.
     */
    public void moveDate(int days){
        //delegate this command to timeKeeper object
    }

    /**
     * Called when library changes state to closed.
     */
    public void closeLibrary() {
        //call clearVisitors() in VisitorDB
    }

}
