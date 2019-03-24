package Controller.Request;

import Model.Book.Service;
import Model.Client.AccountDB;

public class SetBookInfoService implements Request {
    /**
     * Message for missing parameters
     */
    private static final String PARAM_MESSAGE = String.format(MISSING_PARAM,
            SERVICE_REQUEST) + DELIMITER + "visitor ID,id[,ids]";
    private AccountDB accountDB;
    private String clientID;
    private String params;

    /**
     * Create a set book info service request with the given service.
     * @param clientID The client ID to set for
     * @param params The service to set to
     */
    public SetBookInfoService(String clientID, String params) {
        this.accountDB = AccountDB.getInstance();
        this.clientID = clientID;
        this.params = params;
    }

    /**
     * Check the parameters for validity.
     * @return
     */
    public boolean checkParams() {
        String[] parts = params.split(DELIMITER);
        if (parts.length != 1) {
            return false;
        }
        return Service.isService(params);
    }

    /**
     * Perform set book info service request by first checking if the
     * service is correct or not and then executing.
     * @return Response string representing an error or success
     */
    @Override
    public String execute() {
        if (!checkParams()) {
            return clientID + DELIMITER + PARAM_MESSAGE;
        }
        Service service = params.equals(Service.LOCAL.getService()) ? Service.LOCAL : Service.GOOGLE;
        return accountDB.setBookInfoService(clientID, service);
    }

}
