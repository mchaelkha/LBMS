package Controller.Request;

import Model.Client.AccountDB;

public interface Request extends RequestUtil {
    default boolean checkParams() {
        return true;
    }
    String execute();
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
