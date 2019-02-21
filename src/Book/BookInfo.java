package Book;

import java.util.List;

public class BookInfo {

    String isbn;
    String title;
    List<String> authors;
    String publisher;
    String publishDate;
    int totalNumCopies;
    int totalCopiesAvailable;

    /**
     * Adds a copy of this book to the library.
     */
    public void addCopy(){

    }

    /**
     * Checks out a copy of this book from the library.
     * @return Whether the book was successfully checked out.
     */
    public boolean checkOutCopy(){
        return false;
    }

    /**
     * Returns a copy of this book to the library.
     */
    public void returnCopy(){

    }

    /**
     * Gives information about this book
     * @return a String containing information about the book.
     */
    public String getInfo(){
        return "";
    }

    /**
     * Chooses the method to compare two books.
     * @param compare The method to compare by.
     */
    public void setComparator(String compare){

    }
}
