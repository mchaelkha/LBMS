package Controller.Request;

import Model.Library.TimeKeeper;

/**
 * Gives the system's current date and time.
 * @author Jack Li
 */
public class CurrentDateTime extends AccessibleRequest {

    /**
     * Used to build a response returned to the user including the simulation's date and time.
     */
    private TimeKeeper timeKeeper;

    /**
     * Creates a new CurrentDateTime command.
     */
    public CurrentDateTime(TimeKeeper timeKeeper, String clientID) {
        super(clientID, false);
        this.timeKeeper = timeKeeper;
    }

    /**
     * Get the name of the request
     * @return The name
     */
    @Override
    public String getName() {
        return DATE_TIME_REQUEST;
    }

    /**
     * Executes the CurrentDateTime command to return the current date and time.
     * @return The current date and time within the system.
     */
    @Override
    public String execute() {
        return clientID + DELIMITER + timeKeeper.readTime() +
                DELIMITER + timeKeeper.readDate() + TERMINATOR;
    }
}
