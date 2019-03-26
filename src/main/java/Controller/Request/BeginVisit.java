package Controller.Request;

import Model.Client.AccountDB;
import Model.Library.LibrarySystem;
import Model.Visitor.VisitorDB;

import java.util.ArrayList;

/**
 * Begin visit request to start a visit for a visitor.
 *
 * @author Michael Kha
 */
public class BeginVisit implements Request {
    /**
     * Message for missing parameters
     */
    private static final String PARAM_MESSAGE = String.format(MISSING_PARAM,
            ARRIVE_REQUEST) + DELIMITER + "visitorID";
    /**
     * The librarySystem. Used to check library closed or open state.
     */
    private LibrarySystem librarySystem;
    /**
     * The Visitor database. Used to add the visitor to the collection of current visitors.
     */
    private VisitorDB visitorDB;
    /**
     * Account database. Used to add the beginVisit request to account who requested it.
     */
    private AccountDB accountDB;
    /**
     * The client that made this request
     */
    private String clientID;
    /**
     * Params in the command
     */
    private String params;
    /**
     * The visitor ID to start the visit for
     */
    private String visitorID;

    /**
     * Create a new begin visit request given the visitor database
     * and the parameters for the request.
     * @param librarySystem The library system containing system databases
     * @param params The parameters that follow a request command
     */
    public BeginVisit(LibrarySystem librarySystem, VisitorDB visitorDB,
                      AccountDB accountDB, String clientID, String params) {
        this.librarySystem = librarySystem;
        this.visitorDB = visitorDB;
        this.accountDB = accountDB;
        this.clientID = clientID;
        this.params = params;
    }

    /**
     * Check the parameters to validate that the request is
     * @return If the parameters are correct
     */
    @Override
    public boolean checkParams() {
        String[] parts = params.split(DELIMITER);
        //visitorID given
        if (parts.length == 1) {
            visitorID = parts[0];
            return true;
        }
        //visitorID not given
        else if (parts.length == 0) {
            visitorID = accountDB.getVisitorIDFromClientID(clientID);
            return true;
        }
        return false;
    }

    /**
     * Execute the begin visit command which returns a string.
     * @return String indicating that the visit has started successfully
     */
    @Override
    public String execute() {
        //TODO check if visitorID param is given, if not use clientID to find visitorID before calling beginVisit()
        if (!checkParams()) {
            return clientID + DELIMITER + PARAM_MESSAGE;
        }
        String response = visitorID + DELIMITER + librarySystem.beginVisit(visitorID, visitorDB);
        String[] parts = response.split(",");
        //Only add successful beginVisit requests to account commandHistory
        if(parts.length == 4){
            accountDB.addRequestToCommandHistory(this, clientID);
        }
        //Library.beginVisit()->currentLibraryState.beginVisit()->
        return visitorID + DELIMITER + librarySystem.beginVisit(visitorID, visitorDB);
    }

    /**
     * Undo a begin visit request
     */
    @Override
    public void undo(){

    }

    /**
     * Redo a begin visit request
     */
    @Override
    public void redo(){

    }
}
