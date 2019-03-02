package Controller.Request;

/**
 * Missing parameter exception to be used when checking parameters.
 *
 * @author Michael Kha
 */
public class MissingParamException extends Exception {

    /**
     * Create a missing parameter exception
     * @param message Message displaying what params are missing
     */
    public MissingParamException(String message) {
        super(message);
    }
}
