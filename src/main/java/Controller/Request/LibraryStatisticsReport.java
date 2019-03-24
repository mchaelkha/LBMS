package Controller.Request;

import Model.Library.ReportGenerator;

/**
 * Library statistics report request to get the statistics of the library
 * for some number of days back or the start.
 *
 * @author Michael Kha
 */
public class LibraryStatisticsReport implements Request{
    /**
     * Message for missing parameters
     */
    private static final String PARAM_MESSAGE = String.format(MISSING_PARAM,
            REPORT_REQUEST) + DELIMITER + "[,days]";
    /**
     * Responsible for the creation of statistical reports
     */
    private ReportGenerator reportGenerator;
    /**
     * The client that made this request
     */
    private String clientID;
    /**
     * Params in the command
     */
    private String params;
    /**
     * Number of days to report back on
     */
    private int days;

    /**
     * Create a new find borrowed books request given the library
     * and the parameters for the request.
     * @param reportGenerator ReportGenerator
     * @param params The parameters that follow a request command
     */
    public LibraryStatisticsReport(ReportGenerator reportGenerator, String clientID, String params) {
        this.reportGenerator = reportGenerator;
        this.clientID = clientID;
        this.params = params;
    }

    /**
     * Check the parameters to validate what the request is.
     * @return If the parameters are correct
     */
    @Override
    public boolean checkParams() {
        String[] parts = params.split(",");
        if (parts.length == 1) {
            if(parts[0].equals("")){
                days = 0;
            }
            else{
                days = Integer.parseInt(parts[0]);
            }
        }
        else{
            return false;
        }
        return true;
    }

    /**
     * Execute the library statistics command which returns a string.
     * @return String displaying the library statistics
     */
    @Override
    public String execute() {
        if (!checkParams()) {
            return clientID + DELIMITER + PARAM_MESSAGE;
        }
        return clientID + DELIMITER + reportGenerator.generateInfoReport(days);
    }
}
