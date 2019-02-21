package Library;

import Request.Request;

public class LibrarySystem {
    private String openTime;
    private String closeTime;
    private String currentMonth;
    private DBManager manager;

    /**
     * Gives the status of the library
     * @return Whether the library is open.
     */
    public boolean isOpen(){
        return false;
    }

    /**
     * Handles requests from the client
     * @param request The request being made.
     */
    public void handleRequest(Request request){

    }

    /**
     * Moves the date forward by a certain number of days.
     * @param days The number of days to move forward.
     */
    public void moveDate(int days){

    }

    /**
     * Compiles the month's information.
     * @param month
     */
    private void generateInfoReport(String month){

    }
}
