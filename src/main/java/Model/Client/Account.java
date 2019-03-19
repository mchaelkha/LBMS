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
     * List of commands that have been done and can potentially be undone
     */
    private Stack<Request> commandHistory;

    /**
     * List of commands that have been undone and can potentially be redone
     */
    private Stack<Request> undoHistory;

    /**
     * The last search performed by the account
     */
    private Map<String, BookInfo> lastSearch;

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

    // TODO: add helper methods to call requests in the role field
}
