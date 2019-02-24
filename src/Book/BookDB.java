package Book;

import java.io.Serializable;
import java.util.List;

/**
 * The book database that is used by the library to manage book purchases,
 * book checkouts, and book returns.
 *
 * @author Michael Kha
 */
public class BookDB extends BookData implements Serializable {

    /**
     * Bookstore to purchase books from
     */
    private Bookstore bookstore;

    /**
     * Create a new book database that is empty.
     */
    public BookDB(){
        super();
        bookstore = new Bookstore();
    }

    /**
     * Purchase new books for the library based on the last search
     * made on the bookstore. The books are mapped to the last search's IDs.
     * Used to fulfill PurchaseRequest.
     * @param quantity Number of books to purchase for each book ID
     * @param bookIDs List of book IDs from the last search to purchase
     */
    public void purchase(int quantity, List<String> bookIDs){
        List<BookInfo> booksPurchased = bookstore.purchaseBooks(quantity, bookIDs);
        String isbn;
        BookInfo temp;
        for (BookInfo book : booksPurchased) {
            isbn = book.getIsbn();
            // If book already in library, add copies
            if (books.containsKey(isbn)) {
                temp = books.get(isbn);
                book.addCopy(temp.getTotalCopies());
                books.put(isbn, book);
            } else {
                books.put(isbn, book);
            }
        }
    }

    /**
     * Get the number of copies of a book currently in the library.
     * @param book the ISBN of the book.
     * @return The number of copies of the book.
     */
    public int getNumCopies(String book){
        return books.get(book).getTotalCopies();
    }

    /**
     * Removes a copy of a book from the library.
     * @param book the ISBN of the book.
     * @return Whether a copy of the book was removed.
     */
    public boolean removeCopy(String book){
        BookInfo bookInfo = books.get(book);
        return bookInfo.checkOutCopy();
    }

    /**
     * Returns a copy of the book to the library.
     * @param book the ISBN of the book.
     */
    public void returnCopy(String book){
        if (books.containsKey(book))
            books.get(book).returnCopy();
    }

}
