package Book;

import java.util.List;

public class BookInfo {

    private String isbn;
    private String title;
    private List<String> authors;
    private String publisher;
    private String publishDate;
    private int pageCount;
    private int totalNumCopies;
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
        totalNumCopies = 1;
        totalCopiesAvailable = 1;
    }

    /**
     * Adds a copy of this book to the library.
     */
    public void addCopy(){
        totalNumCopies++;
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
        totalCopiesAvailable++;
    }

    /**
     * Get the book's ISBN
     * @return The book's ISBN
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Gives information about this book
     * @return a String containing information about the book.
     */
    public String getInfo(){
        return "";
    }

    /**
     * Chooses the method to compare two books.
     * @param compare The method to compare by.
     */
    public void setComparator(String compare){

    }
}
