package main.java.Model.Visitor;

import main.java.Model.Checkout.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a registered visitor in the Library System
 */
public class VisitorInfo {

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
     * All visits that a visitor has made
     */
    private Map<String, String> visits;
    /**
     * Identifying information for a visitor including name, address, and
     * phone number
     */
    private String[] visitorInformation;
    /**
     * The unique visitor id
     */
    private int visitorID;

    /**
     * Checkout transactions
     */
    private List<Transaction> transactionList;

    /**
     * Create visitor info given a line of info.
     * @param info Correctly formatted info
     */
    public VisitorInfo(String info) {
        // break whole string to each part of info
        visitorInformation = info.split(",");
        firstName = visitorInformation[0];
        lastName = visitorInformation[1];
        address = visitorInformation[2];
        phoneNumber = visitorInformation[3];
        transactionList = new ArrayList<>();
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

    public int getNumberOfTransactions(){
        return transactionList.size();
    }

    public void addTransaction(Transaction transaction){
        transactionList.add(transaction);
    }

    public void removeTransaction(Transaction transaction) {
        transactionList.remove(transaction);
    }

    public List<Transaction> getTransactionList(){
        return transactionList;
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
