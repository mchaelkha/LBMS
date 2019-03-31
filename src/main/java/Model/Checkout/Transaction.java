package Model.Checkout;

import Model.Book.BookInfo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Class that stores information about checkout transactions performed by Visitors.
 * @author Hersh Nagpal
 * @author Luis Gutierrez
 */
public class Transaction implements Serializable {

    /**
     * The base fine
     */
    private static double BASE_FINE = 10;

    /**
     * The amount the fine increases with each day past the due date
     */
    private static double FINE_DAILY_INCREMENT = 2;

    /**
     * The maximum amount of days that a book can be checked out for.
     */
    private static int MAX_CHECKOUT_PERIOD = 7;

    /**
     * The date that the checkout occurred
     */
    private LocalDateTime checkoutDate;
    /**
     * The date that the book is due
     */
    private LocalDateTime dueDate;
    /**
     * The date that the book is returned
     */
    private LocalDateTime returnDate;
    /**
     * The book
     */
    private BookInfo bookInfo;
    /**
     * The fine amount of the transaction
     */
    private double fineAmount;

    /**
     * Create a new transaction given a checkout date and the book.
     * @param checkoutDate The checkout date
     * @param bookInfo The book
     */
    public Transaction(LocalDateTime checkoutDate, BookInfo bookInfo) {
        this.checkoutDate = checkoutDate;
        this.bookInfo = bookInfo;
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
    public double getFineAmount() {
        return fineAmount;
    }

    /**
     * Set the fine amount for this transaction
     */
    public void setFine() {
        if(returnDate.isBefore(dueDate)) {
            this.fineAmount = 0;
        } else {
            this.fineAmount = calculateTransactionFine();
        }
    }

    /**
     * Calculate the fine amount from this transaction. This
     * is determined by the number of late days.
     */
    public double calculateTransactionFine() {
        return BASE_FINE + FINE_DAILY_INCREMENT * calculateNumLateDays();
    }

    /**
     * Get the book info of this transaction.
     * @return The book info
     */
    public BookInfo getBookInfo() {
        return bookInfo;
    }

    /**
     * Get the isbn of the book associated with this Transaction
     * @return the isbn of the book associated with this Transaction
     */
    public String getIsbn() {
        return bookInfo.getIsbn();
    }

    /**
     * Get the title of the book associated with this Transaction
     * @return The title of the book
     */
    public String getTitle() {
        return bookInfo.getTitle();
    }

    /**
     * Get the due date
     * @return The due date
     */
    public String getDueDate(){
        return returnDateString(dueDate);
    }

    /**
     * Get the checkout date
     * @return The checkout date
     */
    public String getCheckoutDate(){
        return returnDateString(checkoutDate);
    }

    /**
     * Get the date string of the local date time.
     * @param localDateTime The local date and time
     * @return Formatted date string
     */
    public String returnDateString(LocalDateTime localDateTime) {
        return localDateTime.getYear() + "/" + localDateTime.getMonthValue() + "/" + localDateTime.getDayOfMonth();
    }

    /**
     * Used to clear transaction fine when visitor pays fine
     */
    public void clearFine(){
        fineAmount = 0;
    }

    /**
     * Helper method to decrease fine amount
     * @param amount amount that fineAmount is being decreased by
     */
    public void decreaseFineAmount(double amount){
        fineAmount -= amount;
    }
}
