package Library;

public interface LibraryState {
    String checkoutBook(String visitorID);
    String startVisit(String visitorID);
}