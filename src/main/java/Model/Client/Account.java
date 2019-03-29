package Model.Client;

import Controller.Request.Request;
import Model.Book.BookInfo;

import java.util.Map;
import java.util.Stack;

/**
 * An account registered in the library which performs user-specific requests.
 *
 * @author Michael Kha
 */
public class Account {

    /**
     * The role which determines request permissions
     */
    private Role role;

    /**
     * The visitor that owns the account
     */
    private String visitorID;

    /**
     * The accounts username
     */
    private String username;

    /**
     * The accounts secure password
     */
    private String password;

    /**
     * The book information service to use
     */
    private Service service;

    /**
     * List of undoable commands that have been performed by this account
     */
    private Stack<Request> commandHistory;

    /**
     * List of commands that have been undone and can potentially be redone
     */
    private Stack<Request> undoHistory;

    /**
     * The last store search performed by the account
     */
    private Map<String, BookInfo> storeSearch;

    /**
     * The last library search performed by the account
     */
    private Map<String, BookInfo> librarySearch;

    /**
     * The last borrowed search performed by the account
     */
    private Map<String, BookInfo> borrowedSearch;

    /**
     * Create an account given the following credentials.
     * @param username The username
     * @param password The password
     * @param role The role
     * @param visitorID The visitor ID
     */
    public Account(String username, String password, Role role, String visitorID) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.visitorID = visitorID;
        commandHistory = new Stack<>();
        undoHistory = new Stack<>();
        service = Service.LOCAL;
    }

    /**
     * Set the book information service to the given service
     * @param service The service to set to
     */
    public void setService(Service service) {
        this.service = service;
    }

    /**
     * Get the book information service of the account
     * @return The service
     */
    public Service getService() {
        return service;
    }

    /**
     * Authenticate an account for login by validating username and password
     * are correct (matching).
     * @param username The username to match
     * @param password The password to match
     * @return If both username and password match
     */
    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    /**
     * Set the account's store search.
     * @param books The books to set to
     */
    public void setStoreSearch(Map<String, BookInfo> books) {
        storeSearch = books;
    }

    /**
     * Set the account's library search.
     * @param books The books to set to
     */
    public void setLibrarySearch(Map<String, BookInfo> books) {
        librarySearch = books;
    }

    /**
     * Set the account's borrowed search.
     * @param books The books to set to
     */
    public void setBorrowedSearch(Map<String, BookInfo> books) {
        borrowedSearch = books;
    }

    /**
     * Get the account's store search.
     * @return The store search
     */
    public Map<String, BookInfo> getStoreSearch() {
        return storeSearch;
    }

    /**
     * Get the account's library search.
     * @return The library search
     */
    public Map<String, BookInfo> getLibrarySearch() {
        return librarySearch;
    }

    /**
     * Get the account's borrowed search.
     * @return The borrowed search
     */
    public Map<String, BookInfo> getBorrowedSearch() {
        return borrowedSearch;
    }

    /**
     * Execute the request using the account's role to determine if execution
     * is allowed.
     * @param request The request to execute
     * @return Possibly not authorized message, success, or an error
     */
    public String executeRequest(Request request) {
        return role.executeRequest(request);
    }

    /**
     * Add request executed or redone by this Account to its commandHistory
     * @param request request being added to commandHistory
     */
    public void addPerformedRequest(Request request) {
        //TODO go through each request and add them once executed to commandHistory
        commandHistory.push(request);

        //Clear undoneCommands to prevent undo of further commands
        if (!undoHistory.empty()) {
            undoHistory.clear();
        }
    }

    /**
     * Add request undone by this Account to its undoHistory
     * @param request request being added to its undoHistory
     */
    public void addUndoneCommand(Request request) {
        undoHistory.push(request);
    }

    /**
     * Get the last executed command by this account
     * @return Success of undo request
     */
    public boolean undoRequest() {
        //Check if commandHistory is empty
        if (commandHistory.isEmpty()) {
            return false;
        }
        else{
            Request request = commandHistory.pop();
            request.undo();
            undoHistory.push(request);
            return true;
        }
    }

    /**
     * Get the last undone command by this account
     * @return Success of redo request
     */
    public boolean redoRequest() {
        //Check if undoHistory is empty
        if (undoHistory.isEmpty()) {
            return false;
        }
        else{
            Request request = undoHistory.pop();
            request.redo();
            commandHistory.push(request);
            return true;
        }
    }

    public String getVisitorID() {
        return visitorID;
    }

}
