package Model.Visitor;

import Controller.Request.RequestUtil;
import Model.Library.TimeUtil;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * The visitor database that is used by the library to manage visitor commands
 * including beginVisit, endVisit, registerVisitor, and checkoutBook.
 *
 * @author Luis Gutierrez
 */
public class VisitorDB implements RequestUtil, TimeUtil, Serializable{

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
     * Used to keep track of unique 10 digit generated ids
     */
    private Set<Integer> uniqueVisitorIds;

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

        //initialize uniqueVisitorIds (All IDs need to be unique 10 digit numbers)
        uniqueVisitorIds = new HashSet<>();
    }

    /**
     * Register the visitor given properly formatted info.
     * The new visitor is added into the map of visitors.
     * @return Response to user indicating success of registration.
     */
    public String registerVisitor(String firstName, String lastName, String address, String phoneNumber, String registeredDate) {
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
        //int newVisitorID = generateVisitorID();
        int newVisitorID = nextVisitorID;
        nextVisitorID++;
        String newVisitorIDString = Integer.toString(newVisitorID);
        registeredVisitors.put(newVisitorIDString, newVisitorInfo);

        return REGISTER_REQUEST+DELIMITER+newVisitorIDString
                +DELIMITER+registeredDate+TERMINATOR;
    }

    /**
     * Start a visit given the visitor id.
     * @param visitorID The visitor id to log a visit with
     */
    public String beginVisit(String visitorID, LocalDateTime startVisitDayTime, String visitDate, String visitStartTime) {
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

            this.startVisitDayTime = startVisitDayTime;

            //Response = "arrive,visitorID,visitDate,visitStartTime;"
            return ARRIVE_REQUEST+DELIMITER+visitorID+DELIMITER
                    +visitDate+DELIMITER+visitStartTime+TERMINATOR;
        }
    }

    /**
     * End a visit that is in progress given the visitor id.
     * @param visitorID The visitor id to end a visit for
     */
    public String endVisit(String visitorID, LocalDateTime endVisitDateTime, String visitEndTime) {
        //Check if visitorID is not in current visitors
        if(!currentVisitors.containsKey(visitorID)){
            //Response = "arrive,invalid-id;"
            return DEPART_REQUEST+DELIMITER+INVALID_ID+TERMINATOR;
        }

        //Remove visitor from currentVisitors
        currentVisitors.remove(visitorID);

        //Response = "depart,visitorID,visitEndTime,visitDuration"
        String visitDuration = calculateDuration(startVisitDayTime, endVisitDateTime);

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

    /**
     * Generate unique random id
     * @return unique 10 digit id
     */
    public int generateVisitorID(){
        Random random = new Random();
        int id = random.nextInt(999999999)+1000000000;
        //Check if id is already in use
        if(uniqueVisitorIds.contains(id)){
            generateVisitorID();
        }
        return id;

    }
}
