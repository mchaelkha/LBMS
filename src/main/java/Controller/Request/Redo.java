package Controller.Request;

import Model.Client.AccountDB;

/**
 * Redo command used to redo a request that has been undone by a specific active account
 */
public class Redo implements Request{
    private AccountDB accountDB;

    private String clientID;

    /**
     * Sets the clientID and accountDB for redo functionality
     * @param accountDB AccountDB used to access specific account that is performing redo operation
     * @param clientID Used to access account performing redo operation in AccountDB
     */
    public Redo(AccountDB accountDB, String clientID) {
        this.clientID = clientID;
        this.accountDB = accountDB;
    }

    public String execute(){
        return accountDB.redo(clientID);
    }
}
