package Model.Client;

public class VisitorRole implements Role {

    @Override
    public String advanceTime() {
        return NOT_AUTHORIZED;
    }

    @Override
    public String beginVisit() {
        return null;
    }

    @Override
    public String purchaseBook() {
        return NOT_AUTHORIZED;
    }

    @Override
    public String searchStore() {
        return NOT_AUTHORIZED;
    }

    @Override
    public String borrowBook() {
        return null;
    }

    @Override
    public String createAccount() {
        return NOT_AUTHORIZED;
    }

    @Override
    public String currentDateTime() {
        return NOT_AUTHORIZED;
    }

    @Override
    public String endVisit() {
        return null;
    }

    @Override
    public String findBorrowedBooks() {
        return NOT_AUTHORIZED;
    }

    @Override
    public String libraryBookSearch() {
        return null;
    }

    @Override
    public String libraryStatisticsReport() {
        return NOT_AUTHORIZED;
    }

    @Override
    public String login() {
        return null;
    }

    @Override
    public String logout() {
        return null;
    }

    @Override
    public String payFine() {
        return null;
    }

    @Override
    public String registerVisitor() {
        return NOT_AUTHORIZED;
    }

    @Override
    public String returnBook() {
        return null;
    }
}
