package Book;

import java.util.Comparator;

/**
 * Book info comparator for sorting by title in alphanumeric order.
 *
 * @author Michael Kha
 */
public class TitleComparator implements Comparator<BookInfo> {

    /**
     * Compare the books such that the titles are they are ordered
     * alphanumerically using the String class's lexicographic compare method.
     * @param b1 First book
     * @param b2 Second book
     * @return The title ordering
     */
    @Override
    public int compare(BookInfo b1, BookInfo b2) {
        String firstTitle = b1.getTitle();
        String secondTitle = b2.getTitle();
        return firstTitle.compareTo(secondTitle);
    }
}
