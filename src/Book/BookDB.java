package Book;

public class BookDB extends BookData {

    /**
     * Create a new book database that is empty.
     */
    public BookDB(){
        super();
    }

    /**
     * Purchase a new book for the library.
     * @param book The ISBN of the book being purchased.
     * @param info Everything else.
     */
    public void purchase(String book, String info){

    }

    /**
     * Get the number of copies of a book currently in the library.
     * @param book the ISBN of the book.
     * @return The number of copies of the book.
     */
    public int getNumCopies(String book){
        return 0;
    }

    /**
     * Removes a copy of a book from the library.
     * @param book the ISBN of the book.
     * @return Whether a copy of the book was removed.
     */
    public boolean removeCopy(String book){
        return false;
    }

    /**
     * Returns a copy of the book to the library.
     * @param book the ISBN of the book.
     */
    public void returnCopy(String book){
        if (books.containsKey(book))
            books.get(book).returnCopy();
    }

}
