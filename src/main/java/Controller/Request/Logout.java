package Controller.Request;

import Model.Client.AccountDB;

/**
 * Logout request to deactivate an account in the system.
 * @author Michael Kha
 */
public class Logout extends AccessibleRequest {

    /**
     * Create a logout request with the given client ID
     * @param clientID The client making the request
     */
    public Logout(String clientID) {
        super(clientID, false);
    }

    /**
     * Get the name of the request
     * @return The name
     */
    @Override
    public String getName() {
        return LOGOUT_REQUEST;
    }

    /**
     * Attempt to log out of the account tied to the client.
     * @return Success message for logging out
     */
    @Override
    public String execute() {
        AccountDB accountDB = AccountDB.getInstance();
        return accountDB.logOut(clientID);
    }
}
