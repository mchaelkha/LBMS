package Controller.Request;

/**
 * Simple request to be used for encapsulating a response string
 * to be executed when needed.
 *
 * @author Michael Kha
 */
public class Simple implements Request {

    /**
     * The response to send when executed
     */
    private String response;

    /**
     * Create a simple request that holds a given response.
     * @param response The response to hold
     */
    public Simple(String response) {
        this.response = response;
    }

    /**
     * Execute the simple request by sending the response string.
     * @return The predetermined response
     */
    @Override
    public String execute() {
        return response;
    }

}
