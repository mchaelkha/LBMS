package Request;

public class BorrowBook implements Request {
    @Override
    public boolean checkParams(String params) {
        return false;
    }

    @Override
    public String execute() {
        return null;
    }
}
