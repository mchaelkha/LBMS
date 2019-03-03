package Controller.Request;

import Model.Library.LibrarySystem;

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
    private LibrarySystem library;
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
     * @param library The library system
     * @param params The parameters that follow a request command
     */
    public BookPurchase(LibrarySystem library, String params) {
        this.library = library;
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
            bookIDs = new ArrayList<>();
            bookIDs.addAll(Arrays.asList(parts).subList(1, parts.length));
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
        return library.bookPurchase(quantity, bookIDs);
    }
}
