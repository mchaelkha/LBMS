package Controller.Request;

import Model.Client.AccountDB;

public interface Request extends RequestUtil {
    /**
     * Get the name of the request
     * @return The name
     */
    String getName();
    /**
     * Execute the request
     * @return The response of the request
     */
    String execute();
    default String getClientID() {
        return null;
    }
    default boolean isEmployeeOnly() {
        return false;
    }
    default boolean hasClientID() {
        return false;
    }
    default boolean checkParams() {
        return true;
    }
    default void undo() {

    }
    default void redo(){

    }
    default void addToCommandHistory(Request request, String clientID) {
        AccountDB accountDB = AccountDB.getInstance();
        if (!accountDB.isActiveAccount(clientID)) {
            return;
        }
        accountDB.addToCommandHistory(request, clientID);
    }
    default void addToUndoHistory(Request request, String clientID) {
        AccountDB accountDB = AccountDB.getInstance();
        if (!accountDB.isActiveAccount(clientID)) {
            return;
        }
        accountDB.addToUndoHistory(request, clientID);
    }
}
