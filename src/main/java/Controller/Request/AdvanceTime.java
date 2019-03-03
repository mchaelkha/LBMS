package Controller.Request;

import Model.Library.LibrarySystem;

/**
 * WIP
 * Advances the time by a number of days and hours as requested by the client.
 * @author Jack Li
 */
public class AdvanceTime implements Request {
    /**
     * Message for missing parameters
     */
    private static final String PARAM_MESSAGE = String.format(MISSING_PARAM,
            ADVANCE_REQUEST) + DELIMITER + "number-of-days[,number-of-hours]";
    /**
     * Singleton timekeeper to keep time the same across the system.
     */
    private LibrarySystem library;
    /**
     * the parameters for the command.
     */
    private String params;
    /**
     * Days to add
     */
    private int days;
    /**
     * Hours to add
     */
    private int hours;

    /**
     * Creates a new AdvanceTime request with the given parameters.
     * @param params The parameters for the command.
     */
    public AdvanceTime(LibrarySystem library, String params) {
        this.library = library;
        this.params = params;
    }

    /**
     * Checks whether the parameters are valid for this command.
     * @return Whether or not the parameters are valid.
     */
    @Override
    public String checkParams() {
        String[] parts = params.split(DELIMITER);
        if (parts.length > 0) {
            days = Integer.parseInt(parts[0]);
            if (parts.length > 1) {
                hours = Integer.parseInt(parts[1]);
            }
            return PROPER_PARAM;
        }
        return PARAM_MESSAGE;
    }

    /**
     * Executes the AdvanceTime command to move the system time forward.
     * @return String indicating that the execution has succeeded.
     */
    @Override
    public String execute() {
        String check = checkParams();
        if (!check.equals(PROPER_PARAM)) {
            return check;
        }
        return library.advanceTime(days, hours);
    }
}
