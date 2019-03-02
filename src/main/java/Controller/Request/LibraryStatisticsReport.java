package Controller.Request;

import Model.Library.ReportGenerator;

public class LibraryStatisticsReport implements Request{

    private ReportGenerator reporter;
    private String params;

    public LibraryStatisticsReport(ReportGenerator reporter, String params) {
        this.reporter = reporter;
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
