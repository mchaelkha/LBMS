package Model.Library;

import Model.Checkout.CheckoutDB;
import Model.Visitor.VisitorDB;

import java.time.LocalDateTime;
import java.util.List;

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
     * @param bookIds the isbns of the books to check out
     */
    String checkoutBooks(LocalDateTime checkoutDate, String visitorID, List<String> bookIds);

    /**
     * Starts a new visit for the given visitor, which allows them to access the library's services.
     * @param visitorID the visitor whose visit to start
     * @return a formatted string regarding the success of the operation.
     */
    String beginVisit(String visitorID);
}