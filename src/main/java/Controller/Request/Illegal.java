package Controller.Request;

/**
 * Illegal request to represent any command that does not exist.
 *
 * @author Michael Kha
 */
public class Illegal implements Request {

    /**
     * Execute the illegal request by displaying a message.
     * @return An illegal request message
     */
    @Override
    public String execute() {
        return ILLEGAL_COMMAND;
    }
}
