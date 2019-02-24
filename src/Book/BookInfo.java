package Book;

import java.io.Serializable;
import java.util.List;

/**
 * The book info to represent all information pertaining to a book.
 *
 * @author Michael Kha
 */
public class BookInfo implements Serializable {

    /**
     * Book attributes
     */
    private String isbn;
    private String title;
    private List<String> authors;
    private String publisher;
    private String publishDate;
    private int pageCount;
    private int totalCopies;
    private int totalCopiesAvailable;

    /**
     * Create book info given the necessary information.
     */
    public BookInfo(String isbn, String title, List<String> authors, String publisher, String publishDate, int pageCount) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.pageCount = pageCount;
        totalCopies = 1;
        totalCopiesAvailable = 1;
    }

    /**
     * Create new book info by copying other book info and specified number of
     * copies.
     */
    public BookInfo(BookInfo other, int copies) {
        isbn = other.isbn;
        title = other.title;
        authors = other.authors;
        publisher = other.publisher;
        publishDate = other.publishDate;
        pageCount = other.pageCount;
        totalCopies = copies;
        totalCopiesAvailable = copies;
    }

    /**
     * Adds a copy of this book to the library.
     */
    public void addCopy(){
        totalCopies++;
    }

    /**
     * Adds a copy of this book to the library.
     */
    public void addCopy(int amount){
        totalCopies += amount;
    }

    /**
     * Checks out a copy of this book from the library.
     * @return Whether the book was successfully checked out.
     */
    public boolean checkOutCopy(){
        if (totalCopiesAvailable == 0) {
            return false;
        }
        totalCopiesAvailable--;
        return true;
    }

    /**
     * Returns a copy of this book to the library.
     */
    public void returnCopy(){
        if (totalCopiesAvailable < totalCopies)
            totalCopiesAvailable++;
    }

    /**
     * Get the book's ISBN.
     * @return The book's ISBN
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Get the book title.
     * @return The book title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the authors of the book.
     * @return The authors of the book
     */
    public List<String> getAuthors() {
        return authors;
    }

    /**
     * Get the publisher.
     * @return The publisher of the book
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Get the publish date
     * @return The publish date
     */
    public String getPublishDate() {
        return publishDate;
    }

    /**
     * Get the page count.
     * @return The page count
     */
    public int getPageCount() {
        return pageCount;
    }

    /**
     * Get the total number of copies
     * @return The total number of copies
     */
    public int getTotalCopies() {
        return totalCopies;
    }

    /**
     * Check if there are copies available.
     * @return If there are copies available
     */
    public boolean hasCopiesAvailable() {
        return totalCopiesAvailable != 0;
    }

}
