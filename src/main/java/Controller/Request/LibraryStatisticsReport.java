package Controller.Request;

import Model.Library.LibrarySystem;
import Model.Library.ReportGenerator;

/**
 * Library statistics report request to get the statistics of the library
 * for some number of days back or the start.
 *
 * @author Michael Kha
 */
public class LibraryStatisticsReport implements Request{

    /**
     * Responsible for the creation of statistical reports
     */
    private ReportGenerator reportGenerator;
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
    public LibraryStatisticsReport(ReportGenerator reportGenerator, String params) {
        this.reportGenerator = reportGenerator;
        this.params = params;
    }

    /**
     * Check the parameters to validate what the request is.
     * @return If the parameters are correct
     */
    @Override
    public String checkParams() {
        String[] parts = params.split(",");
        if (parts.length == 1) {
            days = Integer.parseInt(parts[0]);
        } else {
            days = 0;
        }
        return PROPER_PARAM;
    }

    /**
     * Execute the library statistics command which returns a string.
     * @return String displaying the library statistics
     */
    @Override
    public String execute() {
        checkParams();
        return reportGenerator.generateInfoReport(days);
    }
}
