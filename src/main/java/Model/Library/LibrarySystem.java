package Model.Library;
import Controller.Request.RequestUtil;
import Model.Book.BookDB;
import Model.Book.BookInfo;
import Model.Checkout.CheckoutDB;
import Model.Visitor.VisitorDB;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The LibrarySystem acts as a receiver of commands and delegates
 * almost all functionality to other Database and helper classes.
 * @author Hersh Nagpal
 * @author Luis Gutierrez
 */
public class LibrarySystem implements Serializable, RequestUtil{
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
     * VisitorDataBase to kick out current visitors when library is closed.
     */
    private VisitorDB visitorDB;

    /**
     * The report generator to create daily reports
     */
    private ReportGenerator reporter;

    /**
     * Object used to update library time and to notify library when
     * to close and open (Library state transition)
     */
    private TimeKeeper timeKeeper;

    /**
     * Create the library system that is responsible for knowing about the system's model.
     * @param reporter The reporter to create reports
     */
    public LibrarySystem(ReportGenerator reporter) {
        this.visitorDB = VisitorDB.getInstance();
        this.timeKeeper = TimeKeeper.getInstance();
        this.reporter = reporter;
        //Add Library States
        libraryStates = new HashMap<>();
        libraryStates.put(CLOSED_STATE, new LibraryClosed());
        libraryStates.put(OPEN_STATE, new LibraryOpen());

        //Set initial state based on current time
        currentLibraryState = libraryStates.get(CLOSED_STATE);
    }

    /**
     * Gives the status of the library
     * @return Whether the library is open.
     */
    public boolean isOpen() {
        return currentLibraryState == libraryStates.get(OPEN_STATE);
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
     * Delegates checkoutBook visitor command to library concrete state.
     * @param search The book search
     * @param visitorID the visitor borrowing a book
     * @param bookIds the books to be checked out
     * @return formatted string regarding the success of the command
     */
    public String checkoutBooks(Map<String, BookInfo> search, String visitorID, List<String> bookIds) {
        return currentLibraryState.checkoutBooks(search, timeKeeper.getClock(),visitorID, bookIds);
    }

    /**
     * Called by TimeKeeper to close the library.
     */
    public void closeLibrary() {
        if (isOpen()) {
            //clear current visitors
            visitorDB.clearCurrentVisitors(timeKeeper.getClock());

            //Generate daily report
            reporter.generateDailyReport();

            //Transition library state to closed
            currentLibraryState = libraryStates.get(CLOSED_STATE);
        }
    }

    /**
     * Called by TimeKeeper to open the library.
     */
    public void openLibrary(){
        if (!isOpen()) {
            //Transition state to open
            currentLibraryState = libraryStates.get(OPEN_STATE);
        }
    }

}
