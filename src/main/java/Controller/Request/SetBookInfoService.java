package Controller.Request;

import Model.Client.Service;
import Model.Client.AccountDB;

public class SetBookInfoService extends AccessibleRequest {
    /**
     * Message for missing parameters
     */
    private static final String PARAM_MESSAGE = String.format(MISSING_PARAM,
            SERVICE_REQUEST) + DELIMITER + "visitor ID,id[,ids]";
    private AccountDB accountDB;
    private String params;

    /**
     * Create a set book info service request with the given service.
     * @param clientID The client ID to set for
     * @param params The service to set to
     */
    public SetBookInfoService(String clientID, String params) {
        super(clientID, false);
        this.accountDB = AccountDB.getInstance();
        this.params = params;
    }

    /**
     * Check the parameters for validity.
     * @return If the params are correct
     */
    public boolean checkParams() {
        String[] parts = params.split(DELIMITER);
        if (parts.length != 1) {
            return false;
        }
        return Service.isService(params);
    }

    /**
     * Get the name of the request
     * @return The name
     */
    @Override
    public String getName() {
        return SERVICE_REQUEST;
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
