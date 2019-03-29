package Controller.Request;

import Model.Client.AccountDB;

/**
 * Undo command used to undo a request that has been performed by a specific active account
 */
public class Undo extends AccessibleRequest {

    /**
     * AccountDB used to access account performing the redo request
     */
    private AccountDB accountDB;

    /**
     * Sets the clientID and accountDB for undo functionality
     * @param clientID Used to access account performing undo operation in AccountDB
     */
    public Undo(String clientID) {
        super(clientID, false);
        this.accountDB = AccountDB.getInstance();
    }

    /**
     * Get the name of the request
     * @return The name
     */
    @Override
    public String getName() {
        return UNDO_REQUEST;
    }

    /**
     * Performs undo operation on the specific account in accountDB
     * @return String success of request
     */
    public String execute(){
        return accountDB.undo(clientID);
    }
}
