package Visitor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToDoubleBiFunction;

/**
 *
 */
public class VisitorDB {

    /**
     * All visitors that have been logged
     */
    private Map<String, VisitorInfo> visitors;
    /**
     * All current visitors in the library
     */
    private Map<String, VisitorInfo> currentVisitors;

    /**
     * Create a new visitor database that is empty.
     */
    public VisitorDB() {
        visitors = new HashMap<>();
        currentVisitors = new HashMap<>();
    }

    /**
     * Register the visitor given properly formatted info.
     * The new visitor is added into the map of visitors.
     * @param info The info needed to create a visitor
     */
    public void registerVisitor(String info) {
        //might return visitorinfo
        //check for duplicate visitorInfo
    }

    /**
     * Start a visit given the visitor id.
     * @param visitorID The visitor id to log a visit with
     */
    public String startVisit(String visitorID) {
        //Check if visitor with id already exists in currentVisitors
        if (currentVisitors.containsKey(visitorID)) {
            return "arrive,duplicate";
        }
        //Check if visitor has not registered yet
        else if (!visitors.containsKey(visitorID)){
            return "arrive,invalid-id";
        }
        //Add visitor to currentVisitors and update its state
        else{
            VisitorInfo visitor = visitors.get(visitorID);
            currentVisitors.put(visitorID, visitor);
            return "arrive";
            //TODO return correct string with visit date and start time
        }
        //get visitorInfo and update new state
    }

    /**
     * End a visit that is in progress given the visitor id.
     * @param visitorID The visitor id to end a visit for
     */
    public void endVisit(String visitorID) {

    }

    /**
     * Clear all the current visitors that are logged.
     */
    public void clearCurrentVisitors() {

    }

    /**
     * Gets the VisitorInfo object
     */
    public void getVisitor(String visitorID){

    }

}
