package magazine_service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

/**
 * Controller class for the view tab in the magazine service application.
 * Handles viewing the customer and supplement information, along with bill history. 
 */
public class ViewTabController {

    @FXML
    private ListView<Supplement> supplementList;

    @FXML
    private ListView<Customer> customerList;

    @FXML
    private TextArea informationPanel;

    private MagazineService magazineService;

    /**
     * Setter for the MagazineService.
     * 
     * @param magazineService the MagazineService to be set
     */
    public void setMagazineService(MagazineService magazineService) {
        this.magazineService = magazineService;
        initializeUIComponents();
    }

    /**
     * Initializes the UI components with data from the MagazineService.
     */
    private void initializeUIComponents() {
        // Clear selection in supplement list and customer list
        supplementList.getSelectionModel().clearSelection();
        customerList.getSelectionModel().clearSelection();

        // Convert List<Supplement> to ObservableList<Supplement> and set to supplement list
        ObservableList<Supplement> supplementObservableList = FXCollections.observableArrayList(magazineService.getMagazine().getSupplements());
        supplementList.setItems(supplementObservableList);

        // Convert List<Customer> to ObservableList<Customer> and set to customer list
        ObservableList<Customer> customerObservableList = FXCollections.observableArrayList(magazineService.getCustomers());
        customerList.setItems(customerObservableList);

        // Customize the cell factory for the supplement list to display only names
        supplementList.setCellFactory((ListView<Supplement> param) -> new ListCell<Supplement>() {
            @Override
            protected void updateItem(Supplement item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getName());
                } else {
                    setText(null);
                }
            }
        });

        // Customize the cell factory for the customer list to display only names
        customerList.setCellFactory((ListView<Customer> param) -> new ListCell<Customer>() {
            @Override
            protected void updateItem(Customer item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getName());
                } else {
                    setText(null);
                }
            }
        });

        // Set event listeners for the supplement list
        supplementList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displaySupplementInformation(newValue);
            }
        });

        // Set event listeners for the customer list
        customerList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayCustomerInformation(newValue);
            }
        });
    }


    private void displaySupplementInformation(Supplement supplement) {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(supplement.getName()).append("\n");
        sb.append("Weekly Cost: ").append(supplement.getWeeklyCost()).append("\n");
        
        sb.append("Subscribed Customers: \n");
        for(Customer customer : magazineService.getCustomers()) {
            if (customer.getSubscribedSupplements().contains(supplement)) {
                sb.append("• ").append(customer.getName()).append("\n");
            }
        }
        
        // Add more information if needed
        informationPanel.setText(sb.toString());
    }
    
    private void displayCustomerInformation(Customer customer) {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(customer.getName()).append("\n");
        sb.append("Email: ").append(customer.getEmailAddress()).append("\n");
        sb.append("Address: ").append(customer.getStreetNumber()).append(" ")
                .append(customer.getStreetName()).append(", ").append(customer.getSuburb()).append(", ")
                .append(customer.getPostcode()).append("\n");

        // Check customer type and display relevant information
        if (customer instanceof PayingCustomer) {
            PayingCustomer payingCustomer = (PayingCustomer) customer;
            sb.append("Payment Type: ").append(payingCustomer.getPaymentMethod().getPaymentType()).append("\n");
            sb.append("Status: Paying Customer\n\n");
            sb.append("Associated Customers:\n");
            for (Customer assocCustomer : payingCustomer.getAssociateCustomers()) {
                sb.append("• ").append(assocCustomer.getName()).append("\n");
            }
            sb.append("\n");
            sb.append("Subscriptions:\n");
            for (Supplement subscribedSupplement : customer.getSubscribedSupplements()) {
                sb.append("• ").append(subscribedSupplement.getName()).append("\n");
            }
            sb.append("\n");
            sb.append(payingCustomer.getBillingHistory().getBillingHistory());
        } else if (customer instanceof AssociateCustomer) {
            sb.append("Subscriptions:\n");
            for (Supplement subscribedSupplement : customer.getSubscribedSupplements()) {
                sb.append("• ").append(subscribedSupplement.getName()).append("\n");
            }
            sb.append("\n");
            sb.append("Status: Associate Customer\n");
            sb.append("Your bills have been covered\n\n");
        }

        informationPanel.setText(sb.toString());
    }

}
