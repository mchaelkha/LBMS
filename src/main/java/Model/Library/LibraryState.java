package main.java.Model.Library;

import main.java.Model.Checkout.CheckoutDB;
import main.java.Model.Visitor.VisitorDB;

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
    String checkoutBooks(LocalDateTime checkoutDate, String visitorID, List<String> bookIds, CheckoutDB checkOutDB, VisitorDB visitorDB);

    /**
     * Starts a new visit for the given visitor, which allows them to access the library's services.
     * @param visitorID the visitor whose visit to start
     * @return a formatted string regarding the success of the operation.
     */
    String beginVisit(String visitorID, VisitorDB visitorDB);
}