package Request;

public interface Request extends RequestUtil {
    String checkParams(String params);
    String execute();
}
