package Controller.Request;

/**
 * Allows requests to know if they are undoable or employee only
 */
public abstract class RequestAbstract implements Request {
    private boolean undoable;
    private boolean employeeOnly;

    public RequestAbstract(boolean undoable, boolean employeeOnly) {
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
