package main.java.Model.Visitor;

import main.java.Controller.Request.RequestUtil;
import main.java.Model.Library.TimeKeeper;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * The visitor database that is used by the library to manage visitor commands
 * including beginVisit, endVisit, registerVisitor, and checkoutBook.
 *
 * @author Luis Gutierrez
 */
public class VisitorDB implements RequestUtil, Serializable{

    /**
     * All visitors that are registered in the library. (VisitorID, VisitorInfo)
     */
    private Map<String, VisitorInfo> registeredVisitors;

    /**
     * All current visitors in the library. (VisitorID, VisitorInfo)
     */
    private Map<String, VisitorInfo> currentVisitors;

    /**
     * Used for providing visitors with unique IDs
     */
    private int nextVisitorID;

    /**
     * First visitorID provided when library is created
     */
    private final int INITIAL_VISITOR_ID = 1000000000;

    /**
     * Max number of transactions for a visitor
     */
    private static int MAX_NUMBER_OF_TRANSACTIONS = 5;

    /**
     * Updated when Visitor begins a visit. Used to calculate visit duration
     */
    private LocalDateTime startVisitDayTime;

    /**
     * Create a new visitor database that is empty.
     */
    public VisitorDB() {
        registeredVisitors = new HashMap<>();
        currentVisitors = new HashMap<>();

        //initialize nextVisitorID (All IDs need to be a 10 digit number)
        nextVisitorID = INITIAL_VISITOR_ID;
    }

    /**
     * Register the visitor given properly formatted info.
     * The new visitor is added into the map of visitors.
     * @return Response to user indicating success of registration.
     */
    public String registerVisitor(String firstName, String lastName, String address, String phoneNumber) {
        //Create new visitorInfo object using params
        VisitorInfo newVisitorInfo = new VisitorInfo(firstName,lastName, address, phoneNumber);

        //check for duplicate visitorInfo
        for (String currentKey : registeredVisitors.keySet()) {
            //Duplicate visitorInfo found
            if (registeredVisitors.get(currentKey).equals(newVisitorInfo)) {
                return REGISTER_REQUEST + DELIMITER + DUPLICATE + TERMINATOR;
            }
        }
        //No duplicate was found. Register new visitor.
        int newVisitorID = nextVisitorID;
        nextVisitorID++;
        String newVisitorIDString = Integer.toString(newVisitorID);
        registeredVisitors.put(newVisitorIDString, newVisitorInfo);
        TimeKeeper timeKeeper = TimeKeeper.getInstance();
        String registeredDate = timeKeeper.readDate();
        return REGISTER_REQUEST+DELIMITER+newVisitorIDString
                +DELIMITER+registeredDate+TERMINATOR;
    }

    /**
     * Start a visit given the visitor id.
     * @param visitorID The visitor id to log a visit with
     */
    public String beginVisit(String visitorID) {
        //Check if visitor with id already exists in currentVisitors
        if (currentVisitors.containsKey(visitorID)) {
            //Response = "arrive,duplicate;"
            return ARRIVE_REQUEST+DELIMITER+DUPLICATE+TERMINATOR;
        }
        //Check if visitor has not been registered yet
        else if (!registeredVisitors.containsKey(visitorID)){
            //Response = "arrive,invalid-id;"
            return ARRIVE_REQUEST+DELIMITER+INVALID_ID+TERMINATOR;
        }
        //Add visitor to currentVisitors
        else{
            VisitorInfo visitor = registeredVisitors.get(visitorID);
            currentVisitors.put(visitorID, visitor);

            //Record visit date and time
            TimeKeeper timeKeeper = TimeKeeper.getInstance();
            String visitDate = timeKeeper.readDate();
            String visitStartTime = timeKeeper.readTime();

            //Get LocalDateTime for the startVisit time used for endVisit response
            startVisitDayTime = timeKeeper.getClock();

            //Response = "arrive,visitorID,visitDate,visitStartTime;"
            return ARRIVE_REQUEST+DELIMITER+visitorID+DELIMITER
                    +visitDate+DELIMITER+visitStartTime+TERMINATOR;
        }
    }

    /**
     * End a visit that is in progress given the visitor id.
     * @param visitorID The visitor id to end a visit for
     */
    public String endVisit(String visitorID) {
        //Check if visitorID is not in current visitors
        if(currentVisitors.containsKey(visitorID)){
            //Response = "arrive,invalid-id;"
            return DEPART_REQUEST+DELIMITER+INVALID_ID+TERMINATOR;
        }

        //Remove visitor from currentVisitors
        currentVisitors.remove(visitorID);

        //Response = "depart,visitorID,visitEndTime,visitDuration"
        TimeKeeper timeKeeper = TimeKeeper.getInstance();
        String visitEndTime = timeKeeper.readTime();

        LocalDateTime endVisitDateTime = timeKeeper.getClock();
        String visitDuration = timeKeeper.calculateDuration(startVisitDayTime, endVisitDateTime);

        return DEPART_REQUEST+DELIMITER+visitorID+DELIMITER+visitEndTime+
                DELIMITER+visitDuration+TERMINATOR;
    }

    /**
     * Clear all the current visitors that are logged.
     * Called when Library closes.
     */
    public void clearCurrentVisitors() {
        currentVisitors.clear();
    }

    /**
     * Check if there is a current visitor with visitorID
     * @param visitorID Visitor's ID
     * @return true if visitor is a current visitor
     */
    public boolean validCurrentVisitor(String visitorID) {
        return currentVisitors.containsKey(visitorID);
    }
}
