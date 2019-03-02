package main.java.Model.Library;

import main.java.Controller.Request.RequestUtil;

import java.time.LocalDateTime;

/**
 * The state of the Library when it is closed. Checkouts and visits not allowed.
 *
 * @author Luis Gutierrez
 */
public class LibraryClosed implements LibraryState, RequestUtil {


    /**
     * Returns error string notifying visitor of closed library state.
     * @param visitorID the ID of the visitor checking out the books
     * @param isbn the isbn of the book to check out
     * @param checkoutDate the current date of checkout
     */
    @Override
    public String checkoutBook(LocalDateTime checkoutDate, String visitorID, String isbn) {
        return BORROW_REQUEST+DELIMITER+CLOSED_LIBRARY;
    }

    /**
     * Returns error string notifying visitor of closed library state.
     * @param visitorID the visitor whose visit to start
     * @return a formatted string regarding the success of the operation.
     */
    @Override
    public String beginVisit(String visitorID) {
        return ARRIVE_REQUEST+DELIMITER+CLOSED_LIBRARY;
    }
}
