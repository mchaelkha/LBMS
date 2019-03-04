package Model.Book;

import Controller.Request.RequestUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The book database that is used by the library to manage book purchases,
 * book checkouts, and book returns.
 *
 * @author Michael Kha
 */
public class BookDB extends BookData implements Serializable, RequestUtil {

    /**
     * Bookstore to purchase books from
     */
    private Bookstore bookstore;

    /**
     * Create a new book database that is empty.
     */
    public BookDB() {
        super();
        bookstore = new Bookstore();
    }

    /**
     * Search the book store for books with the given information.
     * @param title The title
     * @param authors The authors
     * @param isbn The isbn
     * @param publisher The publisher
     * @param sort The sort order
     * @return The mapping of hits to a unique ID
     */
    public Map<String, BookInfo> searchStore(String title,
                                             List<String> authors,
                                             String isbn,
                                             String publisher, String sort) {
        return bookstore.searchBooks(title, authors, isbn, publisher, sort);
    }

    /**
     * Purchase new books for the library based on the last search
     * made on the bookstore. The books are mapped to the last search's IDs.
     * Used to fulfill BookPurchase.
     * @param quantity Number of books to purchase for each book ID
     * @param bookIDs List of book IDs from the last search to purchase
     */
    public String purchase(int quantity, List<String> bookIDs) {
        String response = "" + BUY_REQUEST + DELIMITER + SUCCESS + DELIMITER;
        List<BookInfo> booksPurchased = bookstore.purchaseBooks(
                quantity, bookIDs);
        response += booksPurchased.size() * quantity + DELIMITER;
        String isbn;
        BookInfo temp;
        for (BookInfo book : booksPurchased) {
            response += NEW_LINE;
            isbn = book.getIsbn();
            // If book already in library, add copies
            if (books.containsKey(isbn)) {
                temp = books.get(isbn);
                book.addCopy(temp.getTotalCopies());
                books.put(isbn, book);
            } else {
                books.put(isbn, book);
            }
            // Build response string
            response += book.toString();
            response += DELIMITER + book.getTotalCopies();
        }
        return response + TERMINATOR;
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
     * Borrow books from a list of book IDs referring to books in the last
     * search.
     * @param bookIDs The book IDs to checkout
     * @return List of books to be borrowed or null if invalid book ID
     */
    public List<BookInfo> borrowBooks(List<String> bookIDs) {
        List<BookInfo> books = new ArrayList<>();
        for (String bookID : bookIDs) {
            BookInfo book = lastSearch.get(bookID);
            // All IDs must match
            if (book == null) {
                return null;
            }
            books.add(book);
            // Book no longer available
            if (!book.checkOutCopy()) {
                return null;
            }
        }
        return books;
    }

    /**
     * Removes a copy of a book from the library.
     * @param book the ISBN of the book.
     * @return Whether a copy of the book was removed.
     */
    public boolean removeCopy(String book) {
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
