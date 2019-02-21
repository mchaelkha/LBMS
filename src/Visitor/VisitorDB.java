package Visitor;

import java.util.HashMap;
import java.util.Map;

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
    }

    /**
     * Start a visit given the visitor id.
     * @param visitorID The visitor id to log a visit with
     */
    public void startVisit(String visitorID) {

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

}
