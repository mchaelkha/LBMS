package Request;

import Book.BookDB;

public class BookPurchase implements Request {

    private BookDB bookDB;
    private String params;

    public BookPurchase(BookDB bookDB, String params) {
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
