package Controller.Request;

public interface Request extends RequestUtil {
    default boolean checkParams() {
        return true;
    }
    String execute();
    default String undo() {
        return UNDO_REQUEST + DELIMITER + "cannot-undo" + TERMINATOR;
    }
    default String redo() {
        return REDO_REQUEST + DELIMITER + "cannot-redo" + TERMINATOR;
    }
}
