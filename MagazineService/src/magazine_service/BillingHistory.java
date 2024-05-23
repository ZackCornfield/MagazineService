package magazine_service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The BillingHistory class represents the billing history of a paying customer.
 * It contains information about the bills generated for the customer.
 * Each bill includes details about the subscribed supplements and associated customers,
 * along with the total cost for that month.
 * 
 * @author Zack Cornfield
 */
public class BillingHistory implements Serializable {
    private List<Bill> bills;

    /**
     * Default constructor for BillingHistory class.
     * Initializes the attributes with default values.
     */
    public BillingHistory() {
        this.bills = new ArrayList<>();
    }

    /**
     * Retrieves the list of bills in the billing history.
     * 
     * @return The list of bills.
     */
    public List<Bill> getBills() {
        return bills;
    }

    /**
     * Generates a bill for the current month based on the subscribed supplements and
     * associated customers costs, and adds it to the billing history.
     * 
     * @param subscribedSupplements The list of subscribed supplements.
     * @param associateCustomers The list of associated customers.
     * @param paymentMethod PaymentMethod instance 
     * @param magazineCost Cost of the magazine 
     */
    public void generateBill(List<Supplement> subscribedSupplements, List<AssociateCustomer> associateCustomers, PaymentMethod paymentMethod, double magazineCost) {
        Bill bill = new Bill(subscribedSupplements, associateCustomers, paymentMethod, magazineCost);
        bills.add(bill);
    }
    
    /**
     * Prints the billing history in a nice format.
     */
    public void printBillingHistory() {
        System.out.println("Billing History:");
        System.out.println("-----------------------------------------------------------------");
        for (int i = 0; i < bills.size(); i++) {
            Bill bill = bills.get(i);
            System.out.println("Bill " + (i + 1) + ":");
            System.out.println("Subscribed Supplements:");
            for (Supplement supplement : bill.getSubscribedSupplements()) {
                System.out.println("- " + supplement.getName() + ": $" + supplement.getWeeklyCost() * 4);
            }
            System.out.println("Associated Customers:");
            for (AssociateCustomer associateCustomer : bill.getAssociateCustomers()) {
                System.out.println("- " + associateCustomer.getName() + ": $" + bill.calculateAssociateCustomerCost(associateCustomer));
            }
            System.out.println("Magazine Cost: $" + bill.getMagazineCost());
            System.out.println("Total Cost: $" + bill.getTotalCost());
            System.out.println("Payment Method: ");
            System.out.println("Payment Type: " + bill.getPaymentMethod().getPaymentType()); 
            if(bill.getPaymentMethod().getPaymentType() == PaymentMethod.PaymentType.CREDIT_DEBIT_CARD) {
                System.out.println("• Credit Card: " + bill.getPaymentMethod().getAccountNumber()); 
            }
            else
            {
                System.out.println("• Bank Account: " + bill.getPaymentMethod().getAccountNumber()); 
            }
            System.out.println("-----------------------------------------------------------------");
        }
    }
}

/**
 * The Bill class represents a bill for a paying customer.
 * It includes details about the subscribed supplements, associated customers,
 * and calculates the total cost for that month.
 * 
 * @author Zack Cornfield
 */
class Bill implements Serializable {
    private List<Supplement> subscribedSupplements;
    private List<AssociateCustomer> associateCustomers;
    private double magazineCost; 
    private double totalCost;
    private PaymentMethod paymentMethod; 

    public Bill(List<Supplement> subscribedSupplements, List<AssociateCustomer> associateCustomers, PaymentMethod paymentMethod, double magazineCost) {
        this.subscribedSupplements = subscribedSupplements;
        this.associateCustomers = associateCustomers;
        this.paymentMethod = paymentMethod; 
        this.magazineCost = magazineCost; 
        this.totalCost = calculateTotalCost();
    }

    /**
     * Calculates the total cost of this bill.
     * 
     * @return The total cost of the bill.
     */
    public double calculateTotalCost() {
        double total = magazineCost; 

        // Calculate the cost of subscribed supplements
        for (Supplement supplement : subscribedSupplements) {
            total += supplement.getWeeklyCost() * 4; // Assuming 4 weeks in a month
        }

        // Calculate the cost of associated customers
        for (AssociateCustomer associateCustomer : associateCustomers) {
            total += calculateAssociateCustomerCost(associateCustomer);
        }

        return total;
    }

    /**
     * Calculates the total cost of a specific associate customer for this bill.
     * 
     * @param associateCustomer The associate customer for whom to calculate the cost.
     * @return The total cost of the associate customer for this bill.
     */
    public double calculateAssociateCustomerCost(AssociateCustomer associateCustomer) {
        double total = 0;

        // Add the cost of subscribed supplements for the associate customer
        for (Supplement supplement : associateCustomer.getSubscribedSupplements()) {
            total += supplement.getWeeklyCost() * 4; // Assuming 4 weeks in a month
        }
        
        total += magazineCost; 

        return total;
    }

    /**
     * Retrieves the list of subscribed supplements in this bill.
     * 
     * @return The list of subscribed supplements.
     */
    public List<Supplement> getSubscribedSupplements() {
        return subscribedSupplements;
    }

    /**
     * Retrieves the list of associate customers in this bill.
     * 
     * @return The list of associate customers.
     */
    public List<AssociateCustomer> getAssociateCustomers() {
        return associateCustomers;
    }
    
    public PaymentMethod getPaymentMethod() {
        return paymentMethod; 
    }
    
    /**
     * Retrieves the cost of the magazine.
     * 
     * @return The cost of the magazine.
     */
    public double getMagazineCost() {
        return magazineCost; 
    }

    /**
     * Retrieves the total cost of this bill.
     * 
     * @return The total cost of the bill.
     */
    public double getTotalCost() {
        return totalCost;
    }
}
