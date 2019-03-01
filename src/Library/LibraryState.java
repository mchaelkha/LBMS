package Library;

import java.time.LocalDateTime;

/**
 * The state interface that allows the Library's activities to change depending
 * on whether or not it is open.
 * 
 * @author Hersh Nagpal
 */
public interface LibraryState {
    /**
     * Returns the given book for the given visitor.
     * @param visitorID the ID of the visitor checking out the books
     * @param isbn the isbn of the book to check out
     */
    String checkoutBook(LocalDateTime checkoutDate, String visitorID, String isbn);

    /**
     * Starts a new visit for the given visitor, which allows them to access the library's services.
     * @param visitorID the visitor whose visit to start
     * @return a formatted string regarding the success of the operation.
     */
    String startVisit(String visitorID);
}