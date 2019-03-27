package Model.Client;

import Controller.Request.RequestUtil;

/**
 * Implementation of the state pattern for the purpose of allowing different
 * behavior between visitor and employee requests.
 *
 * @author Michael Kha
 */
public interface Role extends RequestUtil {
    public String advanceTime();
    public String beginVisit();
    public String purchaseBook();
    public String searchStore();
    public String borrowBook();
    public String createAccount();
    public String currentDateTime();
    public String endVisit();
    public String findBorrowedBooks();
    public String libraryBookSearch();
    public String libraryStatisticsReport();
    public String login();
    public String logout();
    public String payFine();
    public String registerVisitor();
    public String returnBook();
}
