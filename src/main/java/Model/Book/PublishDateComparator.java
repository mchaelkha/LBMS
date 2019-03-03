package Model.Book;

import java.util.Comparator;

/**
 * Book info comparator for sorting from newest publishing date to oldest.
 *
 * @author Michael Kha
 */
public class PublishDateComparator implements Comparator<BookInfo> {

    /**
     * Compare the books such that the order is descending by publish
     * date.
     * @param b1 First book
     * @param b2 Second book
     * @return The publish date ordering
     */
    @Override
    public int compare(BookInfo b1, BookInfo b2) {
        String[] firstDate = b1.getPublishDate().split("-");
        String[] secondDate = b2.getPublishDate().split("-");
        // Compare years
        int firstYear = Integer.parseInt(firstDate[0]);
        int secondYear = Integer.parseInt(secondDate[0]);
        int yearDiff = secondYear - firstYear;
        // If there exists a difference or only publish date only has year
        if (yearDiff != 0 || (firstDate.length == 1 ||
                secondDate.length == 1)) {
            return yearDiff;
        }
        // Otherwise same year; check months
        // First trim 0 from months
        firstDate[1] = trimLeadingZero(firstDate[1]);
        secondDate[1] = trimLeadingZero(secondDate[1]);
        // Compare months
        int firstMonth = Integer.parseInt(firstDate[1]);
        int secondMonth = Integer.parseInt(secondDate[1]);
        int monthDiff = secondMonth - firstMonth;
        if (monthDiff != 0) {
            return monthDiff;
        }
        // Otherwise same month; check days
        // First trim 0 from days;
        firstDate[2] = trimLeadingZero(firstDate[2]);
        secondDate[2] = trimLeadingZero(secondDate[2]);
        // Compare days
        int firstDay = Integer.parseInt(firstDate[2]);
        int secondDay = Integer.parseInt(secondDate[2]);
        return secondDay - firstDay;
    }

    /**
     * Remove leading zero character from the string.
     * @param subject String to perform on
     * @return The possibly new substring
     */
    private String trimLeadingZero(String subject) {
        if (subject.charAt(0) == '0') {
            subject = subject.substring(1);
        }
        return subject;
    }
}
