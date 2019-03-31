package Controller.Request;

import Model.Book.BookDB;
import Model.Book.BookInfo;
import Model.Client.AccountDB;

import java.util.*;

/**
 * Book purchase request to add books to the library.
 *
 * @author Michael Kha
 */
public class BookPurchase extends AccessibleRequest {
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
     * (ISBN,BookInfo) Storing copy of books in the library for undo/redo functionality
     */
    private Map<String,BookInfo> booksCopy;
    /**
     * Store search used for this book purchase in order to redo this request
     */
    private Map<String,BookInfo> bookSearch;

    /**
     * Create a new book purchase request given the book database
     * and the parameters for the request.
     * @param clientID The client making the request
     * @param params The parameters that follow a request command
     */
    public BookPurchase(String clientID, String params) {
        super(clientID, true);
        this.bookDB = BookDB.getInstance();
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
     * Get the name of the request
     * @return The name
     */
    @Override
    public String getName() {
        return BUY_REQUEST;
    }

    @Override
    public void undo(){
        bookDB.undoPurchase(bookIDs, quantity);
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
        //Store state of books available in library before executing BookPurchase request
        booksCopy = new HashMap<>(bookDB.getBooksCopy());

        AccountDB accountDB = AccountDB.getInstance();
        Map<String, BookInfo> search = accountDB.getStoreSearch(clientID);
        //Store search that was used for the BookPurchase
        bookSearch = search;
        if (search == null) {
            return clientID + DELIMITER + NOT_AUTHORIZED;
        }
        addToCommandHistory(this,clientID);
        return clientID + DELIMITER + bookDB.purchase(search, quantity, bookIDs);
    }

    /**
     * Undo a Book Purchase request
     */
    @Override
    public void undo(){
        //remove copies added to library books collection
        bookDB.setBooks(booksCopy);
    }

    /**
     * Redo a Book Purchase request.
     */
    @Override
    public void redo(){
        bookDB.purchase(bookSearch, quantity, bookIDs);
    }
}
