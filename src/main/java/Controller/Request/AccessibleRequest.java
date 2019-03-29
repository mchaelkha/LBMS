package Controller.Request;

/**
 * Allows requests to know if they are undoable or employee only
 */
public abstract class AccessibleRequest implements Request {

    /**
     * The client ID that made the accessible request
     */
    String clientID;
    /**
     * If the request can only be performed by an employee and not a visitor
     */
    private boolean employeeOnly;

    /**
     * The accessible request which requires a client ID and its access
     * privileges.
     * @param clientID The client that made the request
     * @param employeeOnly If only employees have access to execute
     */
    public AccessibleRequest(String clientID, boolean employeeOnly) {
        this.clientID = clientID;
        this.employeeOnly = employeeOnly;
    }

    /**
     * Get the client ID that made the request
     * @return The client ID
     */
    @Override
    public String getClientID() {
        return clientID;
    }

    /**
     * Check the access privileges of the request.
     * @return If only employees can execute
     */
    public boolean isEmployeeOnly() {
        return employeeOnly;
    }
}
