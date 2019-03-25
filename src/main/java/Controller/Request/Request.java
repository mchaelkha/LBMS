package Controller.Request;

public interface Request extends RequestUtil {
    default boolean checkParams() {
        return true;
    }
    String execute();
    default void undo() {

    }
    default void redo(){

    }
}
