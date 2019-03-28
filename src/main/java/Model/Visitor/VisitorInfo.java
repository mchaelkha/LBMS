package Model.Visitor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a registered visitor in the Library System
 *
 * @author Luis Gutierrez
 */
public class VisitorInfo implements Serializable {

    /**
     * Visitor's first name
     */
    private String firstName;
    /**
     * Visitor's last name
     */
    private String lastName;
    /**
     * Visitor's address
     */
    private String address;
    /**
     * Visitor's phone number
     */
    private String phoneNumber;
    /**
     * All visits that a visitor has made from start to end
     */
    private List<Visit> visits;
    /**
     * Current visit tracked by the start
     */
    private Visit current;

    /**
     * Set visitor info.
     */
    public VisitorInfo(String firstName, String lastName, String address, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        visits = new ArrayList<>();
    }

    /**
     * Set the current starting time of a visit.
     * @param start The start of a visit
     */
    public void startVisit(LocalDateTime start) {
        current = new Visit(start);
    }

    /**
     * End a current visit by adding the visit's start and end to the visits.
     * @param end The date of visiting
     */
    public void endVisit(LocalDateTime end) {
        current.end(end);
        visits.add(current);
        current = null;
    }

    /**
     * Get the start of the visit
     * @return The time and date of the start of the visit
     */
    public LocalDateTime getVisitStart() {
        return current.getStart();
    }

    /**
     * Get the first name.
     * @return First name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get the last name.
     * @return Last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Get the address.
     * @return Address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Get the phone number.
     * @return Phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Used by undo to clear visit that was started by this visitor
     */
    public Visit clearCurrentVisit(){
        Visit currentCopy = current;
        current = null;
        return currentCopy;
    }


    /**
     * Used to check for duplicate visitors in library.
     * @param o Visitor being compared to this
     * @return true if visitor "o" is equal to this
     */
    @Override
    public boolean equals(Object o){
        if (o == this) {
            return true;
        }

        if (!(o instanceof VisitorInfo)) {
            return false;
        }

        VisitorInfo v = (VisitorInfo) o;
        return v.getFirstName().equals(firstName) &&
                v.getLastName().equals(lastName) &&
                v.getAddress().equals(address) &&
                v.getPhoneNumber().equals(phoneNumber);
    }
}
