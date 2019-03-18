package Controller;

import Model.Account.Account;

public class Client {

    private String clientID;

    private Account account;

    public Client(String clientID) {
        this.clientID = clientID;
    }

    /**
     * Associate the given account to this client.
     * @param account The account to set
     */
    public void logIn(Account account) {
        this.account = account;
    }

    /**
     * Remove the current account of this client.
     */
    public void logOut() {
        account = null;
    }

}
