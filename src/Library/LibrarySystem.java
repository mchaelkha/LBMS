/**
 * 
 * @author Hersh Nagpal
 */
package Library;

import Request.Request;

public class LibrarySystem {
    private static int OPEN_HOUR = 9;
    private static int CLOSE_HOUR = 12+9;

    private TimeKeeper timeKeeper;

    /**
     * Gives the status of the library
     * @return Whether the library is open.
     */
    public boolean isOpen() {
        return timeKeeper.isLibraryOpen(OPEN_HOUR, CLOSE_HOUR);
    }

    /**
     * Moves the date forward by a certain number of days.
     * @param days The number of days to move forward.
     */
    public void moveDate(int days){

    }
}
