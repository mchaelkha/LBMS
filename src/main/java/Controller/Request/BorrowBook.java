package Controller.Request;

import Model.Book.BookDB;
import Model.Checkout.CheckoutDB;
import Model.Library.LibrarySystem;
import Model.Visitor.VisitorDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Borrow book request to allow visitors to checkout books.
 *
 * @author Michael Kha
 */
public class BorrowBook implements Request {
    /**
     * Message for missing parameters
     */
    private static final String PARAM_MESSAGE = String.format(MISSING_PARAM,
            ARRIVE_REQUEST) + DELIMITER + "visitor ID,{id}";
    /**
     * The librarySystem. Used to check library closed or open state.
     */
    private LibrarySystem librarySystem;
    /**
     * Used to verify that visitor can borrow a book and to create a new transaction
     */
    private CheckoutDB checkoutDB;
    /**
     * Used to check for valid visitor id.
     */
    private VisitorDB visitorDB;

    private BookDB bookDB;
    /**
     * Params in the command
     */
    private String params;
    /**
     * The visitor ID to borrow with
     */
    private String visitorID;
    /**
     * The list of book IDs to borrow
     */
    private List<String> bookIDs;

    /**
     * Create a new borrow book request given the book database and the
     * parameters for the request.
     * @param librarySystem used to delegate command actions
     * @param params The parameters that follow a request command
     */
    public BorrowBook(LibrarySystem librarySystem, CheckoutDB checkoutDB,
                      VisitorDB visitorDB, BookDB bookDB, String params) {
        this.librarySystem = librarySystem;
        this.checkoutDB = checkoutDB;
        this.visitorDB = visitorDB;
        this.bookDB = bookDB;
        this.params = params;
    }

    /**
     * Check the parameters to validate that the request is
     * @return If the parameters are correct
     */
    @Override
    public String checkParams() {
        String[] parts = params.split(DELIMITER);
        if (parts.length > 1) {
            visitorID = parts[0];
            bookIDs = new ArrayList<>();
            bookIDs.addAll(Arrays.asList(parts).subList(1, parts.length));
            return PROPER_PARAM;
        }
        return PARAM_MESSAGE;
    }

    /**
     * Execute the borrow book command which returns a string.
     * @return String indicating if book was borrowed by providing a due date
     *         or an error message for invalid input.
     */
    @Override
    public String execute() {
        String check = checkParams();
        if (!check.equals(PROPER_PARAM)) {
            return check;
        }
        //library.checkoutBooks()->currLibraryState.checkoutBooks()->checkoutDB.checkout()
        return librarySystem.checkoutBooks(visitorID,bookIDs,checkoutDB,visitorDB,bookDB);
    }
}
