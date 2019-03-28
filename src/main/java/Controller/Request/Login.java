package Controller.Request;

import Model.Client.AccountDB;

/**
 * Login request made by clients in order to log an active account.
 * @author Michael Kha
 */
public class Login implements Request {
    /**
     * Message for missing parameters
     */
    private static final String PARAM_MESSAGE = String.format(MISSING_PARAM,
            LOGIN_REQUEST) + DELIMITER + "username,password";
    /**
     * Client making the request
     */
    private String clientID;
    /**
     * The parameters of the login request
     */
    private String params;
    /**
     * The username
     */
    private String username;
    /**
     * The password
     */
    private String password;

    /**
     * Create a login request given the client logging in and the parameters.
     * @param clientID The client making the request
     * @param params The parameters to log in with
     */
    public Login(String clientID, String params) {
        this.clientID = clientID;
        this.params = params;
    }

    /**
     * Check the parameters and initialize the username and password
     * @return If the parameters are provided
     */
    @Override
    public boolean checkParams() {
        String[] parts = params.split(DELIMITER);
        if (parts.length == 2) {
            username = parts[0];
            password = parts[1];
            return true;
        }
        return false;
    }

    /**
     * Execute the login request by checking the parameters and attempting a login.
     * @return Login response from the account database
     */
    @Override
    public String execute() {
        if (!checkParams()) {
            return clientID + DELIMITER + PARAM_MESSAGE;
        }
        AccountDB accountDB = AccountDB.getInstance();
        return accountDB.logIn(clientID, username, password);
    }
}
