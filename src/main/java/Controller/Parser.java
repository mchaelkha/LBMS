package Controller;

import Controller.Request.Request;
import Controller.Request.RequestUtil;

/**
 * Parser interface to be used for the proxy pattern. Parsers process requests.
 *
 * @author Michael Kha
 */
public interface Parser extends RequestUtil {

    /**
     * Process the string into a request.
     * @param request Request to process
     * @return A request
     */
    Request processRequest(String request);

}
