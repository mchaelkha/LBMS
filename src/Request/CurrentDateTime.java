package Request;

import Library.TimeKeeper;

public class CurrentDateTime implements Request {

    private TimeKeeper timeKeeper;
    private String params;

    public CurrentDateTime() {
        this.timeKeeper = TimeKeeper.getInstance();
        //this.params = params;
    }

    @Override
    public String checkParams() {
        return "";
    }

    @Override
    public String execute() {
        return timeKeeper.readTime() + "," + timeKeeper.readDate();
    }
}
