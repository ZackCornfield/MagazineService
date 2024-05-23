package magazine_service;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * The MagazineService class manages the magazine service operations, including sending emails,
 * calculating costs, adding and removing customers.
 * @author Zack Cornfield
 */
public class MagazineService implements Serializable {
    private Magazine magazine;
    private List<Customer> customers;

    /**
     * Constructs a MagazineService object with the specified magazine and list of customers.
     * @param magazine The magazine object containing details of supplements.
     * @param customers The list of customers subscribed to the magazine service.
     */
    public MagazineService(Magazine magazine, List<Customer> customers) {
        this.magazine = magazine;
        this.customers = customers; 
    }
    
    /**
     * Gets the list of customers.
     * @return The list of customers.
     */
    public List<Customer> getCustomers() {
        return customers; 
    }
    
    /**
     * Gets the list of paying customers.
     * @return The list of paying customers.
     */
    public List<PayingCustomer> getPayingCustomers() {
        List<PayingCustomer> payingCustomers = new ArrayList<>();
        for (Customer customer : customers) {
            if (customer instanceof PayingCustomer) {
                payingCustomers.add((PayingCustomer) customer);
            }
        }
        return payingCustomers;
    }

    /**
     * Gets the list of associate customers.
     * @return The list of associate customers.
     */
    public List<AssociateCustomer> getAssociateCustomers() {
        List<AssociateCustomer> associateCustomers = new ArrayList<>();
        for (Customer customer : customers) {
            if (customer instanceof AssociateCustomer) {
                associateCustomers.add((AssociateCustomer) customer);
            }
        }
        return associateCustomers;
    }
    
    public Magazine getMagazine() {
        return magazine; 
    }

    /**
     * Adds a new customer to the list of customers.If the customer is an associate customer, it associates them with the corresponding paying customer.
     * @param customer The customer to be added.
     * @return Boolean.
     */
    public boolean addCustomer(Customer customer) {
        if (customer instanceof AssociateCustomer) {
            AssociateCustomer associateCustomer = (AssociateCustomer)customer; 
            PayingCustomer payingCustomer = findPayingCustomerByName(associateCustomer.getPayingCustomer().getName());
            if (payingCustomer != null) {
                payingCustomer.addAssociateCustomer(associateCustomer);
                customers.add(customer);
                return true;
            } else {
                return false; // Paying customer not found
            }
        } else {
            customers.add(customer);
            return true;
        }
    }

    /**
     * Removes a customer from the list of customers.
     * @param customerToRemove The customer to be removed.
     */
    public void removeCustomer(Customer customerToRemove) {
        if (customerToRemove instanceof PayingCustomer) {
            PayingCustomer payingCustomerToRemove = (PayingCustomer) customerToRemove;

            // Use an Iterator to safely remove AssociateCustomer instances
            Iterator<AssociateCustomer> iterator = payingCustomerToRemove.getAssociateCustomers().iterator();
            while (iterator.hasNext()) {
                AssociateCustomer associateCustomer = iterator.next();
                iterator.remove(); // Remove from the list of associate customers
                customers.remove(associateCustomer); // Remove from the main customer list
            }

        } else if (customerToRemove instanceof AssociateCustomer) {
            AssociateCustomer associateCustomerToRemove = (AssociateCustomer) customerToRemove;
            if (associateCustomerToRemove.getPayingCustomer() != null) {
                associateCustomerToRemove.getPayingCustomer().removeAssociateCustomer(associateCustomerToRemove);
            }
        }

        customers.remove(customerToRemove);
    }

    /**
     * Finds a paying customer by name.
     * @param payingCustomerName The name of the paying customer to find.
     * @return The PayingCustomer object if found, or null otherwise.
     */
    public PayingCustomer findPayingCustomerByName(String payingCustomerName) {
        for (Customer customer : customers) {
            if (customer instanceof PayingCustomer) {
                PayingCustomer payingCustomer = (PayingCustomer) customer;
                if (payingCustomer.getName().equals(payingCustomerName)) {
                    return payingCustomer;
                }
            }
        }
        return null; // Paying customer not found
    }
}
