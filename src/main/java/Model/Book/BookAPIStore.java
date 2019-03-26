package Model.Book;

import Controller.Request.RequestUtil;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The API bookstore that uses the Google Books API to search for books.
 *
 * @author Michael Kha
 */
public class BookAPIStore extends BookData implements RequestUtil {

    /**
     * Query search parameters
     */
    private static final String Q_TITLE = "intitle=";
    private static final String Q_AUTHOR = "inauthor=";
    private static final String Q_ISBN = "isbn=";
    private static final String Q_PUBLISHER = "inpublisher=";

    /**
     * Parts of the url you will need to create a connection.
     */
    private static final String URL = "https://www.googleapis.com/books/v1/volumes";
    // See this page for search parameters: https://developers.google.com/books/docs/v1/using

    /**
     * Search the books through the Google Books API web service. The JSON
     * response must be interpreted into book information.
     * @param title Title search parameter
     * @param authors Authors search parameter
     * @param isbn ISBN search parameter
     * @param publisher Publisher search parameter
     * @param sort Sort the search by either title or publish-date
     * @return Mapping of the books
     */
    @Override
    public Map<String, BookInfo> searchBooks(String title,
                                             List<String> authors,
                                             String isbn,
                                             String publisher, String sort) {
        Map<String, BookInfo> searchedBooks;
        List<BookInfo> hits = null;
        String query = createQuery(title, authors, isbn, publisher);
        try {
            URL BookURL = new URL(URL + query);
            HttpURLConnection conn = (HttpURLConnection) BookURL.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuilder response = new StringBuilder();
            // Build a single one line string of JSON
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            hits = parseJSONString(response.toString());
            hits = sortBooks(hits, sort);
            if (hits == null) {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        searchedBooks = createMap(hits);
        return searchedBooks;
    }

    /**
     * Create the search query for the book API search.
     * @param title Title search parameter
     * @param authors Authors search parameter
     * @param isbn ISBN search parameter
     * @param publisher Publisher search parameter
     * @return A query to be used in a search GET request
     */
    private String createQuery(String title, List<String> authors,
                               String isbn, String publisher) {
        String ignore = "*";
        String query = "?q=";
        try {
            if (!title.equals(ignore)) {
                title = encode(title);
                query += Q_TITLE + title + "&";
            }
            if (!authors.isEmpty()) {
                query += Q_AUTHOR;
                for (int i = 0; i < authors.size(); i++) {
                    String author = encode(authors.get(i));
                    authors.set(i, author);
                    query += author;
                    if (i == authors.size() - 1) {
                        query += "+";
                    }
                }
                query += "&";
            }
            if (!isbn.equals(ignore)) {
                query += Q_ISBN + isbn + "&";
            }
            if (!publisher.equals(ignore)) {
                publisher = encode(publisher);
                query += Q_PUBLISHER + publisher + "&";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // Always remove last "&" symbol
        return query.substring(0, query.length() - 1);
    }

    /**
     * Replace spaces so they are readable in the URL.
     * @param element Element to replace for
     * @return Element with %20 as a space
     */
    private String encode(String element) throws UnsupportedEncodingException {
        return URLEncoder.encode(element, StandardCharsets.UTF_8.toString());
    }

    /**
     * Parse the JSON response book by book.
     * @param response The JSON response produced by a web request
     */
    private List<BookInfo> parseJSONString(String response) {
        // Tutorial: http://tutorials.jenkov.com/java-json/gson-jsonparser.html
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonTree = jsonParser.parse(response);
        if (!jsonTree.isJsonObject()) {
            return null;
        }
        List<BookInfo> hits = new ArrayList<>();
        JsonObject jsonObject = jsonTree.getAsJsonObject();
        JsonArray booksArray = jsonObject.getAsJsonArray("items");
        // Iterate over each book or item
        for (int i = 0; i < booksArray.size(); i++) {
            String title;
            List<String> authors = new ArrayList<>();
            String isbn = null;
            String publisher;
            // Retrieve book and needed info
            JsonObject book = booksArray.get(i).getAsJsonObject();
            JsonObject volumeInfo = book.getAsJsonObject("volumeInfo");
            JsonObject saleInfo = book.getAsJsonObject("saleInfo");
            // TODO: Check saleability is "FOR_SALE"
            JsonElement saleability = saleInfo.get("saleability");
            if (!(saleability.getAsString().equals("FOR_SALE")))
                continue;
            // TODO: Check country is "US"
            JsonElement country = saleInfo.get("country");
            if (!(country.getAsString().equals("US")))
                continue;
            // Check title exists
            JsonElement titleField = volumeInfo.get("title");
            if (titleField == null) {
                continue;
            }
            title = titleField.toString();
            title = title.substring(1, title.length() - 1);
            // Check authors exist
            JsonArray authorsArray = volumeInfo.getAsJsonArray("authors");
            if (authorsArray == null) {
                continue;
            }
            for (int a = 0; a < authorsArray.size(); a++) {
                JsonElement authorElement = authorsArray.get(a);
                String authorField = authorElement.toString();
                authorField = authorField.substring(1, authorField.length() - 1);
                authors.add(authorField);
            }
            // Check isbn exists
            JsonArray identifiers = volumeInfo.getAsJsonArray("industryIdentifiers");
            if (identifiers == null) {
                continue;
            }
            for (int z = 0; z < identifiers.size(); z++) {
                JsonElement isbnElement = identifiers.get(z).getAsJsonObject().get("identifier");
                String isbnField = isbnElement.toString();
                // We want ISBN_13
                if (isbnField.length() == 13 + 2) {
                    // Remove quotes
                    isbn = isbnField.substring(1, 14);
                    break;
                }
            }
            // Check publisher exists
            JsonElement publisherField = volumeInfo.get("publisher");
            if (publisherField == null) {
                continue;
            }
            publisher = publisherField.toString();
            publisher = publisher.substring(1, publisher.length() - 1);
            // TODO: grab page count and publish date for book info object
            String publishDate = volumeInfo.get("publishedDate").toString();
            int pages = Integer.parseInt(volumeInfo.get("pageCount").toString());
            hits.add(new BookInfo(isbn, title, authors, publisher, publishDate, pages));

            // Add to hits by creating a BookInfo object
            //hits.add(new BookInfo(title, authors, isbn, publisher));
        }
        return hits;
    }

    // TODO: remove test method
    public static void main(String[] args) {
        BookAPIStore bookAPIStore = new BookAPIStore();
        Map<String, BookInfo> search = bookAPIStore.searchBooks("Harry Potter", new ArrayList<>(), "*", "*", "*");
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
        System.out.println(result);
    }

}
