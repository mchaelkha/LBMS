package Controller.Request;

import Model.Client.AccountDB;

/**
 * Redo command used to redo a request that has been undone by a specific active account
 */
public class Redo extends AccessibleRequest {

    /**
     * AccountDB used to access account performing the redo request
     */
    private AccountDB accountDB;

    /**
     * Sets the clientID and accountDB for redo functionality
     * @param clientID Used to access account performing redo operation in AccountDB
     */
    public Redo(String clientID) {
        super(clientID, false);
        this.accountDB = AccountDB.getInstance();
    }

    /**
     * Get the name of the request
     * @return The name
     */
    @Override
    public String getName() {
        return REDO_REQUEST;
    }

    /**
     * Performs redo operation on the specific account in accountDB
     * @return String success of request
     */
    public String execute(){
        return accountDB.redo(clientID);
    }
}
