package Model.Client;

import Controller.Request.AccessibleRequest;
import Controller.Request.RequestUtil;
import Controller.Request.Request;

/**
 * Implementation of the state pattern for the purpose of allowing different
 * behavior between visitor and employee requests.
 *
 * @author Michael Kha
 */
public interface Role extends RequestUtil {
    public String executeRequest(AccessibleRequest request);
}
