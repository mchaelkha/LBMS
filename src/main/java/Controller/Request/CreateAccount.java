package Controller.Request;

import Model.Client.AccountDB;
import Model.Client.EmployeeRole;
import Model.Client.Role;
import Model.Client.VisitorRole;
import Model.Visitor.VisitorDB;

/**
 * Create account request to create new employee or visitor accounts.
 * @author Michael Kha
 */
public class CreateAccount implements Request {
    /**
     * Message for missing parameters
     */
    private static final String PARAM_MESSAGE = String.format(MISSING_PARAM,
            CREATE_REQUEST) + DELIMITER + "username,password,role,visitor ID";
    /**
     * The visitor database
     */
    private VisitorDB visitorDB;
    /**
     * The account database
     */
    private AccountDB accountDB;
    /**
     * The client ID
     */
    private String clientID;
    /**
     * The request parameters
     */
    private String params;
    private String username;
    private String password;
    private Role role;
    private String visitorID;


    /**
     * Create an account given the visitor database and the parameters
     * for the request.
     * @param params The request parameters
     */
    public CreateAccount(String clientID, String params) {
        this.visitorDB = VisitorDB.getInstance();
        this.clientID = clientID;
        this.params = params;
        accountDB = AccountDB.getInstance();
    }

    /**
     * Check if the parameters are correct and update the parameter values.
     * @return If the parameters are correct
     */
    @Override
    public boolean checkParams() {
        String[] parts = params.split(DELIMITER);
        if (parts.length != 4) {
            return false;
        }
        username = parts[0];
        password = parts[1];
        // Interpret the role
        switch (parts[2]) {
            case "employee":
                role = new EmployeeRole();
                break;
            case "visitor":
                role = new VisitorRole();
                break;
            default:
                return false;
        }
        visitorID = parts[3];
        return true;
    }

    /**
     * Execute the request to create an account by checking the parameters,
     * if the visitor ID is valid, and then create an account by passing
     * verification to the account database.
     * @return String response indicating a possible error or success
     */
    @Override
    public String execute() {
        if (!checkParams()) {
            return clientID + DELIMITER + PARAM_MESSAGE;
        }
        if (!visitorDB.validRegisteredVisitor(visitorID)) {
            return clientID + DELIMITER + CREATE_REQUEST +
                    DELIMITER + "invalid-visitor";
        }
        return clientID + DELIMITER + accountDB.createAccount(username, password, role, visitorID);
    }
}
