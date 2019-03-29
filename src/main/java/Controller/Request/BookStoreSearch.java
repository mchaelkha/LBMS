package Controller.Request;

import Model.Book.BookDB;
import Model.Book.BookInfo;
import Model.Client.AccountDB;
import Model.Client.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Book store search request to query for specific books.
 *
 * @author Michael Kha
 */
public class BookStoreSearch extends AccessibleRequest {
    /**
     * Message for missing parameters
     */
    private static final String PARAM_MESSAGE = String.format(MISSING_PARAM,
            SEARCH_REQUEST) + DELIMITER + "title,[{authors},isbn" +
            "[,publisher[,sort order]]]";
    /**
     * Book database used to buy and store new library books
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
     * @param clientID The client making the request
     * @param params The parameters that follow a request command
     */
    public BookStoreSearch(String clientID, String params) {
        super(clientID, true);
        this.bookDB = BookDB.getInstance();
        this.params = params;
    }

    /**
     * Check the parameters to validate that the request is
     * @return If the parameters are correct
     */
    @Override
    public boolean checkParams() {
        if (!params.contains("}") || !params.contains("{")) {
            return false;
        }
        String[] firstSplit = params.split("[{}]");
        authors = new ArrayList<>(Arrays.asList(firstSplit[1].split(",")));
        if (authors.size() == 1 && authors.get(0).equals(IGNORE)) {
            authors.clear();
        }
        String[] titleArr = firstSplit[0].split(",(?!\\s)");
        title = titleArr[0];
        String[] isbnPublisherSort = firstSplit[2].split(",(?!\\s)");
        if (isbnPublisherSort.length != 4) {
            return false;
        }
        isbn = isbnPublisherSort[1];
        publisher = isbnPublisherSort[2];
        sort = isbnPublisherSort[3];
        return true;
    }

    /**
     * Get the name of the request
     * @return The name
     */
    @Override
    public String getName() {
        return SEARCH_REQUEST;
    }

    /**
     * Execute the book store search command which returns a string.
     * @return String containing the results of the book search
     */
    @Override
    public String execute() {
        if (!checkParams()) {
            return clientID + DELIMITER + PARAM_MESSAGE;
        }
        AccountDB accountDB = AccountDB.getInstance();
        Service service = accountDB.getService(clientID);
        if (service == null) {
            return clientID + DELIMITER + NOT_AUTHORIZED;
        }
        Map<String, BookInfo> search = bookDB.searchStore(service, title, authors,
                isbn, publisher, sort);
        accountDB.setStoreSearch(search, clientID);
        return clientID + DELIMITER + buildString(search);
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
        String result = "" + SEARCH_REQUEST + DELIMITER
                + size + DELIMITER;
        for (String id : search.keySet()) {
            BookInfo book = search.get(id);
            result += NEW_LINE;
            result += id + DELIMITER;
            result += book + DELIMITER;
        }
        result += TERMINATOR;
        return result;
    }

}
