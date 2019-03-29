package Controller.Request;

import Model.Library.ReportGenerator;
import Model.Library.TimeKeeper;
import Model.Visitor.VisitorDB;

import java.time.LocalDateTime;

/**
 * Advances the time by a number of days and hours as requested by the client.
 * @author Jack Li
 */
public class AdvanceTime extends AccessibleRequest {
    /**
     * Message for missing parameters
     */
    private static final String PARAM_MESSAGE = String.format(MISSING_PARAM,
            ADVANCE_REQUEST) + DELIMITER + "number-of-days[,number-of-hours]";
    /**
     * TimeKeeper used to update simulation time
     */
    private TimeKeeper timeKeeper;
    /**
     * ReportGenerator used to create empty daily reports if advance time advances days
     */
    private ReportGenerator reportGenerator;
    /**
     * VisitorDB used to kick out visitors if advance time advances days
     */
    private VisitorDB visitorDB;
    /**
     * the parameters for the command.
     */
    private String params;
    /**
     * Days to add
     */
    private int days;
    /**
     * Hours to add
     */
    private int hours;

    /**
     * Creates a new AdvanceTime request with the given parameters.
     * @param params The parameters for the command.
     */
    public AdvanceTime(ReportGenerator reportGenerator,
                       TimeKeeper timeKeeper, String clientID, String params) {
        super(clientID, true);
        this.timeKeeper = timeKeeper;
        this.reportGenerator = reportGenerator;
        this.visitorDB = VisitorDB.getInstance();
        this.params = params;
    }

    /**
     * Checks whether the parameters are valid for this command.
     * @return Whether or not the parameters are valid.
     */
    @Override
    public boolean checkParams() {
        String[] parts = params.split(DELIMITER);
        if (parts.length > 0) {
            days = Integer.parseInt(parts[0]);
            if (parts.length > 1) {
                hours = Integer.parseInt(parts[1]);
            }
            return true;
        }
        return false;
    }

    /**
     * Get the name of the request
     * @return The name
     */
    @Override
    public String getName() {
        return ADVANCE_REQUEST;
    }

    /**
     * Executes the AdvanceTime command to move the system time forward.
     * @return String indicating that the execution has succeeded.
     */
    @Override
    public String execute() {
        if (!checkParams()) {
            return clientID + DELIMITER + PARAM_MESSAGE;
        }
        //Move simulation time forward by "days" and "hours"
        timeKeeper.addDays(days);
        timeKeeper.addHours(hours);

        if(days>0){
            //End visit of visitors currently in library. End time is same day, closing hour
            LocalDateTime startTimeCopy = LocalDateTime.now();
            LocalDateTime endVisitTime = startTimeCopy.withHour(19);
            visitorDB.clearCurrentVisitors(endVisitTime);
            //For each day moved forward generate a new daily report
            for(int i = 1; i<=days; i++){
                reportGenerator.generateDailyReport();
            }
        }

        //Update libraryState
        timeKeeper.setLibraryStateAdvance();
        return clientID + DELIMITER + ADVANCE_REQUEST + DELIMITER + SUCCESS + TERMINATOR;
    }
}
