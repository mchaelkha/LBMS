package Request;

import Library.LibrarySystem;
import Library.TimeKeeper;

/**
 * WIP
 * Advances the time by a number of days and hours as requested by the client.
 * @author Jack Li
 */
public class AdvanceTime implements Request {
    /**
     * Singleton timekeeper to keep time the same across the system.
     */
    private TimeKeeper timeKeeper;
    /**
     * the parameters for the command.
     */
    private String params;

    /**
     * Creates a new AdvanceTime request with the given parameters.
     * @param params The parameters for the command.
     */
    public AdvanceTime(String params) {
        this.timeKeeper = TimeKeeper.getInstance();
        this.params = params;
    }

    /**
     * Checks whether the parameters are valid for this command.
     * @return Whether or not the parameters are valid.
     */
    @Override
    public String checkParams() {
        return "";
    }

    /**
     * Executes the AdvanceTime command to move the system time forward.
     * @return String indicating that the execution has succeeded.
     */
    @Override
    public String execute() {
        String[] arr = params.split(",");
        timeKeeper.addDays(Integer.parseInt(arr[1]));
        timeKeeper.addHours(Integer.parseInt(arr[2]));
        return "success";
    }
}
