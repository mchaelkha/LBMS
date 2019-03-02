package main.java.Controller.Request;

import main.java.Model.Book.BookDB;
import main.java.Model.Book.BookInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Book store search request to query for specific books.
 *
 * @author Michael Kha
 */
public class BookStoreSearch implements Request {

    /**
     * The book database of the library
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
     * Create a new book store search request given the book database
     * and the parameters for the request.
     * @param bookDB The book database
     * @param params The parameters that follow a request command
     */
    public BookStoreSearch(BookDB bookDB, String params) {
        this.bookDB = bookDB;
        this.params = params;
    }

    /**
     * TODO: proper missing parameter checking
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
        String[] titleArr = firstSplit[0].split(DELIMITER);
        title = titleArr[0];
        String[] isbnPublisherSort = firstSplit[2].split(DELIMITER);
        isbn = isbnPublisherSort[0];
        publisher = isbnPublisherSort[1];
        sort = isbnPublisherSort[2];
        return PROPER_PARAM;
    }

    /**
     * Execute the book store search command which returns a string.
     * @return String containing the results of the book search
     */
    @Override
    public String execute() {
        String check = checkParams();
        if (!check.equals(PROPER_PARAM)) {
            return check;
        }
        Map<String, BookInfo> search = bookDB.searchStore(
                title, authors, isbn, publisher, sort);
        return buildString(search);
    }

    /**
     * Build the formatted string to return as a response.
     * @param search Mapping of ID to book info
     * @return Readable string representation of the map
     */
    private String buildString(Map<String, BookInfo> search) {
        int size = search.size();
        String result = "" + SEARCH_REQUEST + DELIMITER
                + size + DELIMITER + NEW_LINE;
        for (String id : search.keySet()) {
            BookInfo book = search.get(id);
            result += id + DELIMITER;
            result += book + DELIMITER;
            result += NEW_LINE;
        }
        result += TERMINATOR;
        return result;
    }

}
