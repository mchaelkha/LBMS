package Model.Library;

/**
 * A Library Statistics Report encapsulating summary information of the simulation.
 *
 * @author Luis Gutierrez
 */
public class StatisticsReport {

    private String dateGenerated;
    private int numBooksInLibrary;
    private int numRegisteredVisitors;
    private String avgLengthVisit;
    private int numBooksPurchased;
    private int finesCollected;
    private int finesUncollected;

    /**
     * TODO Create new StatisticsReport when simulation is started (Fields set to 0) this will represent overall stats
     * TODO Create new StatisticsReport every library closing (In timekeeper when hour = 12)
     * TODO
     * @param dateGenerated
     * @param numBooksInLibrary
     * @param numRegisteredVisitors
     * @param avgLengthVisit
     * @param numBooksPurchased
     * @param finesCollected
     * @param finesUncollected
     */
    public StatisticsReport(String dateGenerated, int numBooksInLibrary, int numRegisteredVisitors,
                            String avgLengthVisit, int numBooksPurchased, int finesCollected, int finesUncollected) {
        this.dateGenerated = dateGenerated;
        this.numBooksInLibrary = numBooksInLibrary;
        this.numRegisteredVisitors = numRegisteredVisitors;
        this.avgLengthVisit = avgLengthVisit;
        this.numBooksPurchased = numBooksPurchased;
        this.finesCollected = finesCollected;
        this.finesUncollected = finesUncollected;
    }

    //TODO create constructor that takes a list of StatisticsReports and accumulates stats into one statisticsReport

    /**
     * Update current report with information from a new report.
     * Used to update general StatisticsReport holding statistics for the whole simulation.
     */
    public void updateReport(StatisticsReport statisticsReport) {

    }

    public String getDateGenerated() {
        return dateGenerated;
    }

    public int getNumBooksInLibrary() {
        return numBooksInLibrary;
    }

    public int getNumRegisteredVisitors() {
        return numRegisteredVisitors;
    }

    public String getAvgLengthVisit() {
        return avgLengthVisit;
    }

    public int getNumBooksPurchased() {
        return numBooksPurchased;
    }

    public int getFinesCollected() {
        return finesCollected;
    }

    public int getFinesUncollected() {
        return finesUncollected;
    }
}
