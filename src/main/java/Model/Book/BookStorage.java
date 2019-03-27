package Model.Book;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Book info data structure that holds books to be searched for.
 *
 * @author Michael Kha
 */
public abstract class BookStorage {

    /**
     * Available books mapped to their IBSNs
     */
    Map<String, BookInfo> books;
    /**
     * Comparators for book info
     */
    private Comparator<BookInfo> byTitle;
    private Comparator<BookInfo> byPublishDate;

    /**
     * Create a new book data structure that can be used to search for books.
     */
    public BookStorage() {
        books = new HashMap<>();
        byTitle = new TitleComparator();
        byPublishDate = new PublishDateComparator();
    }

    /**
     * Search the books using a filter on all the available books for purchase.
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
        // Filter out results into a list of search hits
        List<BookInfo> hits = books.values().stream()
                .filter(b -> matchingFilter(b, title, authors,
                        isbn, publisher))
                .collect(Collectors.toList());
        // Sort the books by the specified parameter
        hits = sortBooks(hits, sort);
        if (hits == null) {
            return null;
        }
        return createMap(hits);
    }

    /**
     * Create a mapping of books from the list of books.
     * @param hits The list of books
     * @return The mapping of books from 0 to N books
     */
    public Map<String, BookInfo> createMap(List<BookInfo> hits) {
        Map<String, BookInfo> searchedBooks = new HashMap<>();
        // Map to a unique ID for the hits
        int id = 0;
        for (BookInfo info : hits) {
            searchedBooks.put(String.valueOf(id), info);
            id++;
        }
        return searchedBooks;
    }

    /**
     * Sort the books by the given sorting strategy.
     * @param hits The list of books to sort
     * @param sort The sorting strategy
     * @return If the strategy is invalid return null otherwise return the reordered list
     */
    public List<BookInfo> sortBooks(List<BookInfo> hits, String sort) {
        switch (sort) {
            case "*":
                break;
            case "title":
                hits.sort(byTitle);
                break;
            case "publish-date":
                hits.sort(byPublishDate);
                break;
            case "book-status":
                hits = hits.stream().filter(BookInfo::hasCopiesAvailable)
                        .collect(Collectors.toList());
                break;
            default:
                return null;
        }
        return hits;
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
            // Authors must match at least part of the book's authors
            for (String author : authors) {
                List<String> authorHits = bookAuthors.stream()
                        .filter(a -> a.contains(author))
                        .collect(Collectors.toList());
                if (authorHits.isEmpty()) {
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
}
