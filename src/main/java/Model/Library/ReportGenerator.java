package Model.Library;

import Model.Book.BookDB;
import Model.Checkout.CheckoutDB;
import Model.Visitor.VisitorDB;

import java.util.ArrayList;
import java.util.List;

/**
 * The report generator that aggregates the statistics from each individual
 * database to create a report.
 *
 * @author Michael Kha
 * @author Luis Gutierrez
 */
public class ReportGenerator {

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
            //TODO
        }
        return "";
    }

    /**
     * Method called by timeKeeper to generate a daily report when it is closing hour
     * @param dateGenerated current string date passed by timeKeeper
     */
    public void generateDailyReport(String dateGenerated) {
        //bookDB: get numBooksInLibrary,
        //checkoutDB: get numBooksPurchased, get finesCollected, get finesUncollected
        //visitorDB: get numRegisteredVisitors, get avgLengthVisit,
        //TimeKeeper: get dateGenerated

        int numRegisteredVisitors = visitorDB.getNumRegisteredVisitors();
        String avgLengthVisit = visitorDB.getAverageLengthVisit();


        //TODO clear visitLengths list in visitorDB,
        //StatisticsReport statisticsReport = new StatisticsReport()
        //return statisticsReport
    }

}
