package Model.Client;

import Controller.Request.Request;
import Controller.Request.RequestUtil;
import Model.Book.BookInfo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the accounts of the library by performing operations on the accounts
 * or by using the accounts to perform requests.
 *
 * @author Michael Kha
 */
public class AccountDB implements Serializable, RequestUtil {

    /**
     * Singleton instance
     */
    private static AccountDB instance = null;

    /**
     * All the accounts that have been created with usernames as keys
     */
    private Map<String, Account> accounts;

    /**
     * All the accounts that have been created with visitor IDs as keys
     */
    private Map<String, Account> visitors;

    /**
     * Only logged in accounts. The client ID mapped to an account after
     * being logged in. Logging out removes the account from being active.
     */
    private Map<String, Account> activeAccounts;

    /**
     * Admin employee that makes initial simulation requests
     */
    private Account employee01;

    /**
     * Create the account database which holds all accounts and active accounts.
     */
    private AccountDB() {
        accounts = new HashMap<>();
        visitors = new HashMap<>();
        activeAccounts = new HashMap<>();
        employee01 = new Account("Admin", "123", new EmployeeRole(), "1000000000");
        accounts.put("Admin", employee01);
        activeAccounts.put("1000", employee01);
    }

    /**
     * Get the single instance of AccountDB
     * @return The one and only AccountDB
     */
    public static AccountDB getInstance() {
        if (instance == null) {
            instance = new AccountDB();
        }
        return instance;
    }

    /**
     * Execute the request. If the request is tied to a client ID,
     * let the account execute depending on its role.
     * @param request The request to execute
     * @return An execution response
     */
    public String executeRequest(Request request) {
        String clientID = request.getClientID();
        Account account = activeAccounts.get(clientID);
        if (account == null) {
            return clientID + DELIMITER + request.getName()
                    + DELIMITER + NOT_AUTHORIZED;
        }
        return account.executeRequest(request);
    }

    /**
     * Check if the client ID is tied to an active account.
     * @param clientID The client ID to check
     * @return Is the client logged into an account
     */
    public boolean isActiveAccount(String clientID) {
        return activeAccounts.containsKey(clientID);
    }

    /**
     * Add the request to the command history.
     * @param request The request to add
     * @param clientID The client ID to get the account to add to
     */
    public void addToCommandHistory(Request request, String clientID) {
        Account account = activeAccounts.get(clientID);
        account.addPerformedRequest(request);
    }

    /**
     * Add the request to the undo history.
     * @param request The request to add
     * @param clientID The client ID to get the account to add to
     */
    public void addToUndoHistory(Request request, String clientID) {
        Account account = activeAccounts.get(clientID);
        account.addUndoneCommand(request);
    }

    /**
     * Create an account using the necessary credentials.
     * @param username Username to use
     * @param password Password to use
     * @param role The account role, either visitor or employee
     * @param visitorID The associated visitor ID
     * @return An error or success response about the creation of the account
     */
    public String createAccount(String username,
                                String password, Role role, String visitorID) {
        // Check no duplicate username
        if (accounts.containsKey(username)) {
            return CREATE_REQUEST + DELIMITER + "duplicate-username" + TERMINATOR;
        }
        // Check no duplicate visitor ID
        if (visitors.containsKey(visitorID)) {
            return CREATE_REQUEST + DELIMITER + "duplicate-visitor" + TERMINATOR;
        }
        // Create account
        Account account = new Account(username, password, role, visitorID);
        accounts.put(username, account);
        visitors.put(visitorID, account);
        // Return success response
        return CREATE_REQUEST + DELIMITER + SUCCESS + TERMINATOR;
    }

    /**
     * Log into an account and associate the client ID to the active account.
     * @param clientID The client ID to log in an account for
     * @param username The username of the account
     * @param password The password of the account
     * @return A response indicating if the login was successful or not
     */
    public String logIn(String clientID, String username, String password) {
        // Check if username and password valid
        if (!accounts.containsKey(username)) {
            return clientID + DELIMITER + LOGIN_REQUEST +
                    DELIMITER + "bad-username-or-password" + TERMINATOR;
        }
        Account account = accounts.get(username);
        if (!account.authenticate(username, password)) {
            return clientID + DELIMITER + LOGIN_REQUEST +
                    DELIMITER + "bad-username-or-password" + TERMINATOR;
        }
        // Update active accounts
        activeAccounts.put(clientID, account);
        // Return success response
        return clientID + DELIMITER + LOGIN_REQUEST +
                DELIMITER + SUCCESS + TERMINATOR;
    }

