import Book.BookDB;
import Checkout.CheckoutDB;
import Visitor.VisitorDB;

import java.io.*;
import java.util.ArrayList;

/**
 * Main class to start the library book management system.
 * TODO: add library system and time keeper possibly
 * @author Michael Kha
 */
public class LBServer {

    /**
     * The path name to save and open files
     */
    private static final String PATH = "assets/";

    /**
     * Database to keep track of books
     */
    private BookDB bookDB;
    /**
     * Database to keep track of visitors
     */
    private VisitorDB visitorDB;
    /**
     * Database to keep track of book checkouts by visitors
     */
    private CheckoutDB checkoutDB;

    /**
     * Create the main system by creating new databases.
     */
    public LBServer() {
        bookDB = new BookDB();
        visitorDB = new VisitorDB();
        checkoutDB = new CheckoutDB(visitorDB, bookDB);
    }

    /**
     * Create the main system from existing databases.
     * @param bookDB The book database
     * @param visitorDB The visitor database
     * @param checkoutDB The checkout database
     */
    public LBServer(BookDB bookDB, VisitorDB visitorDB, CheckoutDB checkoutDB) {
        this.bookDB = bookDB;
        this.visitorDB = visitorDB;
        this.checkoutDB = checkoutDB;
    }

    /**
     * Save the state of main system by serializing the databases to the file.
     * @param file The file to save to
     */
    public void shutdown(String file) {
        try {
            FileOutputStream fos = new FileOutputStream(PATH + file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            ArrayList<Object> items = new ArrayList<>();
            items.add(bookDB);
            items.add(visitorDB);
            items.add(checkoutDB);
            oos.writeObject(items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restore the main system by reading a properly serialized object file.
     * @param file Serialized object file
     */
    @SuppressWarnings("unchecked exception")
    public void restore(String file) {
        try {
            FileInputStream fis = new FileInputStream(PATH + file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            // Suppress unchecked casting here
            ArrayList<Object> items = (ArrayList<Object>) ois.readObject();
            bookDB = (BookDB) items.get(0);
            visitorDB = (VisitorDB) items.get(1);
            checkoutDB = (CheckoutDB) items.get(2);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Start the library book management system.
     * Arguments determine run mode:
     * 1. No args: start new management system
     * 2. Two arguments - "open FILE": restore system from a clean shutdown
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        int argc = args.length;
        switch (argc) {
            case 0:
                break;
            case 2:
                break;
            default:
                break;
        }
    }

}
