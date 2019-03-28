package Model.Client;

import Controller.Request.AccessibleRequest;

/**
 * Visitors must check to ensure that they have the sufficient 
 * permissions to perform their requests.
 * @author Hersh Nagpal
 */
public class VisitorRole implements Role {

    public String executeRequest(AccessibleRequest request) {
        if(request.isEmployeeOnly()) {
            return NOT_AUTHORIZED;
        } else {
            return request.execute();
        }
    }   
}
