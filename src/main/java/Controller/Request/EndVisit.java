package Controller.Request;

import Model.Client.AccountDB;
import Model.Library.TimeKeeper;
import Model.Visitor.Visit;
import Model.Visitor.VisitorDB;

import java.time.LocalDateTime;

/**
 * End visit request to start a visit for a visitor.
 *
 * @author Michael Kha
 */
public class EndVisit extends AccessibleRequest {
    /**
     * Message for missing parameters
     */
    private static final String PARAM_MESSAGE = String.format(MISSING_PARAM,
            ARRIVE_REQUEST) + DELIMITER + "visitor ID";
    /**
     * Visitor database used to update currentVisitors by removing visitor ending their visit.
     */
    private VisitorDB visitorDB;
    /**
     * Account database to retrieve visitor ID from account if not provided
     */
    private AccountDB accountDB;
    /**
     * TimeKeeper used to build response to user by including the simulation date and time,
     * which represent the time the visitor left the library.
     */
    private TimeKeeper timeKeeper;
    /**
     * Params in the command
     */
    private String params;
    /**
     * The visitor ID to start the visit for
     */
    private String visitorID;
    /**
     * The endVisit object to hold the end time for redoing this request
     */
    private Visit endVisit;

    /**
     * Create a new end visit request given the visitor database
     * and the parameters for the request.
     * TODO finish commenting request, LBServer, request parser classes
     * @param timeKeeper TimeKeeper
     * @param clientID The client making the request
     * @param params The parameters that follow a request command
     */
    public EndVisit(TimeKeeper timeKeeper, String clientID, String params) {
        super(clientID, false);
        this.visitorDB = VisitorDB.getInstance();
        accountDB = AccountDB.getInstance();
        this.timeKeeper = TimeKeeper.getInstance();
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
        return DEPART_REQUEST;
    }

    /**
     * Execute the begin visit command which returns a string.
     * @return String indicating that the visit has ended and the end time
     *         and duration of the visit.
     */
    @Override
    public String execute() {
        if (!checkParams()) {
            return clientID + DELIMITER + PARAM_MESSAGE;
        }
        String response = clientID + DELIMITER + visitorDB.endVisit(visitorID,
                timeKeeper.getClock(), timeKeeper.readTime());
        String[] parts = response.split(",");
        //Only add successful endVisit requests to account commandHistory
        if(parts.length == 4){
            addToCommandHistory(this, clientID);
        }
        return response;
    }

    /**
     * Undo an end visit request
     */
    @Override
    public void undo(){
        //get last visit added to visitorInfo visits and clear its
        //endTime and make it current.

        //add visitor back to currentVisitors in visitorDB
        //endTime is cleared -> store visit object containing end time in this request for redo
        endVisit = visitorDB.addVisit(visitorID);
    }

    /**
     * Redo an end visit request. Redoing endVisit request preserves startTime.
     */
    @Override
    public void redo(){
        LocalDateTime endVisitTime = endVisit.getEnd();
        visitorDB.endVisit(visitorID, endVisitTime, timeKeeper.readDate(endVisitTime));
    }
}
