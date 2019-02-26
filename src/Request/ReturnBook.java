package Request;

import Checkout.CheckoutDB;

public class ReturnBook implements Request {

    private CheckoutDB checkoutDB;
    private String params;

    public ReturnBook(CheckoutDB checkoutDB, String params) {
        this.checkoutDB = checkoutDB;
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
