package Request;

import Library.LibrarySystem;

/**
 * WIP
 * Advances the time by a number of days and hours as requested by the client.
 * @author Jack Li
 */
public class AdvanceTime implements Request {
    private LibrarySystem library = new LibrarySystem();

    @Override
    public boolean checkParams(String params) {
        return false;
    }

    @Override
    public String execute() {
        return null;
    }
}
