package Controller.Request;

import Model.Library.LibrarySystem;

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
    public BeginVisit(LibrarySystem librarySystem, String params) {
        this.librarySystem = librarySystem;
        this.params = params;
    }

    /**
     * Check the parameters to validate that the request is
     * @return If the parameters are correct
     */
    @Override
    public String checkParams() {
        String[] parts = params.split(DELIMITER);
        if (parts.length == 1) {
            visitorID = parts[0];
            return PROPER_PARAM;
        }
        return PARAM_MESSAGE;
    }

    /**
     * Execute the begin visit command which returns a string.
     * @return String indicating that the visit has started successfully
     */
    @Override
    public String execute() {
        String check = checkParams();
        if (!check.equals(PROPER_PARAM)) {
            return check;
        }
        return librarySystem.beginVisit(visitorID);
    }
}
