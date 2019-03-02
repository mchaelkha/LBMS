package main.java.Controller.Request;

/**
 * Illegal request to represent any command that does not exist.
 *
 * @author Michael Kha
 */
public class Illegal implements Request {

    /**
     * @deprecated  Not used for illegal requests
     */
    @Deprecated
    @Override
    public String checkParams() {
        return "";
    }

    /**
     * Execute the illegal request by displaying a message.
     * @return An illegal request message
     */
    @Override
    public String execute() {
        return ILLEGAL_COMMAND;
    }
}
