package Library;
import java.util.Map;
import Request.Request;

/**
 * The LibrarySystem acts as a reciever of commands and delegates 
 * almost all functionality to other Database and helper classes.
 * @author Hersh Nagpal
 */
public class LibrarySystem {
    private static int OPEN_HOUR = 9;
    private static int CLOSE_HOUR = 12+9;
    private Map<String,LibraryState> states;
    public LibraryState currentLibraryState;

    private TimeKeeper timeKeeper;

    /**
     * Gives the status of the library
     * @return Whether the library is open.
     */
    public boolean isOpen() {
        return timeKeeper.isLibraryOpen(OPEN_HOUR, CLOSE_HOUR);
    }

    /**
     * Returns the given book for the given visitor.
     * @param visistorID the visitor returning the book
     * @param isbn the book to be returned by the visitor
     * @return a formatted string regarding the success
     */
    public String checkoutBook(String visitorID, String isbn) {
        return currentLibraryState.checkoutBook(visitorID, isbn);
    }

    /**
     * Moves the date forward by a certain number of days.
     * @param days The number of days to move forward.
     */
    public void moveDate(int days){

    }
}
