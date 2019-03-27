package Model.Library;

import Controller.Request.RequestUtil;
import Model.Book.BookDB;
import Model.Checkout.CheckoutDB;
import Model.Visitor.VisitorDB;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * The report generator that aggregates the statistics from each individual
 * database to create a report.
 *
 * @author Michael Kha
 * @author Luis Gutierrez
 */
public class ReportGenerator implements RequestUtil, Serializable {

    /**
     * The book database
     */
    private BookDB bookDB;
    /**
     * The visitor database
     */
    private VisitorDB visitorDB;
    /**
     * The checkout database
     */
    private CheckoutDB checkoutDB;

    /**
     * Used to get the Statistics Report date generated
     */
    private TimeKeeper timeKeeper;

    /**
     * Holds all StatisticsReports generated every day.
     */
    private List<StatisticsReport> statisticsReportList;

    /**
     * Create the report generator that knows about the other databases.
     * @param bookDB The book database
     * @param visitorDB The visitor database
     * @param checkoutDB The checkout database
     */
    public ReportGenerator(TimeKeeper timeKeeper,BookDB bookDB, VisitorDB visitorDB, CheckoutDB checkoutDB) {
        this.timeKeeper = timeKeeper;
        this.bookDB = bookDB;
        this.visitorDB = visitorDB;
        this.checkoutDB = checkoutDB;
        this.statisticsReportList = new ArrayList<>();
    }

    /**
     * Generates the info report.
     * @param days Number of days back
     * @return A report of the relevant information for the days back.
     */
    public String generateInfoReport(int days){
        if (days == 0) {
            StatisticsReport generalStatisticsReport = new StatisticsReport(statisticsReportList);
            return REPORT_REQUEST+DELIMITER+timeKeeper.readDate()+DELIMITER+NEW_LINE+generalStatisticsReport.toString();
        }
        else {
            //Create new report using daily reports to be included
            List<StatisticsReport> reportsIncluded = new ArrayList<>();
            int reportIndex = statisticsReportList.size()-1;
            int count = days;
            //Check that days param is lower than daily reports generated
            if (days > statisticsReportList.size()) {
                return REPORT_REQUEST+DELIMITER+"invalid-number-of-days"+TERMINATOR;
            }
            while(count > 0){
                reportsIncluded.add(statisticsReportList.get(reportIndex));
                reportIndex -= 1;
                count--;
            }

            StatisticsReport statisticsReport = new StatisticsReport(reportsIncluded);
            return REPORT_REQUEST+DELIMITER+ timeKeeper.readDate()+DELIMITER+NEW_LINE+statisticsReport.toString();
        }
    }

    /**
     * Method called by timeKeeper to generate a daily report when it is closing hour
     */
    public StatisticsReport generateDailyReport() {
        //Fields are cleared in databases when new report is generated
        //bookDB: get numBooksInLibrary (from BookStorage books.size), get numBooksPurchased
        //checkoutDB: get finesCollected, get finesUncollected
        //visitorDB: get numRegisteredVisitors, get avgLengthVisit,
        //TimeKeeper: get dateGenerated

        //System.out.println("Report Generated");
        int numRegisteredVisitors = visitorDB.getNumRegisteredVisitors();
        long avgLengthVisitLong = visitorDB.getAverageLengthVisit();
        String avgLengthVisit = TimeKeeper.calculateDurationString(avgLengthVisitLong);
        int numBooksInLibrary = bookDB.getNumBooksInLibrary();
        int numBooksPurchased = bookDB.getNumBooksPurchased();
        int collectedFines = checkoutDB.getCollectedFines();
        int uncollectedFines = checkoutDB.getUncollectedFines();

        StatisticsReport statisticsReport = new StatisticsReport(numBooksInLibrary,
                numRegisteredVisitors, avgLengthVisitLong, avgLengthVisit, numBooksPurchased,
                collectedFines, uncollectedFines);
        statisticsReportList.add(statisticsReport);

        //Clear daily stats in DataBases
        checkoutDB.clearDailyFineFields();
        visitorDB.clearVisitLengths();
        bookDB.clearNumBooksPurchased();

        return statisticsReport;
    }

}
