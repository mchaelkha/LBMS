package Controller.Request;

import Model.Book.BookDB;
import Model.Book.BookInfo;
import Model.Client.AccountDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
            BUY_REQUEST) + DELIMITER + "quantity,id[,ids]";
    /**
     * The book database of the library
     */
    private BookDB bookDB;
    /**
     * The client that made this request
     */
    private String clientID;
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
     * @param clientID The client making the request
     * @param params The parameters that follow a request command
     */
    public BookPurchase(String clientID, String params) {
        this.bookDB = BookDB.getInstance();
        this.clientID = clientID;
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
            return clientID + DELIMITER + PARAM_MESSAGE;
        }
        AccountDB accountDB = AccountDB.getInstance();
        Map<String, BookInfo> search = accountDB.getStoreSearch(clientID);
        if (search == null) {
            return clientID + DELIMITER + NOT_AUTHORIZED;
        }
        return clientID + DELIMITER + bookDB.purchase(search, quantity, bookIDs);
    }
}
