package Controller.Request;

import Model.Book.BookDB;

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
     * Message for missing parameters
     */
    private static final String PARAM_MESSAGE = String.format(MISSING_PARAM,
            ARRIVE_REQUEST) + DELIMITER + "quantity,id[,ids]";
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
     * List of books from their IDs of the most recent book store search
     */
    private List<String> bookIDs;

    /**
     * Create a new book purchase request given the book database
     * and the parameters for the request.
     * TODO finish commenting this class
     * @param params The parameters that follow a request command
     */
    public BookPurchase(BookDB bookDB, String params) {
        this.bookDB = bookDB;
        this.params = params;
    }

    /**
     * Check the parameters to validate that the request is
     * @return If the parameters are correct
     */
    @Override
    public boolean checkParams() {
        String[] parts = params.split(DELIMITER);
        if (parts.length > 1) {
            quantity = Integer.parseInt(parts[0]);
            bookIDs = new ArrayList<>();
            bookIDs.addAll(Arrays.asList(parts).subList(1, parts.length));
            return true;
        }
        return false;
    }

    /**
     * Execute the book purchase command which returns a string.
     * @return String indicating the books purchased if any
     */
    @Override
    public String execute() {
        if (!checkParams()) {
            return PARAM_MESSAGE;
        }
        return bookDB.purchase(quantity, bookIDs);
    }
}
