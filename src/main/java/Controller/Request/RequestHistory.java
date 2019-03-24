package Controller.Request;

import java.util.Stack;

/**
 * Hold Stacks containing previously performed and undone commands. Part of undo/redo functionality
 */
public class RequestHistory {
    private Stack<Request> performedRequests;
    private Stack<Request> undoneRequests;

    public RequestHistory() {
        performedRequests = new Stack<>();
        undoneRequests = new Stack<>();
    }

    public void addPerformedRequest(Request request) {
        performedRequests.push(request);

        //Clear undoneCommands to prevent undo of further commands
        if (!undoneRequests.empty()) {
            undoneRequests.clear();
        }
    }

    public void addUndoneCommand(Request request) {
        undoneRequests.push(request);
    }

    public Request getRequestToUndo(Request request) {
        return performedRequests.pop();
    }

    public Request getRequestToRedo(Request request) {
        return undoneRequests.pop();
    }
}
