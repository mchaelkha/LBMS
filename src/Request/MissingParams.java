package Request;

public class MissingParams implements Request {
    @Override
    public String checkParams(String params) {
        return "";
    }

    @Override
    public String execute() {
        return null;
    }
}
