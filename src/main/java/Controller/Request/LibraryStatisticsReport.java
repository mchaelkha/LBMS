package Controller.Request;

import Model.Library.LibrarySystem;

public class LibraryStatisticsReport implements Request{

    private LibrarySystem library;
    private String params;

    public LibraryStatisticsReport(LibrarySystem library, String params) {
        this.library = library;
        this.params = params;
    }

    @Override
    public String checkParams() {
        return "";
    }

    @Override
    public String execute() {
        return null;
    }
}
