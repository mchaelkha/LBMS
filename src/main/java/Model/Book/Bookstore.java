package main.java.Model.Book;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.*;

/**
 * Reads a properly formatted books file to initialize BookInfo objects to be
 * purchased by a library.
 *
 * @author Michael Kha
 */
public class Bookstore extends BookData implements Serializable {

    /**
     * The file path of the books file
     */
    private static final String STORE_PATH = "assets/books.txt";

    /**
     * Create a new bookstore by initializing the state.
     */
    public Bookstore() {
        super();
        init();
    }

    /**
     * Initialize the state of the bookstore by reading a book file.
     */
    private void init() {
        try (Scanner scanner = new Scanner(new File(STORE_PATH))) {
            String line;
            BookInfo bookInfo;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                bookInfo = generateBookInfoFromLine(line);
                books.put(bookInfo.getIsbn(), bookInfo);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate book info given a properly formatted line of info
     * @param line Line of book info
     * @return The book info that was generated
     */
    private BookInfo generateBookInfoFromLine(String line) {
        String isbn;
        String title;
        String publisher;
        String publishDate;
        int pageCount;
        // First split by left and right brackets
        String[] firstSplit = line.split("[{}]");
        // Second item of array will be all authors in csv format
        List<String> authors = new ArrayList<>(
                Arrays.asList(firstSplit[1].split(",")));
        // Split isbn and title
        String[] isbnTitle = firstSplit[0].split(",(?!\\s)");
        isbn = isbnTitle[0];
        title = isbnTitle[1];
        // Split publisher, publish date, page count
        String[] publishing = firstSplit[2].split(",(?!\\s)");
        publisher = publishing[1];
        publishDate = publishing[2];
        pageCount = Integer.parseInt(publishing[3]);
        // Create book info
        return new BookInfo(isbn, title, authors, publisher,
                publishDate, pageCount);
    }

    /**
     * Purchase books of a given quantity.
     * @param quantity Quantity to purchase
     * @param bookIDs List of book IDs
     * @return The list of book info that has been purchased
     */
    public List<BookInfo> purchaseBooks(int quantity, List<String> bookIDs) {
        List<BookInfo> bookInfoList = new ArrayList<>();
        for (String book : bookIDs) {
            BookInfo original = lastSearch.get(book);
            BookInfo copy = new BookInfo(original, quantity);
            bookInfoList.add(copy);
        }
        return bookInfoList;
    }

}
