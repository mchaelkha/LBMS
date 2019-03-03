package main.java.Model.Library;

import main.java.Controller.Request.RequestUtil;
import main.java.Model.Checkout.CheckoutDB;
import main.java.Model.Checkout.Transaction;
import main.java.Model.Visitor.VisitorDB;

import java.time.LocalDateTime;

/**
 * The state of the Library when it is open. Checkouts and visits are allowed.
 * 
 * @author Hersh Nagpal
 */
class LibraryOpen implements LibraryState,RequestUtil {

    /**
     * Returns the given book for the given visitor.
     * @param visitorID the ID of the visitor checking out the books
     * @param isbn the isbn of the book to check out
     * @param checkoutDate the current date of checkout
     */
    @Override
    public String checkoutBook(LocalDateTime checkoutDate, String visitorID, String isbn, CheckoutDB checkoutDB, VisitorDB visitorDB) {
        //Call checkoutBook in CheckoutDB

        //Make methods returning booleans for each check in VisitorDB
        //If all are true call checkoutDB

        //Check if visitor has outstanding fine
        if (checkoutDB.hasOutstandingFine(visitorID)) {
            int fineAmount = checkoutDB.calculateFine(visitorID);
            //return "borrow,outstanding-fine,amount"
            return BORROW_REQUEST + DELIMITER + OUTSTANDING_FINE + fineAmount;
        }
        //Check if visitor has book limit
        else if(checkoutDB.hasBookLimit(visitorID)){
            return BORROW_REQUEST+DELIMITER+BOOK_LIMIT_EXCEDED;
        }
        //Check if visitorID is a valid id
        else if(!visitorDB.validCurrentVisitor(visitorID)){
            return BORROW_REQUEST+DELIMITER+INVALID_VISITOR_ID;
        }
        else{
            //call checkout() in CheckoutDB
            Transaction transaction = checkoutDB.checkout(checkoutDate, visitorID, isbn);
            String dueDate = transaction.getDueDate();
            return BORROW_REQUEST+DELIMITER+dueDate;
        }
    }

    /**
     * Starts a new visit for the given visitor, which allows them to access the library's services.
     * @param visitorID the visitor whose visit to start
     * @return a formatted string regarding the success of the operation.
     */
    @Override
    public String beginVisit(String visitorID, VisitorDB visitorDB) {
        return visitorDB.beginVisit(visitorID);
    }

} 