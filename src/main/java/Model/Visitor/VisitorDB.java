package Model.Visitor;

import Controller.Request.RequestUtil;
import Model.Library.TimeKeeper;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * The visitor database that is used by the library to manage visitor commands
 * including beginVisit, endVisit, registerVisitor, and checkoutBook.
 *
 * @author Luis Gutierrez
 */
public class VisitorDB implements RequestUtil, Serializable{

    /**
     * Singleton instance
     */
    private static VisitorDB instance = null;

    /**
     * All visitors that are registered in the library. (VisitorID, VisitorInfo)
     */
    private Map<String, VisitorInfo> registeredVisitors;

    /**
     * All current visitors in the library. (VisitorID, VisitorInfo)
     */
    private Map<String, VisitorInfo> currentVisitors;

    /**
     * List of visit lengths to calculate average visit length for StatisticReports
     */
    private List<Long> visitLengths;

    /**
     * Used for providing visitors with unique IDs
     */
    private int nextVisitorID;

    /**
     * Used to keep track of unique 10 digit generated ids
     */
    private final int INITIAL_VISITOR_ID = 1000000000;

    /**
     * Create a new visitor database that is empty.
     */
    private VisitorDB() {
        registeredVisitors = new HashMap<>();
        currentVisitors = new HashMap<>();
        visitLengths = new ArrayList<>();
        nextVisitorID = INITIAL_VISITOR_ID;
    }

    public static VisitorDB getInstance() {
        if (instance == null) {
            instance = new VisitorDB();
        }
        return instance;
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

            visitor.startVisit(startVisitDayTime);
            //this.startVisitDayTime = startVisitDayTime;

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
        VisitorInfo visitor = currentVisitors.remove(visitorID);
        LocalDateTime start = visitor.getVisitStart();

        //Record visit duration for ReportGenerator
        visitLengths.add(TimeKeeper.calculateDurationMillis(start, endVisitDateTime));

        //Response = "depart,visitorID,visitEndTime,visitDuration"
        String visitDuration = TimeKeeper.calculateDuration(start, endVisitDateTime);

        return DEPART_REQUEST+DELIMITER+visitorID+DELIMITER+visitEndTime+
                DELIMITER+visitDuration+TERMINATOR;
    }

    /**
     * Clear all the current visitors that are logged. Called when Library closes.
     * @param end The local date time when the visitors are leaving
     */
    public void clearCurrentVisitors(LocalDateTime end) {
        // End the visits for all current visitors upon closing
        VisitorInfo visitor;
        LocalDateTime start;
        for (String visitorID : currentVisitors.keySet()) {
            visitor = currentVisitors.get(visitorID);
            start = visitor.getVisitStart();
            visitor.endVisit(end);
            visitLengths.add(TimeKeeper.calculateDurationMillis(start, end));
        }
        // Clear outside of iterator
        currentVisitors.clear();
    }

    /**
     * Clear visitLengths. Called when library closes.
     */
    public void clearVisitLengths() {
        visitLengths.clear();
    }

    /**
     * Check if there is a current visitor with visitorID
     * @param visitorID Visitor's ID
     * @return true if visitor is a current visitor
     */
    public boolean validCurrentVisitor(String visitorID) {
        return currentVisitors.containsKey(visitorID);
    }

    public boolean validRegisteredVisitor(String visitorID) {
        return registeredVisitors.containsKey(visitorID);
    }

    /**
     * Helper method for ReportGenerator to get current number of registered visitors
     */
    public int getNumRegisteredVisitors(){
        return registeredVisitors.size();
    }

    /**
     * Helper method for ReportGenerator to get average length of visit
     * @return average length of visit
     */
    public long getAverageLengthVisit(){
        if (visitLengths.isEmpty()) {
            return 0;
            //return TimeKeeper.calculateDurationString(0);
        }
        long averageLengthVisit = 0;
        for (Long visitLength : visitLengths) {
            averageLengthVisit += visitLength;
        }
        averageLengthVisit = averageLengthVisit/(visitLengths.size());
        return averageLengthVisit;
        //return TimeKeeper.calculateDurationString(averageLengthVisit);
    }

    /**
     * Removes visitor with visitorID from currentVisitors
     * Called by beginVisit request undo() method
     * @param visitorID Visitor being removed
     */
    public Visit removeVisit(String visitorID) {
        //Grab visit object started from beginVisit request
        Visit startVisit = currentVisitors.get(visitorID).clearCurrentVisit();
        currentVisitors.remove(visitorID);
        return startVisit;
    }

    /**
     * Adds visitor
     * @param visitorID
     * @return
     */
    public void addVisit(String visitorID) {

    }
}
