package Model.Book;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * The API bookstore that uses the Google Books API to search for books.
 *
 * @author Michael Kha
 */
public class BookAPIStore extends BookData {

    private static final String Q_TITLE = "intitle=";
    private static final String Q_AUTHOR = "inauthor=";
    private static final String Q_ISBN = "isbn=";
    private static final String Q_PUBLISHER = "inpublisher=";


    /**
     * Parts of the url you will need to create a connection.
     */
    private static final String KEY = "&key=AIzaSyDw1I-JjSgEU8TkPkU7g3cjKXk6BAsu-do";
    private static final String URL = "https://www.googleapis.com/books/v1/volumes";
    // See this page for search parameters: https://developers.google.com/books/docs/v1/using

    @Override
    public Map<String, BookInfo> searchBooks(String title,
                                             List<String> authors,
                                             String isbn,
                                             String publisher, String sort) {
        String query = createQuery(title, authors, isbn, publisher);
        try {
            URL BookURL = new URL(URL + query + KEY);
            HttpURLConnection conn = (HttpURLConnection) BookURL.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            Gson gson = new Gson();
            // TODO: finish writing
            gson.fromJson(in, BookInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
        if (!title.equals(ignore)) {
            query += Q_TITLE + replaceSpaces(title) + "&";
        }
        if (!authors.isEmpty()) {
            query += Q_AUTHOR;
            for (int i = 0; i < authors.size(); i++) {
                String author = authors.get(i);
                query += replaceSpaces(author);
                if (i == authors.size() - 1) {
                    query += "+";
                }
            }
        }
        if (!isbn.equals(ignore)) {
            query += Q_ISBN + isbn + "&";
        }
        if (!publisher.equals(ignore)) {
            query += Q_PUBLISHER + replaceSpaces(publisher) + "&";
        }
        // Always remove last "&" symbol
        return query.substring(0, query.length() - 1);
    }

    /**
     * Replace spaces so they are readable in the URL.
     * @param element Element to replace for
     * @return Element with %20 as a space
     */
    private String replaceSpaces(String element) {
        return element.replace(" ", "%20");
    }

}
