package Controller.Request;

import Model.Client.AccountDB;
import Model.Library.LibrarySystem;
import Model.Library.TimeKeeper;
import Model.Visitor.Visit;
import Model.Visitor.VisitorDB;

import java.time.LocalDateTime;

/**
 * Begin visit request to start a visit for a visitor.
 *
 * @author Michael Kha
 */
public class BeginVisit extends AccessibleRequest {
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
     * Params in the command
     */
    private String params;
    /**
     * The visitor ID to start the visit for
     */
    private String visitorID;
    /**
     * Hold the visit started by beginVisit to maintain visit start time when redoing request
     */
    private Visit startVisit;
    /**
     * Used to for readDate and readTime functionality in timeKeeper for redoing BeginVisit requests
     */
    private TimeKeeper timeKeeper;

    /**
     * Create a new begin visit request given the visitor database
     * and the parameters for the request.
     * @param librarySystem The library system containing system databases
     * @param params The parameters that follow a request command
     */
    public BeginVisit(LibrarySystem librarySystem,
                      String clientID, String params) {
        super(clientID, false);
        this.timeKeeper = TimeKeeper.getInstance();
        this.librarySystem = librarySystem;
        this.visitorDB = VisitorDB.getInstance();
        this.params = params;
        this.accountDB = AccountDB.getInstance();
    }

    /**
     * Check the parameters to validate that the request is
     * @return If the parameters are correct
     */
    @Override
    public boolean checkParams() {
        String[] parts = params.split(DELIMITER);
        if (parts.length == 1) {
            //visitorID given
            if (parts[0].length() == 10) {
                visitorID = parts[0];
            }
            //visitorID not given
            else{
                visitorID = accountDB.getVisitorIDFromClientID(clientID);
            }
            return true;
        }
        return false;
    }

    /**
     * Get the name of the request
     * @return The name
     */
    @Override
    public String getName() {
        return ARRIVE_REQUEST;
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
        String response = clientID + DELIMITER + librarySystem.beginVisit(visitorID);
        String[] parts = response.split(",");
        //Only add successful beginVisit requests to account commandHistory
        if(parts.length == 5){
            addToCommandHistory(this, clientID);
        }
        //Library.beginVisit()->currentLibraryState.beginVisit()->
        return response;
    }

    /**
     * Undo a begin visit request
     */
    @Override
    public void undo(){
        //undo current (new visit with start time) field in VisitorID to null
        //remove visitor from currentVisitors in visitorDB
        startVisit = visitorDB.removeVisit(visitorID);
    }

    /**
     * Redo a begin visit request. Redoing beginVisit request preserves startTime.
     */
    @Override
    public void redo(){
        LocalDateTime startVisitTime = startVisit.getStart();
        visitorDB.beginVisit(visitorID, startVisitTime, timeKeeper.readDate(startVisitTime), timeKeeper.readTime(startVisitTime));
    }
}
