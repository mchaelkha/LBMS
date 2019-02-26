package Request;

import Visitor.VisitorDB;

public class BeginVisit implements Request {

    private VisitorDB visitorDB;
    private String params;

    public BeginVisit(VisitorDB visitorDB, String params) {
        this.visitorDB = visitorDB;
        this.params = params;
    }

    @Override
    public String checkParams() {
        return "";
    }

    @Override
    public String execute() {
        return null;
    }
}
