package main.java.Model.Library;

import main.java.Model.Book.BookDB;
import main.java.Model.Checkout.CheckoutDB;
import main.java.Model.Visitor.VisitorDB;

/**
 *
 *
 * @author Michael Kha
 */
public class ReportGenerator {

    private BookDB bookDB;
    private VisitorDB visitorDB;
    private CheckoutDB checkoutDB;

    public ReportGenerator(BookDB bookDB, VisitorDB visitorDB, CheckoutDB checkoutDB) {
        this.bookDB = bookDB;
        this.visitorDB = visitorDB;
        this.checkoutDB = checkoutDB;
    }

    /**
     * Generates the monthly info report.
     * @param month Which month it currently is.
     * @return A report of the relevant information for the month.
     */
    public String generateInfoReport(String month){
        return "";
    }

    /**
     * Backs up the library's data.
     */
    public void backupData(){

    }
}
