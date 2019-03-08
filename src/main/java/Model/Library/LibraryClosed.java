package Model.Library;

import Controller.Request.RequestUtil;
import Model.Book.BookDB;
import Model.Checkout.CheckoutDB;
import Model.Visitor.VisitorDB;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The state of the Library when it is closed. Checkouts and visits not allowed.
 *
 * @author Luis Gutierrez
 */
public class LibraryClosed implements LibraryState, RequestUtil {

    /**
     * Returns error string notifying visitor of closed library state.
     * @param visitorID the ID of the visitor checking out the books
     * @param bookIds the isbns of the books to check out
     * @param checkoutDate the current date of checkout
     */
    @Override
    public String checkoutBooks(LocalDateTime checkoutDate, String visitorID, List<String> bookIds,
                                CheckoutDB checkoutDB, VisitorDB visitorDB, BookDB bookDB) {
        return BORROW_REQUEST+DELIMITER+CLOSED_LIBRARY+TERMINATOR;
    }

    /**
     * Returns error string notifying visitor of closed library state.
     * @param visitorID the visitor whose visit to start
     * @return a formatted string regarding the success of the operation.
     */
    @Override
    public String beginVisit(String visitorID, VisitorDB visitorDB) {
        return ARRIVE_REQUEST+DELIMITER+CLOSED_LIBRARY+TERMINATOR;
    }
}