    /**
     * Log out of an active account that is associated to the client ID.
     * @param clientID The client ID mapped to the account
     * @return Success response for logging out
     */
    public String logOut(String clientID) {
        // Update active accounts;
        activeAccounts.remove(clientID);
        // Return success response
        return clientID + DELIMITER + LOGOUT_REQUEST +
                DELIMITER + SUCCESS + TERMINATOR;
    }

    /**
     * Undo the last command of the account that is associated to the client ID.
     * @param clientID The client ID to get the account to undo for
     * @return A response indicating if the undo operation could be performed
     */
    public String undo(String clientID) {
        // Grab account and call undo method
        Account account = activeAccounts.get(clientID);
        //Return response based on undo success
        boolean requestSuccess = account.undoRequest();
        if (requestSuccess) {
            return clientID+DELIMITER+UNDO_REQUEST+DELIMITER+SUCCESS+TERMINATOR;
        }
        else{
            return clientID+DELIMITER+UNDO_REQUEST+DELIMITER+CANNOT_UNDO+TERMINATOR;
        }
    }

    /**
     * Undo the last undone command of the account that is associated to the client ID.
     * @param clientID The client ID to get the account to undo for
     * @return A response indicating if the redo operation could be performed
     */
    public String redo(String clientID) {
        // Grab account and call undo method
        Account account = activeAccounts.get(clientID);
        //Return response based on redo success
        boolean requestSuccess = account.redoRequest();
        if (requestSuccess) {
            return clientID+DELIMITER+REDO_REQUEST+DELIMITER+SUCCESS+TERMINATOR;
        }
        else{
            return clientID+DELIMITER+REDO_REQUEST+DELIMITER+CANNOT_REDO+TERMINATOR;
        }
    }

    public String getVisitorIDFromClientID(String clientID) {
        return activeAccounts.get(clientID).getVisitorID();
    }

    /**
     * Set the book info service for an active account.
     * @param clientID The client ID to get the account to set the service for
     * @return A response indicating if the service was set
     */
    public String setBookInfoService(String clientID, Service service) {
        // Check account is active
        if (!activeAccounts.containsKey(clientID)) {
            return clientID + DELIMITER + SERVICE_REQUEST + DELIMITER + "cannot-set" + TERMINATOR;
        }
        // Perform setting on account
        Account account = activeAccounts.get(clientID);
        account.setService(service);
        // Return success response
        return clientID + DELIMITER + SERVICE_REQUEST + DELIMITER + "success" + TERMINATOR;
    }

    public Service getService(String clientID) {
        Account account = activeAccounts.get(clientID);
        if (account == null) {
            return null;
        }
        return account.getService();
    }

    /**
     * Set the account's library search.
     * @param books The books to set to
     * @param clientID The client ID to get the account
     */
    public void setLibrarySearch(Map<String, BookInfo> books, String clientID) {
        Account account = activeAccounts.get(clientID);
        if (account == null) {
            return;
        }
        account.setLibrarySearch(books);
    }

    /**
     * Set the account's store search.
     * @param books The books to set to
     * @param clientID The client ID to get the account
     */
    public void setStoreSearch(Map<String, BookInfo> books, String clientID) {
        Account account = activeAccounts.get(clientID);
        if (account == null) {
            return;
        }
        account.setStoreSearch(books);
    }

    /**
     * Set the account's borrowed search.
     * @param books The books to set to
     * @param clientID The client ID to get the account
     */
    public void setBorrowedSearch(Map<String, BookInfo> books, String clientID) {
        Account account = activeAccounts.get(clientID);
        if (account == null) {
            return;
        }
        account.setBorrowedSearch(books);
    }

    /**
     * Get the account's library search.
     * @param clientID The client ID to get the account
     * @return The book search
     */
    public Map<String, BookInfo> getLibrarySearch(String clientID) {
        Account account = activeAccounts.get(clientID);
        if (account == null) {
            return null;
        }
        return account.getLibrarySearch();
    }

    /**
     * Get the account's store search.
     * @param clientID The client ID to get the account
     * @return The book search
     */
    public Map<String, BookInfo> getStoreSearch(String clientID) {
        Account account = activeAccounts.get(clientID);
        if (account == null) {
            return null;
        }
        return account.getStoreSearch();
    }

    /**
     * Set the account's borrowed search.
     * @param clientID The client ID to get the account
     * @return The book search
     */
    public Map<String, BookInfo> getBorrowedSearch(String clientID) {
        Account account = activeAccounts.get(clientID);
        if (account == null) {
            return null;
        }
        return account.getBorrowedSearch();
    }

}
