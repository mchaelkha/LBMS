package Request;

import Book.BookDB;

public class LibraryBookSearch implements Request {

    private BookDB bookDB;
    private String params;

    public LibraryBookSearch(BookDB bookDB, String params) {
        this.bookDB = bookDB;
        this.params = params;
    }

    @Override
    public String checkParams() {
        return "";
    }

    @Override
    public String execute() {
        return null;
    }
}
