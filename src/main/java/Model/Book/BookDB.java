package Model.Book;

import Controller.Request.RequestUtil;

import Model.Client.Service;

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
public class BookDB extends BookStorage implements Serializable, RequestUtil {

    private static BookDB instance;

    /**
     * Local bookstore to purchase books from
     */
    private Bookstore bookstore;
    /**
     * Google bookstore to purchase books from
     */
    private BookAPIStore apiStore;

    /**
     * Number of books purchased during the current simulation day (Used for ReportGenerator)
     */
    private int numBooksPurchased;

    /**
     * Create a new book database that is empty.
     */
    private BookDB() {
        super();
        bookstore = new Bookstore();
        apiStore = new BookAPIStore();
    }

    public static BookDB getInstance() {
        if (instance == null) {
            instance = new BookDB();
        }
        return instance;
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
    public Map<String, BookInfo> searchStore(Service service, String title,
                                             List<String> authors,
                                             String isbn,
                                             String publisher, String sort) {
        Map<String, BookInfo> search = null;
        switch (service) {
            case LOCAL:
                search = bookstore.searchBooks(title, authors, isbn, publisher, sort);
                break;
            case GOOGLE:
                search = apiStore.searchBooks(title, authors, isbn, publisher, sort);
                break;
        }
        return search;
    }

    /**
     * Purchase new books for the library based on the last search
     * made on the bookstore. The books are mapped to the last search's IDs.
     * Used to fulfill BookPurchase.
     * @param quantity Number of books to purchase for each book ID
     * @param bookIDs List of book IDs from the last search to purchase
     */
    public String purchase(Map<String, BookInfo> search, int quantity, List<String> bookIDs) {
        String response = "" + BUY_REQUEST + DELIMITER + SUCCESS + DELIMITER;
        List<BookInfo> booksPurchased = bookstore.purchaseBooks(search,
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
        numBooksPurchased += booksPurchased.size();
        return response + TERMINATOR;
    }

    /**
     * Undoes a purchase of books from the bookstore.
     * @param bookIDs The books to remove (isbn).
     * @param quantity The amount of each book to remove.
     */
    public void undoPurchase(List<String> bookIDs, int quantity){
        for(String isbn: bookIDs){
            if(books.containsKey(isbn)){
                books.get(isbn).removeCopies(quantity);
                numBooksPurchased -= quantity;
            }
            if(books.get(isbn).getTotalCopies() <= 0)
                books.remove(isbn);
        }
    }

    /**
     * Helps checkoutBooks request validate that bookIds being borrowed are contained
     * in the last book search.
     * @param search The book search
     * @param bookIds The list of book IDs to check
     * @return If there is no mismatch
     */
    public boolean checkIDsMatch(Map<String, BookInfo> search, List<String> bookIds){
        for (String id : bookIds) {
            if (!search.containsKey(id)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Borrow books from a list of book IDs referring to books in the last
     * search.
     * @param search The book search
     * @param bookIDs The book IDs to checkout
     * @return List of books to be borrowed or null if invalid book ID
     */
    public List<BookInfo> borrowBooks(Map<String, BookInfo> search, List<String> bookIDs) {
        List<BookInfo> books = new ArrayList<>();
        for (String bookID : bookIDs) {
            BookInfo book = search.get(bookID);
            books.add(book);
            // Book no longer available. Not enough copies
            if (!book.checkOutCopy()) {
                return null;
            }
        }
        return books;
    }

    /**
     * Returns a copy of the book to the library.
     * @param book the ISBN of the book.
     */
    public void returnCopy(String book){
        if (books.containsKey(book))
            books.get(book).returnCopy();
    }

    /**
     * Helper method for reportGenerator to retrieve number of books in library
     * @return number of books in library
     */
    public int getLibraryBooksAmount(){
        return books.size();
    }

    /**
     * Helper method for reportGenerator to retrieve number of books purchased
     * @return number of books purchased
     */
    public int getBooksPurchased(){
        return numBooksPurchased;
    }

    /**
     * Clear daily statistic "numBooksPurchased" when daily report is generated
     */
    public void resetBooksPurchased(){
        numBooksPurchased = 0;
    }

}
