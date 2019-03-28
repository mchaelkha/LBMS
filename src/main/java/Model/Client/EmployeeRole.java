package Model.Client;

import Controller.Request.Request;
/**
 * The implementation of the Employee role has permissions to run every command, 
 * so it simply executes the command it is given.
 * @author Hersh Nagpal
 */
public class EmployeeRole implements Role {
    @Override
    public String executeRequest(AccessibleRequest request) {
        return request.execute();
    }
}
