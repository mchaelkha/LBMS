package main.java.Controller.Request;

import main.java.Model.Library.TimeKeeper;

/**
 * Gives the system's current date and time.
 * @author Jack Li
 */
public class CurrentDateTime implements Request {

    /**
     * Singleton timekeeper to keep time the same across the system.
     */
    private TimeKeeper timeKeeper;

    /**
     * Creates a new CurrentDateTime command.
     */
    public CurrentDateTime() {
        this.timeKeeper = TimeKeeper.getInstance();
    }

    /**
     * Checks whether the parameters are valid for this command.
     * @return Nothing, since this command does not require parameters.
     */
    @Override
    public String checkParams() {
        return "";
    }

    /**
     * Executes the CurrentDateTime command to return the current date and time.
     * @return The current date and time within the system.
     */
    @Override
    public String execute() {
        return timeKeeper.readTime() + "," + timeKeeper.readDate() + TERMINATOR;
    }
}
