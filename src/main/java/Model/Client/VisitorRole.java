package Model.Client;

import Controller.Request.Request;
import Controller.Request.RequestUtil;

public class VisitorRole implements Role {

    @Override
    public String advanceTime() {
        return RequestUtil.NOT_AUTHORIZED + RequestUtil.DELIMITER;
    }

    @Override
    public String beginVisit() {
        Request request = new BeginVisit(null, null, null, null);
        return request;
    }

    @Override
    public String purchaseBook() {
        return RequestUtil.NOT_AUTHORIZED + RequestUtil.DELIMITER;
    }

    @Override
    public String searchStore() {
        return RequestUtil.NOT_AUTHORIZED + RequestUtil.DELIMITER;
    }

    @Override
    public String borrowBook() {
        return null;
    }

    @Override
    public String createAccount() {
        return RequestUtil.NOT_AUTHORIZED + RequestUtil.DELIMITER;
    }

    @Override
    public String currentDateTime() {
        return RequestUtil.NOT_AUTHORIZED + RequestUtil.DELIMITER;
    }

    @Override
    public String endVisit() {
        return null;
    }

    @Override
    public String findBorrowedBooks() {
        return RequestUtil.NOT_AUTHORIZED + RequestUtil.DELIMITER;
    }

    @Override
    public String libraryBookSearch() {
        return null;
    }

    @Override
    public String libraryStatisticsReport() {
        return RequestUtil.NOT_AUTHORIZED + RequestUtil.DELIMITER;
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
        return RequestUtil.NOT_AUTHORIZED + RequestUtil.DELIMITER;
    }

    @Override
    public String returnBook() {
        return null;
    }
}
