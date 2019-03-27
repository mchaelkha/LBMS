package Controller.Request;

import Model.Client.AccountDB;

/**
 * Logout request to deactivate an account in the system.
 * @author Michael Kha
 */
public class Logout implements Request {

    /**
     * Client making the request
     */
    private String clientID;

    /**
     * Create a logout request with the given client ID
     * @param clientID The client making the request
     */
    public Logout(String clientID) {
        this.clientID = clientID;
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
