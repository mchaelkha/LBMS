package Model.Library;

import Model.Book.BookDB;
import Model.Checkout.CheckoutDB;
import Model.Visitor.VisitorDB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The report generator that aggregates the statistics from each individual
 * database to create a report.
 *
 * @author Michael Kha
 * @author Luis Gutierrez
 */
public class ReportGenerator implements Serializable {

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
     * Report to hold all data from start of simulation.
     * Used when days param is not specified in report request
     */
    private StatisticsReport generalStatisticsReport;

    /**
     * Create the report generator that knows about the other databases.
     * @param bookDB The book database
     * @param visitorDB The visitor database
     * @param checkoutDB The checkout database
     */
    public ReportGenerator(BookDB bookDB, VisitorDB visitorDB, CheckoutDB checkoutDB) {
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
            //TODO return StatisticsReport representing overall simulation stats
            return generalStatisticsReport.toString();
        } else {
            //Create new report using daily reports to be included
            List<StatisticsReport> reportsIncluded = new ArrayList<>();
            int reportIndex = statisticsReportList.size();
            int count = days;
            while(count > 0){
                reportsIncluded.add(statisticsReportList.get(reportIndex));
                reportIndex -= 1;
                count--;
            }

            StatisticsReport statisticsReport = new StatisticsReport(reportsIncluded);
            return statisticsReport.toString();
        }
    }

    /**
     * Method called by timeKeeper to generate a daily report when it is closing hour
     * @param dateGenerated current string date passed by timeKeeper
     */
    public StatisticsReport generateDailyReport(String dateGenerated) {
        //Some Fields are cleared in databases when new report is generated
        //bookDB: get numBooksInLibrary (from BookData books.size), get numBooksPurchased
        //checkoutDB: get finesCollected, get finesUncollected
        //visitorDB: get numRegisteredVisitors, get avgLengthVisit,
        //TimeKeeper: get dateGenerated

        int numRegisteredVisitors = visitorDB.getNumRegisteredVisitors();
        String avgLengthVisit = visitorDB.getAverageLengthVisit();
        int numBooksInLibrary = bookDB.getNumBooksInLibrary();
        int numBooksPurchased = bookDB.getNumBooksPurchased();
        int collectedFines = checkoutDB.getCollectedFines();
        int uncollectedFines = checkoutDB.getUncollectedFines();

        StatisticsReport statisticsReport = new StatisticsReport(dateGenerated, numBooksInLibrary,
                numRegisteredVisitors, avgLengthVisit, numBooksPurchased, collectedFines, uncollectedFines);

        statisticsReportList.add(statisticsReport);

        return statisticsReport;
    }

}
