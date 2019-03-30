package Controller.Request;

/**
 * Illegal request to represent any command that does not exist.
 *
 * @author Michael Kha
 */
public class Illegal implements Request {

    /**
     * Get the name of the request
     * @return The name
     */
    @Override
    public String getName() {
        return ILLEGAL_COMMAND;
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
