package Model.Client;

import Controller.Request.RequestUtil;

/**
 * Implementation of the state pattern for the purpose of allowing different
 * behavior between visitor and employee requests.
 *
 * @author Michael Kha
 */
public interface Role extends RequestUtil {
    public String executeCommand();
}
