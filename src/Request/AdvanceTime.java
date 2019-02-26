package Request;

import Library.LibrarySystem;
import Library.TimeKeeper;

/**
 * WIP
 * Advances the time by a number of days and hours as requested by the client.
 * @author Jack Li
 */
public class AdvanceTime implements Request {
    private TimeKeeper timeKeeper;
    private String params;

    public AdvanceTime(TimeKeeper timeKeeper, String params) {
        this.timeKeeper = timeKeeper;
        this.params = params;
    }

    @Override
    public String checkParams() {
        return "";
    }

    @Override
    public String execute() {
        return null;
    }
}
