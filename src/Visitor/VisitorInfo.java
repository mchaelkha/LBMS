package Visitor;

import java.util.Map;

/**
 *
 */
public class VisitorInfo {

    /**
     * All visits that a visitor has made
     */
    private Map<String, String> visits;
    /**
     * Identifying information for a visitor including name, address, and
     * phone number
     */
    private String[] info;
    /**
     * The unique visitor id
     */
    private String visitorID;

    /**
     * Create visitor info given a line of info.
     * @param info Correctly formatted info
     */
    public VisitorInfo(String info) {
        // break whole string to each part of info
    }

    /**
     * Record a visit given a date.
     * @param date The date at the start of the visit
     */
    public void startVisit(String date) {

    }

    /**
     * End the last visit given a date.
     * @param date The date at the end of the visit
     */
    public void endVisit(String date) {

    }

    /**
     * Get the first name.
     * @return First name
     */
    public String getFirstName() {
        return null;
    }

    /**
     * Get the last name.
     * @return Last name
     */
    public String getLastName() {
        return null;
    }

    /**
     * Get the address.
     * @return Address
     */
    public String getAddress() {
        return null;
    }

    /**
     * Get the phone number.
     * @return Phone number
     */
    public String getPhoneNumber() {
        return null;
    }
}
