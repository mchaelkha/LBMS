package Visitor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToDoubleBiFunction;

/**
 *
 */
public class VisitorDB {

    /**
     * All visitors that are registered in the library. (VisitorID, VisitorInfo)
     */
    private Map<String, VisitorInfo> registeredVisitors;

    /*
     * All current visitors in the library. (VisitorID, VisitorInfo)
     */
    private Map<String, VisitorInfo> currentVisitors;

    /**
     * Create a new visitor database that is empty.
     */
    public VisitorDB() {
        registeredVisitors = new HashMap<>();
        currentVisitors = new HashMap<>();
    }

    /**
     * Register the visitor given properly formatted info.
     * The new visitor is added into the map of visitors.
     * @param info The info needed to create a visitor
     */
    public String registerVisitor(String info) {
        //Create new visitorInfo object using info string
        VisitorInfo newVisitorInfo = new VisitorInfo(info);

        //might return visitorinfo
        //check for duplicate visitorInfo
        for (String currentKey : registeredVisitors.keySet()) {
            if (registeredVisitors.get(currentKey).equals(newVisitorInfo)) {
                //return RequestUtil.
            }
        }
        return "";
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
        else if (!registeredVisitors.containsKey(visitorID)){
            return "arrive,invalid-id";
        }
        //Add visitor to currentVisitors and update its state
        else{
            VisitorInfo visitor = registeredVisitors.get(visitorID);
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
     * Finds a visitor, determines whether or not they can
     * checkout a book, and then adds a book if they can.
     */
    public boolean checkoutBook(String visitorID) {
        return true;
    }
}
