package Controller.Request;

/**
 * Allows requests to know if they are undoable or employee only
 */
public abstract class AccessibleRequests implements Request {
    private boolean undoable;
    private boolean employeeOnly;

    public AccessibleRequests(boolean undoable, boolean employeeOnly) {
        this.undoable = undoable;
        this.employeeOnly = employeeOnly;
    }

    public boolean isUndoable() {
        return undoable;
    }

    public boolean isEmployeeOnly() {
        return employeeOnly;
    }
}
