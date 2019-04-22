package Controller.Request;

import Model.Book.BookDB;
import Model.Book.BookInfo;
import Model.Checkout.CheckoutDB;
import Model.Client.AccountDB;
import Model.Library.LibrarySystem;
import Model.Library.TimeKeeper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Borrow book request to allow visitors to checkout books.
 *
 * @author Michael Kha
 */
public class BorrowBook extends AccessibleRequest {
    /**
     * Message for missing parameters
     */
    private static final String PARAM_MESSAGE = String.format(MISSING_PARAM,
            ARRIVE_REQUEST) + DELIMITER + "visitor ID,{id}";
    /**
     * The librarySystem. Used to check library closed or open state.
     */
    private LibrarySystem librarySystem;

    private CheckoutDB checkoutDB;

    private Map<String, BookInfo> search;
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
     * @param clientID The client making the request
     * @param params The parameters that follow a request command
     */
    public BorrowBook(LibrarySystem librarySystem, String clientID, String params) {
        super(clientID, false);
        this.librarySystem = librarySystem;
        this.checkoutDB = CheckoutDB.getInstance();
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
            bookIDs = new ArrayList<>();
            if (parts[parts.length - 1].length() == 10) {
                visitorID = parts[parts.length - 1];
                bookIDs.addAll(Arrays.asList(parts).subList(0, parts.length - 1));
            }
            else {
                AccountDB accountDB = AccountDB.getInstance();
                visitorID = accountDB.getVisitorIDFromClientID(clientID);
                bookIDs.addAll(Arrays.asList(parts));
            }
            return true;
        }
        return false;
    }

    /**
     * Undoing BorrowBook command is equivalent to returning books (ReturnBook)
     */
    public void undo(){
        checkoutDB.returnBooks(search, visitorID, bookIDs, BookDB.getInstance(), TimeKeeper.getInstance());
    }

    /**
     * Re-execute this encapsulated request
     */
    public void redo(){
        execute();
    }

    /**
     * Get the name of the request
     * @return The name
     */
    @Override
    public String getName() {
        return BORROW_REQUEST;
    }

    /**
     * Execute the borrow book command which returns a string.
     * @return String indicating if book was borrowed by providing a due date
     *         or an error message for invalid input.
     */
    @Override
    public String execute() {
        if (!checkParams()) {
            return clientID + DELIMITER + PARAM_MESSAGE;
        }
        //library.checkoutBooks()->currLibraryState.checkoutBooks()->checkoutDB.checkout()
        AccountDB accountDB = AccountDB.getInstance();
        Map<String, BookInfo> search = accountDB.getLibrarySearch(clientID);
        if (search == null) {
            return clientID + DELIMITER + NOT_AUTHORIZED;
        }
        return clientID + DELIMITER + librarySystem.checkoutBooks(search, visitorID,bookIDs);
    }
}
