package Request;

import Visitor.VisitorDB;

public class RegisterVisitor implements Request{

    private VisitorDB visitorDB;
    private String params;

    public RegisterVisitor(VisitorDB visitorDB, String params) {
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
