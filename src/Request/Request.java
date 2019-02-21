package Request;

public interface Request {
    void collectDependencies();
    String execute();
}
