package Model.Library;

import Controller.Request.RequestUtil;

import java.sql.Time;
import java.util.List;

/**
 * A Library Statistics Report encapsulating summary information of the simulation.
 *
 * @author Luis Gutierrez
 */
public class StatisticsReport implements RequestUtil {

    private int numBooksInLibrary;
    private int numRegisteredVisitors;
    private String avgLengthVisit;
    private long avgLengthVisitLong;
    private int numBooksPurchased;
    private int finesCollected;
    private int finesUncollected;

    /**
     * Constructor setting the report statistics
     * @param numBooksInLibrary
     * @param numRegisteredVisitors
     * @param avgLengthVisit
     * @param numBooksPurchased
     * @param finesCollected
     * @param finesUncollected
     */
    public StatisticsReport(int numBooksInLibrary, int numRegisteredVisitors,
                            long avgLengthVisitLong, String avgLengthVisit, int numBooksPurchased,
                            int finesCollected, int finesUncollected) {
        this.numBooksInLibrary = numBooksInLibrary;
        this.numRegisteredVisitors = numRegisteredVisitors;
        this.avgLengthVisit = avgLengthVisit;
        this.avgLengthVisitLong = avgLengthVisitLong;
        this.numBooksPurchased = numBooksPurchased;
        this.finesCollected = finesCollected;
        this.finesUncollected = finesUncollected;
    }

    /**
     * Constructor for a statistics report accumulating stats from a list of statistics reports
     * @param statisticsReportList
     */
    public StatisticsReport(List<StatisticsReport> statisticsReportList){
        //Number of Books in Library is the most recent num of books in library (from first report in list)
        numBooksInLibrary = statisticsReportList.get(0).numBooksInLibrary;
        //Number of Registered Visitors from most recent report
        numRegisteredVisitors = statisticsReportList.get(0).numRegisteredVisitors;
        //Fines Uncollected from most recent report
        finesUncollected = statisticsReportList.get(0).finesUncollected;

        long avgLengthVisits = 0;
        for (StatisticsReport statisticsReport : statisticsReportList) {
            avgLengthVisits += statisticsReport.avgLengthVisitLong;
            numBooksPurchased += statisticsReport.numBooksPurchased;
            finesCollected += statisticsReport.finesCollected;
        }
        avgLengthVisitLong = avgLengthVisits/statisticsReportList.size();
        avgLengthVisit = TimeKeeper.calculateDurationString(avgLengthVisits);
    }

    /**
     * String representation of the statistics report to be used as a response.
     * @return The string form of the report
     */
    @Override
    public String toString() {
        return "Number of Books: " + numBooksInLibrary + NEW_LINE +
                "Number of Visitors: " + numRegisteredVisitors + NEW_LINE +
                "Average Length of Visit: " + avgLengthVisit + NEW_LINE +
                "Number of Books Purchased: " + numBooksPurchased + NEW_LINE +
                "Fines Collected: " + finesCollected + NEW_LINE +
                "Fines Outstanding: " + finesUncollected + NEW_LINE + TERMINATOR;
    }
}
