package Controller.Request;

import Model.Client.AccountDB;

public class Undo implements Request{

    private AccountDB accountDB;

    private String clientID;

    public Undo(AccountDB accountDB, String clientID) {
        this.clientID = clientID;
        this.accountDB = accountDB;
    }

    public String execute(){
        return accountDB.undo(clientID);
    }
}
