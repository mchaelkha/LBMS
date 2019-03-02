package main.java.Controller.Request;

import main.java.Model.Visitor.VisitorDB;

/**
 * End visit request to start a visit for a visitor.
 *
 * @author Michael Kha
 */
public class EndVisit implements Request {

    /**
     * The visitor database of the library
     */
    private VisitorDB visitorDB;
    /**
     * Params in the command
     */
    private String params;
    /**
     * The visitor ID to start the visit for
     */
    private String visitorID;

    /**
     * Create a new end visit request given the visitor database
     * and the parameters for the request.
     * @param visitorDB The visitor database
     * @param params The parameters that follow a request command
     */
    public EndVisit(VisitorDB visitorDB, String params) {
        this.visitorDB = visitorDB;
        this.params = params;
    }

    /**
     * TODO: proper missing parameter checking
     * Check the parameters to validate that the request is
     * @return If the parameters are correct
     */
    @Override
    public String checkParams() {
        String[] parts = params.split(DELIMITER);
        if (parts.length == 1) {
            visitorID = parts[0];
        }
        return PARAM_COUNT;
    }

    /**
     * Execute the begin visit command which returns a string.
     * @return String indicating that the visit has ended and the end time
     *         and duration of the visit.
     */
    @Override
    public String execute() {
        String check = checkParams();
        if (!check.equals(PROPER_PARAM)) {
            return check;
        }
        // TODO: implement by calling right methods in DB
        return null;
    }
}
