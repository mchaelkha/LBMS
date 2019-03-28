package Controller.Request;

import Model.Client.AccountDB;

/**
 * Undo command used to undo a request that has been performed by a specific active account
 */
public class Undo implements Request{

    /**
     * AccountDB used to access account performing the redo request
     */
    private AccountDB accountDB;

    /**
     * clientID to help access account performing the redo request
     */
    private String clientID;

    /**
     * Sets the clientID and accountDB for undo functionality
     * @param clientID Used to access account performing undo operation in AccountDB
     */
    public Undo(String clientID) {
        this.clientID = clientID;
        this.accountDB = AccountDB.getInstance();
    }

    /**
     * Performs undo operation on the specific account in accountDB
     * @return String success of request
     */
    public String execute(){
        return accountDB.undo(clientID);
    }
}
