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

    public AdvanceTime(String params) {
        this.timeKeeper = TimeKeeper.getInstance();
        this.params = params;
    }

    @Override
    public String checkParams() {
        return "";
    }

    @Override
    public String execute() {
        String[] arr = params.split(",");
        timeKeeper.addDays(Integer.parseInt(arr[1]));
        timeKeeper.addHours(Integer.parseInt(arr[2]));
        return "success";
    }
}
