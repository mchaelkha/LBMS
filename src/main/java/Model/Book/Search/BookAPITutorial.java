package Model.Book.Search;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Google Books API tutorial. Demonstrating a single search request to the google
 * book API. You will need a key which can be retrieved by going to the google API manager
 * console: https://console.cloud.google.com/
 * @author Michael Kha
 */
public class BookAPITutorial {

    /**
     * Parts of the url you will need to create a connection.
     */
    private static final String KEY = "&key=AIzaSyDw1I-JjSgEU8TkPkU7g3cjKXk6BAsu-do";
    private static final String URL = "https://www.googleapis.com/books/v1/volumes";
    // See this page for search parameters: https://developers.google.com/books/docs/v1/using
    private static final String QUERY = "?q=flowers+inauthor:keyes";

    /**
     * Run this and see some JSON response by the API.
     */
    public static void main(String[] args) throws IOException {
        URL BookURL = new URL(URL + QUERY + KEY);
        HttpURLConnection conn = (HttpURLConnection) BookURL.openConnection();
        // We are performing a GET request
        conn.setRequestMethod("GET");
        // Time to read the response
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        // Build a single one line string of JSON
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        // Some pretty print formatting of JSON
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(response.toString());
        String prettyResponse = gson.toJson(je);
        // Finally print the request out
        System.out.println(prettyResponse);
    }
}
