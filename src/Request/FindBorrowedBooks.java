package Request;

import Visitor.VisitorDB;

public class FindBorrowedBooks implements Request {

    private VisitorDB visitorDB;
    private String params;

    public FindBorrowedBooks(VisitorDB visitorDB, String params) {
        this.visitorDB = visitorDB;
        this.params = params;
    }

    @Override
    public String checkParams() {
        String[] parts = params.split(DELIMITER);
        if (parts.length == 1) {
            return PROPER_PARAM;
        }
        return PARAM_COUNT;
    }

    @Override
    public String execute() {
        String check = checkParams();
        if (check.equals(PROPER_PARAM)) {

        }
        return check;
    }
}
