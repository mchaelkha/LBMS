package Library;

import Book.BookDB;
import Checkout.CheckoutDB;
import Visitor.VisitorDB;

public class DBManager {
    private VisitorDB visitor;
    private CheckoutDB checkout;
    private BookDB book;

    public DBManager(){

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
