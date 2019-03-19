package Controller.Request;

import Model.Book.BookDB;
import Model.Book.BookInfo;
import Model.Library.LibrarySystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Library book search request to query for specific books.
 *
 * @author Michael Kha
 */
public class LibraryBookSearch implements Request {
    /**
     * Message for missing parameters
     */
    private static final String PARAM_MESSAGE = String.format(MISSING_PARAM,
            ARRIVE_REQUEST) + DELIMITER + "title,[{authors},isbn" +
            "[,publisher[,sort order]]]";
    /**
     * Used to search collection of books contained in book database
     */
    private BookDB bookDB;
    /**
     * Params in the command
     */
    private String params;
    /**
     * Book title
     */
    private String title;
    /**
     * Authors of the book
     */
    private List<String> authors;
    /**
     * ISBN for the book
     */
    private String isbn;
    /**
     * Publisher for the book
     */
    private String publisher;
    /**
     * Sort ordering
     */
    private String sort;

    /**
     * Create a new library book search request given the book database
     * and the parameters for the request.
     * @param bookDB System's book database
     * @param params The parameters that follow a request command
     */
    public LibraryBookSearch(BookDB bookDB, String params) {
        this.bookDB = bookDB;
        this.params = params;
    }

    /**
     * Check the parameters to validate that the request is
     * @return If the parameters are correct
     */
    @Override
    public String checkParams() {
        String[] firstSplit = params.split("[{}]");
        authors = new ArrayList<>(Arrays.asList(firstSplit[1].split(",")));
        if (authors.size() == 1 && authors.get(0).equals(IGNORE)) {
            authors.clear();
        }
        String[] titleArr = firstSplit[0].split(",(?!\\s)");
        title = titleArr[0];
        String[] isbnPublisherSort = firstSplit[2].split(",(?!\\s)");
        if (isbnPublisherSort.length != 4) {
            return PARAM_MESSAGE;
        }
        isbn = isbnPublisherSort[1];
        publisher = isbnPublisherSort[2];
        sort = isbnPublisherSort[3];
        return PROPER_PARAM;
    }

    /**
     * Execute the library book search command which returns a string.
     * @return String containing the results of the book search
     */
    @Override
    public String execute() {
        String check = checkParams();
        if (!check.equals(PROPER_PARAM)) {
            return check;
        }
        return buildString(bookDB.searchBooks(title,authors,isbn,publisher,sort));
    }

    /**
     * Build the formatted string to return as a response.
     * @param search Mapping of ID to book info
     * @return Readable string representation of the map
     */
    private String buildString(Map<String, BookInfo> search) {
        if (search == null) {
            return INFO_REQUEST + DELIMITER + "invalid-sort-order" + TERMINATOR;
        }
        int size = search.size();
        String result = "" + INFO_REQUEST + DELIMITER
                + size + DELIMITER;
        for (String id : search.keySet()) {
            BookInfo book = search.get(id);
            result += NEW_LINE;
            result += book.getTotalCopiesAvailable() + DELIMITER;
            result += id + DELIMITER;
            result += book + DELIMITER + book.getPageCount();
        }
        result += String.format("{%d}", size);
        result += TERMINATOR;
        return result;
    }
}
