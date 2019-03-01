package Library;

import java.time.LocalDateTime;

/**
 * The state of the Library when it is closed. Checkouts and visits not allowed.
 *
 * @author Luis Gutierrez
 */
public class LibraryClosed implements LibraryState {


    @Override
    public String checkoutBook(LocalDateTime checkoutDate, String visitorID, String isbn) {
        return null;
    }

    @Override
    public String startVisit(String visitorID) {
        return null;
    }
}
