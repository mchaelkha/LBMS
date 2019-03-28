package Model.Client;

import Controller.Request.Request;

public class EmployeeRole implements Role {
    @Override
    public String executeCommand(Request request) {
        return request.execute();
    }
}
