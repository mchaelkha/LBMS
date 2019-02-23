package Book;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Reads a properly formatted books file to initialize BookInfo objects to be
 * purchased by a library.
 *
 * @author Michael Kha
 */
public class Bookstore {

    /**
     * The file path of the books file
     */
    private static final String STORE_PATH = "assets/books.txt";
    /**
     * Books to be purchased
     */
    private Map<String, BookInfo> books;
    /**
     * Last search of book info mapped to temporary book IDs.
     */
    private Map<String, BookInfo> lastSearch;

    /**
     * Create a new bookstore by initializing the state.
     */
    public Bookstore() {
        books = new HashMap<>();
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
        String[] isbnTitle = firstSplit[0].split(",");
        isbn = isbnTitle[0];
        title = isbnTitle[1];
        // Split publisher, publish date, page count
        String[] publishing = firstSplit[2].split(",");
        publisher = publishing[0];
        publishDate = publishing[1];
        pageCount = Integer.parseInt(publishing[2]);
        // Create book info
        return new BookInfo(isbn, title, authors, publisher,
                publishDate, pageCount);
    }

    /**
     * Search the books using a filter on all the available books for purchase.
     *
     * @param title Title search parameter
     * @param authors Authors search parameter
     * @param isbn ISBN search parameter
     * @param publisher Publisher search parameter
     * @param sort Sort the search by either title or publish-date
     * @return The mapping of hits to a unique ID
     */
    public Map<String, BookInfo> searchBooks(String title,
                                             List<String> authors,
                                             String isbn,
                                             String publisher, String sort) {
        Map<String, BookInfo> searchedBooks = new HashMap<>();
        // Filter out results into a list of search hits
        List<BookInfo> hits = books.values().stream()
                .filter(b -> matchingFilter(b, title, authors,
                        isbn, publisher))
                .collect(Collectors.toList());
        // TODO: Sort the search hits now
        switch (sort) {
            case "title":
                // Call title comparator
                break;
            case "publish-date":
                // Call publish date comparator
                break;
            default:
                // TODO: throw an invalid parameter exception
        }

        // Map to a unique ID for the hits
        int id = 0;
        for (BookInfo info : hits) {
            searchedBooks.put(String.valueOf(id), info);
            id++;
        }
        // Set last search to this recent search
        lastSearch = searchedBooks;
        return searchedBooks;
    }

    /**
     * Determines a matching between book info and its search parameters.
     * Parameters are ignored if they equal '*'. Authors are ignored if the
     * list is empty.
     * @param book Book info to compare with
     * @param title Title from search
     * @param authors Authors from search
     * @param isbn ISBN from search
     * @param publisher Publisher from search
     * @return If the book info has completely matched through each filter
     */
    private boolean matchingFilter(BookInfo book, String title,
                                   List<String> authors,
                                   String isbn, String publisher) {
        String bookTitle = book.getTitle();
        List<String> bookAuthors = book.getAuthors();
        String bookIsbn = book.getIsbn();
        String bookPublisher = book.getPublisher();
        String ignore = "*";
        if (!title.equals(ignore)) {
            // Title may only contain a substring
            if (!bookTitle.contains(title)) {
                return false;
            }
        }
        if (!authors.isEmpty()) {
            for (String author : bookAuthors) {
                // Authors must exactly match
                if (!authors.contains(author)) {
                    return false;
                }
            }
        }
        if (!isbn.equals(ignore)) {
            // ISBN must exactly match
            if (!bookIsbn.equals(isbn)) {
                return false;
            }
        }
        if (!publisher.equals(ignore)) {
            // Publisher must exactly match
            if (!bookPublisher.equals(publisher)) {
                return false;
            }
        }
        return true;
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
