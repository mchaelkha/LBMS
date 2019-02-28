package Request;

import Book.BookDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Book purchase request to add books to the library.
 *
 * @author Michael Kha
 */
public class BookPurchase implements Request {

    /**
     * The book database of the library
     */
    private BookDB bookDB;
    /**
     * Params in the command
     */
    private String params;
    /**
     * Quantity of books to purchase
     */
    private int quantity;
    /**
     * List of book from their IDs to purchase
     */
    private List<String> ids;

    /**
     * Create a new book purchase request given the book database
     * and the parameters for the request.
     * @param bookDB The book database
     * @param params The parameters that follow a request command
     */
    public BookPurchase(BookDB bookDB, String params) {
        this.bookDB = bookDB;
        this.params = params;
    }

    /**
     * TODO: proper missing parameter checking
     * Check the parameters to validate that the request is
     * @return If the parameters are correct
     */
    @Override
    public String checkParams() {
        String[] parts = params.split(DELIMITER);
        if (parts.length > 1) {
            quantity = Integer.parseInt(parts[0]);
            ids = new ArrayList<>();
            ids.addAll(Arrays.asList(parts).subList(1, parts.length));
            return PROPER_PARAM;
        }
        return PARAM_COUNT;
    }

    /**
     * Execute the book purchase command which returns a string.
     * @return String indicating the books purchased if any
     */
    @Override
    public String execute() {
        String check = checkParams();
        if (!check.equals(PROPER_PARAM)) {
            return check;
        }
        return bookDB.purchase(quantity, ids) + TERMINATOR + NEW_LINE;
    }
}
