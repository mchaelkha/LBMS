package Checkout;

/**
 *
 */
public class Transaction {

    public String checkoutDate;

    public String dueDate;

    public String returnDate;

    public String book;

    public int fineAmount;

    public Transaction(String checkOutDate, String book) {
        this.checkoutDate = checkOutDate;
        this.book = book;
        // calculate due date

        fineAmount = 0;
    }

    public void returnBook(String date) {
        returnDate = date;
        calculateFine();
    }

    public int calculateNumLateDays() {
        return 0;
    }

    public int getFineAmount() {
        return fineAmount;
    }

    private void calculateFine() {

    }

}
