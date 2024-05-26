package magazine_service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;

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
     * @param payingCustomer Paying Customer to generate bill for 
     * @param magazineCost Cost of the magazine 
     */
    public void generateBill(PayingCustomer payingCustomer, double magazineCost) {
        Bill bill = new Bill(payingCustomer.getName(), payingCustomer.getEmailAddress(), payingCustomer.getSubscribedSupplements(), payingCustomer.getAssociateCustomers(), payingCustomer.getPaymentMethod(), magazineCost);
        bills.add(bill);
    }
    
    /**
     * Returns the billing history in a string 
     * @return String
     */
    public String getBillingHistory() {
        StringBuilder sb = new StringBuilder();
        sb.append("Billing History:\n");
        sb.append("-----------------------------------------------------------------\n");

        // Create a thread pool
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        CompletionService<String> completionService = new ExecutorCompletionService<>(executor);

        // Submit tasks to print each bill in a separate thread
        for (int i = 0; i < bills.size(); i++) {
            final int index = i;
            completionService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return generateBillString(index);
                }
            });
        }

        // Wait for all tasks to complete and append the results to the StringBuilder
        for (int i = 0; i < bills.size(); i++) {
            try {
                Future<String> future = completionService.take();
                sb.append(future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        sb.append("-----------------------------------------------------------------\n");
        return sb.toString();
    }

    /**
     * Generates the billing history string for a given index 
     * @return String
     */
    private String generateBillString(int index) {
        StringBuilder sb = new StringBuilder();
        Bill bill = bills.get(index);
        sb.append("-- New Monthly Magazine Payment Email --\n");
        sb.append("Sent To: ").append(bill.getEmailAddress()).append("\n\n");
        sb.append("Dear ").append(bill.getName()).append(",\n\n");
        sb.append("We hope this email finds you well.\n\n");
        sb.append("Here is your monthly subscription summary:\n\n");
        sb.append("Subscribed Supplements:\n");
        for (Supplement supplement : bill.getSubscribedSupplements()) {
            sb.append("    • ").append(supplement.getName()).append(": $").append(supplement.getWeeklyCost() * 4).append("\n");
        }
        sb.append("\n");
        sb.append("Associated Customers:\n");
        for (AssociateCustomer assocCustomer : bill.getAssociateCustomers()) {
            sb.append("    • ").append(assocCustomer.getName()).append(": $").append(bill.calculateAssociateCustomerCost(assocCustomer)).append("\n");
        }
        sb.append("\n");
        sb.append("Magazine Cost: $").append(bill.getMagazineCost()).append("\n");
        sb.append("Total Cost: $").append(bill.getTotalCost()).append("\n");
        sb.append("Payment Method:\n");
        sb.append("    • Payment Type: ").append(bill.getPaymentMethod().getPaymentType()).append("\n");
        if (bill.getPaymentMethod().getPaymentType() == PaymentMethod.PaymentType.CREDIT_DEBIT_CARD) {
            sb.append("    • Credit Card: ").append(bill.getPaymentMethod().getAccountNumber()).append("\n");
        } else {
            sb.append("    • Bank Account: ").append(bill.getPaymentMethod().getAccountNumber()).append("\n");
        }
        sb.append("\n");
        sb.append("Thank you for your continued subscription.\n\n");
        sb.append("Best regards,\n");
        sb.append("Your Magazine Service Team\n\n");
        return sb.toString();
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
    private String name;
    private String emailAddress;

    public Bill(String name, String emailAddress, List<Supplement> subscribedSupplements, List<AssociateCustomer> associateCustomers, PaymentMethod paymentMethod, double magazineCost) {
        this.name = name; 
        this.emailAddress = emailAddress; 
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

    public String getName() {
        return name;
    }
    
    public String getEmailAddress() {
        return emailAddress; 
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
