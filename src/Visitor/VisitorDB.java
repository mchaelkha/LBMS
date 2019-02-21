package Visitor;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class VisitorDB {

    private Map<String, VisitorInfo> visitors;

    private Map<String, VisitorInfo> currentVisitors;

    public VisitorDB() {
        visitors = new HashMap<>();
        currentVisitors = new HashMap<>();
    }

    public void registerVisitor(String info) {

    }

    public void startVisit(String visitorID) {

    }

    public void endVisit(String visitorID) {

    }

    public void clearCurrentVisitors() {

    }

}
