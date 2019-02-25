package Checkout;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Class that stores information about checkout transactions performed by Visitors.
 * @author Hersh Nagpal
 */
public class Transaction {

    /**
     * The base fine
     */
    public static int BASE_FINE = 8;

    /**
     * The amount the fine increases with each day past the due date
     */
    public static int FINE_DAILY_INCREMENT = 2;

    /**
     * The maximum amount of days that a book can be checked out for.
     */
    public static int MAX_CHECKOUT_PERIOD = 7;

    /**
     * The date that the checkout occurred
     */
    public LocalDateTime checkoutDate;
    /**
     * The date that the book is due
     */
    public LocalDateTime dueDate;
    /**
     * The date that the book is returned
     */
    public LocalDateTime returnDate;
    /**
     * The book's ISBN
     */
    public String isbn;
    /**
     * The fine amount of the transaction
     */
    public int fineAmount;

    /**
     * Create a new transaction given a checkout date and the book.
     * @param checkoutDate The checkout date
     * @param book The book's ISBN
     */
    public Transaction(LocalDateTime checkoutDate, String isbn) {
        this.checkoutDate = checkoutDate;
        this.isbn = isbn;
        this.dueDate = checkoutDate.plusDays(MAX_CHECKOUT_PERIOD);

        fineAmount = 0;
    }

    /**
     * Return the book on the given date.
     * @param date The date of return
     */
    public void returnBook(LocalDateTime date) {
        returnDate = date;
        setFine();
    }

    /**
     * Calculate the number of late days after the due date.
     * Days are rounded up.
     * @return The number of late days after the due date
     */
    public int calculateNumLateDays() {
        return (int)ChronoUnit.DAYS.between(dueDate, returnDate);
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
    private void setFine() {
        if(returnDate.isBefore(dueDate)) {
            this.fineAmount = 0;
        } else {
            this.fineAmount = BASE_FINE + FINE_DAILY_INCREMENT * (int)ChronoUnit.DAYS.between(dueDate, returnDate);
        }
    }

}
