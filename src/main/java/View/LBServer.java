package View;

import Controller.ClientParser;
import Controller.Parser;
import Model.Client.AccountDB;
import Model.Book.BookDB;
import Model.Checkout.CheckoutDB;
import Controller.RequestParser;
import Model.Library.LibrarySystem;
import Model.Library.ReportGenerator;
import Model.Library.TimeKeeper;
import Model.Visitor.VisitorDB;
import javafx.application.Application;

import java.io.*;
import java.util.*;

/**
 * Main class to start the library book management system.
 * @author Michael Kha
 */
public class LBServer {

    /**
     * The path name to save and open files
     */
    private static final String PATH = "assets/";
    /**
     * Usage message for invalid arguments
     */
    private static final String USAGE = "Usage: LBServer {'CLI'|'GUI'} [FILE]";

    /**
     * The maintained, connected clients
     */
    private Set<String> clients;

    /**
     * Parser used to process possible requests
     */
    private Parser parser;
    /**
     * Input reader used as a view controller to read input from a view
     */
    private InputReader reader;
    /**
     * System that determines when the library is open or closed
     */
    private LibrarySystem library;
    /**
     * Database to keep track of accounts
     */
    private AccountDB accountDB;
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
     * Object used to update library time and to notify library when
     * to close and open (Library state transition)
     */
    private TimeKeeper timeKeeper;
    /**
     * Responsible for the creation of statistical reports
     */
    private ReportGenerator reportGenerator;

    /**
     * Create the main system by creating new databases.
     */
    public LBServer() {
        accountDB = AccountDB.getInstance();
        bookDB = BookDB.getInstance();
        visitorDB = VisitorDB.getInstance();
        checkoutDB = CheckoutDB.getInstance();
        timeKeeper = new TimeKeeper();
        reportGenerator = new ReportGenerator(timeKeeper,bookDB, visitorDB, checkoutDB);
        library = new LibrarySystem(visitorDB, timeKeeper, reportGenerator);
        timeKeeper.setLibrarySystemObserver(library);
        Parser requestParser = new RequestParser(library, timeKeeper, reportGenerator);
        clients = new HashSet<>();
        parser = new ClientParser(requestParser, clients);
        reader = InputReader.init(this, parser);
    }

    /**
     * Create the main system from existing databases.
     * @param accountDB The account database
     * @param bookDB The book database
     * @param visitorDB The visitor database
     * @param checkoutDB The checkout database
     * @param clients The clients of the server
     */
    public LBServer(AccountDB accountDB, BookDB bookDB, VisitorDB visitorDB,
                    CheckoutDB checkoutDB, Set<String> clients) {
        this.accountDB = accountDB;
        this.bookDB = bookDB;
        this.visitorDB = visitorDB;
        this.checkoutDB = checkoutDB;
        this.timeKeeper = new TimeKeeper();
        reportGenerator = new ReportGenerator(timeKeeper,bookDB, visitorDB, checkoutDB);
        library = new LibrarySystem(visitorDB, timeKeeper, reportGenerator);
        Parser requestParser = new RequestParser(library, timeKeeper, reportGenerator);
        parser = new ClientParser(requestParser, clients);
        timeKeeper.setLibrarySystemObserver(library);
        reader = InputReader.init(this, parser);
    }

    /**
     * Start the server by continuing to read input from the reader.
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        String next;
        while (scanner.hasNextLine()) {
            next = scanner.nextLine();
            String response = reader.read(next);
            System.out.println(response);
        }
        System.exit(0);
    }

    public void exit() {
        System.exit(0);
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
            items.add(accountDB);
            items.add(bookDB);
            items.add(visitorDB);
            items.add(checkoutDB);
            items.add(clients);
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
    public static LBServer restore(String file) {
        AccountDB accountDB;
        BookDB bookDB;
        VisitorDB visitorDB;
        CheckoutDB checkoutDB;
        Set<String> clients;
        try {
            FileInputStream fis = new FileInputStream(PATH + file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            // Suppress unchecked casting here
            ArrayList<Object> items = (ArrayList<Object>) ois.readObject();
            accountDB = (AccountDB) items.get(0);
            bookDB = (BookDB) items.get(1);
            visitorDB = (VisitorDB) items.get(2);
            checkoutDB = (CheckoutDB) items.get(3);
            clients = (Set<String>) items.get(4);
            return new LBServer(accountDB, bookDB, visitorDB, checkoutDB, clients);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Start the library book management system.
     * Arguments determine run mode:
     * 1. One argument: CLI/GUI - start new management system from the specified view
     * 2. Two arguments: CLI/GUI FILE - restore system from a clean shutdown
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        int argc = args.length;
        LBServer server = null;
        if (argc == 0 || argc > 2) {
            System.err.println(USAGE);
            System.exit(1);
        }
        switch (argc) {
            case 1:
                server = new LBServer();
                break;
            case 2:
                server = restore(args[1]);
                break;
        }

        switch (args[0]) {
            case "CLI":
                Objects.requireNonNull(server).start();
                break;
            case "GUI":
                new Thread(() -> Application.launch(LibGUI.class)).start();
                break;
        }
    }

}
