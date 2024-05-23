package magazine_service;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * The PayingCustomer class represents a paying customer in the magazine service.
 * It extends the Customer class and includes information about the payment method
 * and associated customers.
 * @author Zack Cornfield
 */
public class PayingCustomer extends Customer implements Serializable{
    private PaymentMethod paymentMethod;
    private List<AssociateCustomer> associateCustomers;
    private BillingHistory billingHistory;
    
    /**
     * Default constructor for PayingCustomer class.
     * Initializes the attributes with default values.
     */
    public PayingCustomer() {
        super(); 
        this.paymentMethod = new PaymentMethod(); 
        this.associateCustomers = new ArrayList<>(); 
        this.billingHistory = new BillingHistory();
    }

    /**
     * Parameterized constructor for PayingCustomer class.
     * 
     * @param associateCustomers The list of associate customers associated with this paying customer.
     * @param name The name of the paying customer.
     * @param emailAddress The email address of the paying customer.
     * @param streetNumber The street number of the paying customer's address.
     * @param streetName The street name of the paying customer's address.
     * @param suburb The suburb of the paying customer's address.
     * @param postcode The postcode of the paying customer's address.
     * @param paymentMethod The payment method of the paying customer.
     * @param subscribedSupplements The list of supplements subscribed by the paying customer.
     */
    public PayingCustomer(List<AssociateCustomer> associateCustomers, String name, String emailAddress, int streetNumber, String streetName, String suburb, String postcode, PaymentMethod paymentMethod, List<Supplement> subscribedSupplements) {
        super(name, emailAddress, streetNumber, streetName, suburb, postcode, subscribedSupplements);
        this.paymentMethod = paymentMethod;
        this.associateCustomers = associateCustomers;
        this.billingHistory = new BillingHistory();
    }  
        
    /**
     * Retrieves the payment method of the paying customer.
     * 
     * @return The payment method of the paying customer.
     */
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    /**
     * Retrieves the billing history of the paying customer.
     * 
     * @return The billing history of the paying customer.
     */
    public BillingHistory getBillingHistory() {
        return billingHistory;
    }

    /**
     * Sets the payment method of the paying customer.
     * 
     * @param paymentMethod The payment method to set.
     */
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * Retrieves the list of associated customers.
     * 
     * @return The list of associated customers.
     */
    public List<AssociateCustomer> getAssociateCustomers() {
        return associateCustomers;
    }

    /**
     * Sets the list of associated customers.
     * 
     * @param associateCustomers The list of associated customers to set.
     */
    public void setAssociateCustomers(List<AssociateCustomer> associateCustomers) {
        this.associateCustomers = associateCustomers;
    }

    /**
     * Adds an associate customer to the list of associated customers.
     * @param customer The associate customer to add.
     */
    public void addAssociateCustomer(AssociateCustomer customer) {
        associateCustomers.add(customer);
    }

    /**
     * Removes an associate customer from the list of associated customers.
     * @param customer The associate customer to remove.
     */
    public void removeAssociateCustomer(AssociateCustomer customer) {
        associateCustomers.remove(customer);
    }
    
    /**
     * Generates a bill for the current month and adds it to the billing history.
     * 
     * @param magazineCost The cost of the magazine.    
     */
    public void generateBill(double magazineCost) {
        billingHistory.generateBill(getSubscribedSupplements(), associateCustomers, paymentMethod, magazineCost);
    }
}
