package Request;

public interface RequestUtil {
    String DELIMITER = ",";
    String TERMINATOR = ";";
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
    String PROPER_PARAM = "";

    String DUPLICATE = "duplicate";
    String SUCCESS = "success";
    String NEW_LINE = System.lineSeparator();
}
