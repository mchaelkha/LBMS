package Model.Client;

import Controller.Request.Request;
import Controller.Request.RequestUtil;

public class VisitorRole implements Role {

    public String executeRequest(Request request) {
        if(request.isEmployeeOnly()) {
            return NOT_AUTHORIZED;
        } else {
            return request.execute();
        }
    }   
}
