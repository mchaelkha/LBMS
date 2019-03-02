package Request;

/**
 * Interface to store constants for classes that need Request strings.
 */
public interface RequestUtil {
    String DELIMITER = ",";
    String TERMINATOR = ";";
    String IGNORE = "*";
    String PARTIAL_REQUEST = "partial-request";
    String REGISTER_REQUEST = "register";
    String ARRIVE_REQUEST = "arrive";
    String DEPART_REQUEST = "depart";
    String INFO_REQUEST = "info";
    String BORROW_REQUEST = "borrow";
    String BORROWED_REQUEST = "borrowed";
    String RETURN_REQUEST = "return";
    String PAY_REQUEST = "pay";
    String SEARCH_REQUEST = "search";
    String BUY_REQUEST = "buy";
    String ADVANCE_REQUEST = "advance";
    String DATE_TIME_REQUEST = "datetime";
    String REPORT_REQUEST = "report";

    String PARAM_COUNT = "Too little or too many params";
    // Use String.format to insert request name
    String MISSING_PARAM = "<%s>" + DELIMITER + "missing-parameters";
    String PROPER_PARAM = "";
    String ILLEGAL_COMMAND = "illegal-command";
    String DUPLICATE = "duplicate";
    String INVALID_ID = "invalid-id";
    String INVALID_VISITOR_ID = "invalid-visitor-id";
    String SUCCESS = "success";
    String CLOSED_LIBRARY = "closed-library";
    String NEW_LINE = System.lineSeparator();

}
