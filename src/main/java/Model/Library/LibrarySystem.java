package main.java.Model.Library;
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
     * VisitorDataBase to help complete visitor command functionality
     */
    private VisitorDB visitorDB;

    /**
     * CheckoutDataBase to help complete visitor command functionality
     */
    private CheckoutDB checkoutDB;

    //TODO Add LibrarySystem constructor to set visitorDB and checkoutDB
    //public LibrarySystem()

    /**
     * Gives the status of the library
     * @return Whether the library is open.
     */
    public boolean isOpen() {
        return timeKeeper.isLibraryOpen(OPEN_HOUR, CLOSE_HOUR);
    }

    public String checkoutBook(String visitorID, String isbn) {
        return checkoutBookHelper(visitorID, isbn, checkoutDB, visitorDB);
    }

    /**
     * Delegates checkoutBook visitor command to library concrete state.
     * @param visitorID the visitor borrowing a book
     * @param isbn the book to be checked out
     * @return a formatted string regarding the success of the command
     */
    public String checkoutBookHelper(String visitorID, String isbn, CheckoutDB checkoutDB, VisitorDB visitorDB) {
        return currentLibraryState.checkoutBook(timeKeeper.getClock(), visitorID, isbn, checkoutDB, visitorDB);
    }

    /**
     * Delegates beginVisit visitor command to library concrete state
     * @param visitorID the visitor returning the book
     * @param visitorDB the visitor database to
     * @return the book to be returned by the visitor
     */
    public String beginVisit(String visitorID, VisitorDB visitorDB){
        return currentLibraryState.beginVisit(visitorID, visitorDB);
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
