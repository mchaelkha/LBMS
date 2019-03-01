package Library;

import java.time.LocalDateTime;

/**
 * The state of the Library when it is open. Checkouts and visits are allowed.
 * 
 * @author Hersh Nagpal
 */
class LibraryOpen implements LibraryState {

    /**
     * Returns the given book for the given visitor.
     * @param visitorID the ID of the visitor checking out the books
     * @param isbn the isbn of the book to check out
     */
    @Override
    public String checkoutBook(LocalDateTime checkoutDate, String visitorID, String isbn) {

        return null;
    }

    /**
     * Starts a new visit for the given visitor, which allows them to access the library's services.
     * @param visitorID the visitor whose visit to start
     * @return a formatted string regarding the success of the operation.
     */
    @Override
    public String startVisit(String visitorID) {
        return null;
    }

} 