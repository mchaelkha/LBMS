package main.java.Controller.Request;

import main.java.Model.Checkout.CheckoutDB;

public class PayFine implements Request {

    private CheckoutDB checkoutDB;
    private String params;

    public PayFine(CheckoutDB checkoutDB, String params) {
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
