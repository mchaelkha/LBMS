package Checkout;

/**
 *
 */
public class Transaction {

    /**
     * The date that the checkout occurred
     */
    public String checkoutDate;
    /**
     * The date that the book is due
     */
    public String dueDate;
    /**
     * The date that the book is returned
     */
    public String returnDate;
    /**
     * The book's ISBN
     */
    public String book;
    /**
     * The fine amount of the transaction
     */
    public int fineAmount;

    /**
     * Create a new transaction given a checkout date and the book.
     * @param checkoutDate The checkout date
     * @param book The book's ISBN
     */
    public Transaction(String checkoutDate, String book) {
        this.checkoutDate = checkoutDate;
        this.book = book;
        // calculate due date

        fineAmount = 0;
    }

    /**
     * Return the book on the given date.
     * @param date The date of return
     */
    public void returnBook(String date) {
        returnDate = date;
        calculateFine();
    }

    /**
     * Calculate the number of late days after the due date.
     * Days are rounded up.
     * @return The number of late days after the due date
     */
    public int calculateNumLateDays() {
        return 0;
    }

    /**
     * Get the fine amount.
     * @return Fine amount
     */
    public int getFineAmount() {
        return fineAmount;
    }

    /**
     * Calculate the fine amount from this transaction. This
     * is determined by the number of late days.
     */
    private void calculateFine() {

    }

}
