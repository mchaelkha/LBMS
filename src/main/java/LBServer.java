import Controller.ClientParser;
import Controller.Parser;
import Controller.Request.Request;
import Model.Client.AccountDB;
import Model.Book.BookDB;
import Model.Checkout.CheckoutDB;
import Controller.RequestParser;
import Model.Client.Client;
import Model.Library.LibrarySystem;
import Model.Library.ReportGenerator;
import Model.Library.TimeKeeper;
import Model.Visitor.VisitorDB;

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
    private static final String USAGE = "invalid arguments";
    /**
     * Command to shutdown the program by first saving
     */
    private static final String SHUTDOWN = "/shutdown";
    /**
     * Command to stop the program immediately
     */
    private static final String EXIT = "/exit";

    /**
     * The maintained, connected clients
     */
    private Map<String, Client> clients;

    /**
     * Parser used to process possible requests
     */
    private Parser parser;
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
        bookDB = new BookDB();
        visitorDB = new VisitorDB();
        checkoutDB = new CheckoutDB();
        timeKeeper = new TimeKeeper();
        reportGenerator = new ReportGenerator(timeKeeper,bookDB, visitorDB, checkoutDB);
        library = new LibrarySystem(visitorDB, timeKeeper, reportGenerator);
        timeKeeper.setLibrarySystemObserver(library);
        //TODO change back to ClientParser after testing reportGenerator
        Parser requestParser = new RequestParser(library, bookDB, visitorDB, checkoutDB, accountDB, timeKeeper, reportGenerator);
        clients = new HashMap<>();
        parser = new ClientParser(requestParser, clients);
    }

    /**
     * Create the main system from existing databases.
     * @param accountDB The account database
     * @param bookDB The book database
     * @param visitorDB The visitor database
     * @param checkoutDB The checkout database

     */
    public LBServer(AccountDB accountDB, BookDB bookDB, VisitorDB visitorDB,
                    CheckoutDB checkoutDB, Map<String, Client> clients) {
        this.accountDB = accountDB;
        this.bookDB = bookDB;
        this.visitorDB = visitorDB;
        this.checkoutDB = checkoutDB;
        this.timeKeeper = new TimeKeeper();
        reportGenerator = new ReportGenerator(timeKeeper,bookDB, visitorDB, checkoutDB);
        library = new LibrarySystem(visitorDB, timeKeeper, reportGenerator);
        Parser requestParser = new RequestParser(library, bookDB, visitorDB, checkoutDB, accountDB, timeKeeper, reportGenerator);
        parser = new ClientParser(requestParser, clients);
        timeKeeper.setLibrarySystemObserver(library);
    }

    /**
     * Start the server by continuing to read input from the command line.
     * Special commands:
     * 1. /shutdown FILE - Shutdowns the program by first saving
     * 2. /exit - Exit the program without saving
     * 3. Client connections: connect and disconnect
     * 4. requests in csv format - commands to run through the parser
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        String next;
        String[] parts;
        while (scanner.hasNextLine()) {
            next = scanner.nextLine();
            // Check for special commands
            if (next.matches("^" + SHUTDOWN + "\\s[\\w].*")) {
                parts = next.split(" ");
                shutdown(parts[1]);
                break;
            }
            if (next.matches("^" + EXIT)) {
                break;
            }
            // Next line must be a request to be processed
            Request request = parser.processRequest(next);
            // TODO: make accounts execute
            // TODO: make requests with optional visitorID param use clientID instead to find account/visitor
            // TODO: Add performed commands only if valid to stacks in accounts
            System.out.println(request.execute());
        }
        scanner.close();
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
        Map<String, Client> clients;
        try {
            FileInputStream fis = new FileInputStream(PATH + file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            // Suppress unchecked casting here
            ArrayList<Object> items = (ArrayList<Object>) ois.readObject();
            accountDB = (AccountDB) items.get(0);
            bookDB = (BookDB) items.get(1);
            visitorDB = (VisitorDB) items.get(2);
            checkoutDB = (CheckoutDB) items.get(3);
            clients = (Map<String, Client>) items.get(4);
            return new LBServer(accountDB, bookDB, visitorDB, checkoutDB, clients);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
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
        LBServer server = null;
        switch (argc) {
            case 0:
                server = new LBServer();
                break;
            case 2:
                if (!args[0].equals("open")) {
                    System.err.println(USAGE);
                    System.exit(1);
                }
                server = restore(args[1]);
                break;
            default:
                System.err.println(USAGE);
                System.exit(1);
                break;
        }
        Objects.requireNonNull(server).start();
    }

}
