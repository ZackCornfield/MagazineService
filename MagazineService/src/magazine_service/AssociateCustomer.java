package magazine_service;

import java.io.Serializable;
import java.util.List; 

/**
 * Represents an associate customer who is associated with a paying customer.
 * Extends the Customer class.
 * 
 * @author Zack Cornfield
 */
public class AssociateCustomer extends Customer implements Serializable {
    private PayingCustomer payingCustomer; 
    
    /**
     * Default constructor for AssociateCustomer.
     */
    public AssociateCustomer() {
        super(); 
    }

    /**
     * Constructor for AssociateCustomer with parameters.
     * 
     * @param payingCustomer the paying customer associated with this associate customer
     * @param name the name of the associate customer
     * @param emailAddress the email address of the associate customer
     * @param streetNumber the street number of the associate customer's address
     * @param streetName the street name of the associate customer's address
     * @param suburb the suburb of the associate customer's address
     * @param postcode the postcode of the associate customer's address
     * @param subscribedSupplements the list of supplements subscribed by the associate customer
     */
    public AssociateCustomer(PayingCustomer payingCustomer, String name, String emailAddress, int streetNumber, String streetName, String suburb, String postcode, List<Supplement> subscribedSupplements) {
        super(name, emailAddress, streetNumber, streetName, suburb, postcode, subscribedSupplements);
        this.payingCustomer = payingCustomer;
    }
    
    /**
     * Retrieves the paying customer associated with this associate customer.
     * 
     * @return the paying customer associated with this associate customer
     */
    public PayingCustomer getPayingCustomer() {
        return payingCustomer;
    }
    
    /**
     * Sets the paying customer associated with this associate customer.
     * 
     * @param payingCustomer the paying customer to be associated with this associate customer
     */
    public void setPayingCustomer(PayingCustomer payingCustomer) {
        this.payingCustomer = payingCustomer; 
    }
}
