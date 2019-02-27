package Visitor;

import Library.TimeKeeper;
import Request.RequestUtil;

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
     * @param info The info needed to create a visitor
     * @return Response to user indicating registration went through or Error.
     */
    public String registerVisitor(String info) {
        //Create new visitorInfo object using info string
        VisitorInfo newVisitorInfo = new VisitorInfo(info);

        //check for duplicate visitorInfo
        for (String currentKey : registeredVisitors.keySet()) {
            //Duplicate visitorInfo found
            if (registeredVisitors.get(currentKey).equals(newVisitorInfo)) {
                return RequestUtil.ARRIVE_REQUEST + RequestUtil.DELIMITER + RequestUtil.DUPLICATE;
            }
        }
        //No duplicate was found. Register new visitor.
        int newVisitorID = nextVisitorID;
        nextVisitorID++;
        String newVisitorIDString = Integer.toString(newVisitorID);
        registeredVisitors.put(newVisitorIDString, newVisitorInfo);
        String registeredDate = TimeKeeper.readDate();
        return RequestUtil.REGISTER_REQUEST+RequestUtil.DELIMITER
                +newVisitorIDString+RequestUtil.DELIMITER+registeredDate;
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
