package Controller.Request;

import Model.Library.LibrarySystem;
import Model.Visitor.VisitorDB;

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
                      String clientID, String params) {
        this.librarySystem = librarySystem;
        this.visitorDB = visitorDB;
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
        if (parts.length == 1) {
            visitorID = parts[0];
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
        if (!checkParams()) {
            return clientID + DELIMITER + PARAM_MESSAGE;
        }
        //Library.beginVisit()->currentLibraryState.beginVisit()->
        return clientID + DELIMITER + librarySystem.beginVisit(visitorID, visitorDB);
    }
}
