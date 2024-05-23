package magazine_service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;

/**
 * Controller class for the edit tab in the magazine service application.
 * Handles editing of customers and supplements 
 */
public class EditTabController {

    @FXML
    private TableView<Customer> editCustomerTable;

    @FXML
    private TableView<Supplement> editSupplementTable;

    private MagazineService magazineService;
    private ObservableList<Customer> customerData = FXCollections.observableArrayList();
    private ObservableList<Supplement> supplementData = FXCollections.observableArrayList();

    /**
     * Getter for the MagazineService.
     * 
     * @return the MagazineService
     */
    public MagazineService getMagazineService() {
        return magazineService;
    }

    /**
     * Setter for the MagazineService.
     * 
     * @param magazineService the MagazineService to be set
     */
    public void setMagazineService(MagazineService magazineService) {
        this.magazineService = magazineService;
        customerData.clear();
        supplementData.clear(); 
        editCustomerTable.getItems().clear();
        editSupplementTable.getItems().clear();
        editCustomerTable.getColumns().clear();
        editSupplementTable.getColumns().clear();
        initializeCustomerTable();
        initializeSupplementTable();
    }

    private void initializeCustomerTable() {
        customerData.addAll(magazineService.getCustomers());

        TableColumn<Customer, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Add listener to the table columns for edit commit events
        nameColumn.setOnEditCommit(event -> {
            Customer customer = event.getRowValue();
            String newValue = event.getNewValue();
            if (newValue == null || newValue.trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Name cannot be empty.");
                editCustomerTable.refresh();
            } else {
                customer.setName(newValue);
            }
        });

        TableColumn<Customer, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Add listener to the table columns for edit commit events
        emailColumn.setOnEditCommit(event -> {
            Customer customer = event.getRowValue();
            String newValue = event.getNewValue();
            if (newValue == null || newValue.trim().isEmpty() || !newValue.contains("@")) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a valid email address.");
                editCustomerTable.refresh();
            } else {
                customer.setEmailAddress(newValue);
            }
        });

        TableColumn<Customer, Integer> streetNumberColumn = new TableColumn<>("Street Number");
        streetNumberColumn.setCellValueFactory(new PropertyValueFactory<>("streetNumber"));
        streetNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        // Add listener to the table columns for edit commit events
        streetNumberColumn.setOnEditCommit(event -> {
            Customer customer = event.getRowValue();
            Integer newValue = event.getNewValue();
            if (newValue == null || newValue <= 0) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a valid street number greater than zero.");
                editCustomerTable.refresh();
            } else {
                customer.setStreetNumber(newValue);
            }
        });

        TableColumn<Customer, String> streetNameColumn = new TableColumn<>("Street Name");
        streetNameColumn.setCellValueFactory(new PropertyValueFactory<>("streetName"));
        streetNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Add listener to the table columns for edit commit events
        streetNameColumn.setOnEditCommit(event -> {
            Customer customer = event.getRowValue();
            String newValue = event.getNewValue();
            if (newValue == null || newValue.trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Street name cannot be empty.");
                editCustomerTable.refresh();
            } else {
                customer.setStreetName(newValue);
            }
        });

        TableColumn<Customer, String> suburbColumn = new TableColumn<>("Suburb");
        suburbColumn.setCellValueFactory(new PropertyValueFactory<>("suburb"));
        suburbColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Add listener to the table columns for edit commit events
        suburbColumn.setOnEditCommit(event -> {
            Customer customer = event.getRowValue();
            String newValue = event.getNewValue();
            if (newValue == null || newValue.trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Suburb cannot be empty.");
                editCustomerTable.refresh();
            } else {
                customer.setSuburb(newValue);
            }
        });

        TableColumn<Customer, String> postcodeColumn = new TableColumn<>("Postcode");
        postcodeColumn.setCellValueFactory(new PropertyValueFactory<>("postcode"));
        postcodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Add listener to the table columns for edit commit events
        postcodeColumn.setOnEditCommit(event -> {
            Customer customer = event.getRowValue();
            String newValue = event.getNewValue();
            if (newValue == null || newValue.trim().isEmpty() || !newValue.matches("\\d{4}")) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a valid 4-digit postcode.");
                editCustomerTable.refresh();
            } else {
                customer.setPostcode(newValue);
            }
        });

        TableColumn<Customer, String> paymentTypeColumn = new TableColumn<>("Payment Type");
        paymentTypeColumn.setCellValueFactory(cellData -> {
            Customer customer = cellData.getValue();
            if (customer instanceof PayingCustomer) {
                PayingCustomer payingCustomer = (PayingCustomer)customer; 
                PaymentMethod paymentMethod = payingCustomer.getPaymentMethod();
                if (paymentMethod != null) {
                    return new SimpleStringProperty(paymentMethod.getPaymentType().toString());
                }
            }
            return new SimpleStringProperty("");
        });

        // Add listener to the table columns for edit commit events
        paymentTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        paymentTypeColumn.setOnEditCommit(event -> {
            Customer customer = event.getRowValue();
            if (customer instanceof PayingCustomer) {
                PayingCustomer payingCustomer = (PayingCustomer)customer; 
                String newValue = event.getNewValue();
                try {
                    PaymentMethod.PaymentType paymentType = PaymentMethod.PaymentType.valueOf(newValue.toUpperCase());
                    payingCustomer.getPaymentMethod().setPaymentType(paymentType);
                } catch (IllegalArgumentException ex) {
                    showAlert(Alert.AlertType.ERROR, "Form Error!", "Invalid payment type. Use 'CREDIT_DEBIT_CARD' or 'BANK_ACCOUNT'.");
                    editCustomerTable.refresh();
                }
            } else if (customer instanceof AssociateCustomer) {
                event.consume();
            }
        });

        TableColumn<Customer, String> accountNumberColumn = new TableColumn<>("Account Number");
        accountNumberColumn.setCellValueFactory(cellData -> {
            Customer customer = cellData.getValue();
            if (customer instanceof PayingCustomer) {
                PayingCustomer payingCustomer = (PayingCustomer)customer; 
                PaymentMethod paymentMethod = payingCustomer.getPaymentMethod();
                if (paymentMethod != null) {
                    return new SimpleStringProperty(paymentMethod.getAccountNumber());
                }
            }
            return new SimpleStringProperty("");
        });

        // Add listener to the table columns for edit commit events
        accountNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        accountNumberColumn.setOnEditCommit(event -> {
            Customer customer = event.getRowValue();
            if (customer instanceof PayingCustomer) {
                PayingCustomer payingCustomer = (PayingCustomer)customer; 
                String newValue = event.getNewValue();
                if (newValue == null || newValue.trim().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Form Error!", "Account number cannot be empty.");
                    editCustomerTable.refresh();
                } else {
                    payingCustomer.getPaymentMethod().setAccountNumber(newValue);
                }
            } else if (customer instanceof AssociateCustomer) {
                event.consume();
            }
        });

        ObservableList<String> payingCustomers = FXCollections.observableArrayList();

        for (Customer customer : magazineService.getCustomers()) {
            if (customer instanceof PayingCustomer) {
                payingCustomers.add(customer.getName());
            }
        }

        TableColumn<Customer, String> payingCustomerColumn = new TableColumn<>("Paying Customer");
        payingCustomerColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof PayingCustomer) {
                return new SimpleStringProperty(cellData.getValue().getName());
            } else {
                if (((AssociateCustomer) cellData.getValue()).getPayingCustomer() == null) {
                    return new SimpleStringProperty("");
                }
                return new SimpleStringProperty(((AssociateCustomer) cellData.getValue()).getPayingCustomer().getName());
            }
        });

        payingCustomerColumn.setCellFactory(ComboBoxTableCell.forTableColumn(payingCustomers));

        payingCustomerColumn.setOnEditCommit(event -> {
            Customer customer = event.getRowValue();
            String newValue = event.getNewValue();
            if (customer instanceof AssociateCustomer && newValue != null) {
                PayingCustomer selectedPayingCustomer = (PayingCustomer) magazineService.getCustomers().stream()
                        .filter(c -> c instanceof PayingCustomer && c.getName().equals(newValue))
                        .findFirst().orElse(null);
                if (selectedPayingCustomer != null) {
                    AssociateCustomer associateCustomer = (AssociateCustomer) customer;
                    if (associateCustomer.getPayingCustomer() != null) {
                        associateCustomer.getPayingCustomer().getAssociateCustomers().remove(associateCustomer);
                    }
                    associateCustomer.setPayingCustomer(selectedPayingCustomer);
                    selectedPayingCustomer.getAssociateCustomers().add(associateCustomer);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Form Error!", "Selected paying customer does not exist.");
                    editCustomerTable.refresh();
                }
            } else if (customer instanceof PayingCustomer) {
                event.consume();
            }
        });

        TableColumn<Customer, String> customerTypeColumn = new TableColumn<>("Customer Type");
        customerTypeColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof PayingCustomer) {
                return new SimpleStringProperty("Paying Customer");
            } else {
                return new SimpleStringProperty("Associate Customer");
            }
        });

        editCustomerTable.getColumns().addAll(nameColumn, emailColumn, streetNumberColumn, streetNameColumn, suburbColumn, postcodeColumn, paymentTypeColumn, accountNumberColumn, payingCustomerColumn, customerTypeColumn);

        for (Supplement supplement : magazineService.getMagazine().getSupplements()) {
            TableColumn<Customer, Boolean> supplementColumn = new TableColumn<>(supplement.getName());
            supplementColumn.setCellValueFactory(cellData -> {
                Customer customer = cellData.getValue();
                BooleanProperty subscribedProperty = new SimpleBooleanProperty(customer.getSubscribedSupplements().contains(supplement));
                subscribedProperty.addListener((obs, oldValue, newValue) -> {
                    if (newValue) {
                        customer.getSubscribedSupplements().add(supplement);
                    } else {
                        customer.getSubscribedSupplements().remove(supplement);
                    }
                });
                return subscribedProperty;
            });
            supplementColumn.setCellFactory(CheckBoxTableCell.forTableColumn(supplementColumn));
            supplementColumn.setEditable(true);
            editCustomerTable.getColumns().add(supplementColumn);
        }

        editCustomerTable.setItems(customerData);
        editCustomerTable.setEditable(true);
    }

    
    private void initializeSupplementTable() {
        supplementData.addAll(magazineService.getMagazine().getSupplements());

        TableColumn<Supplement, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.prefWidthProperty().bind(editSupplementTable.widthProperty().multiply(0.5)); // Set to 50% of the table width

        nameColumn.setOnEditCommit(event -> {
            Supplement supplement = event.getRowValue();
            String oldName = supplement.getName();
            String newValue = event.getNewValue();
            if (newValue == null || newValue.trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Supplement name cannot be empty.");
                editSupplementTable.refresh();
            } else {
                supplement.setName(newValue);
                int index = supplementData.indexOf(supplement);
                if (index != -1) {
                    supplementData.set(index, supplement);
                    for (TableColumn<Customer, ?> column : editCustomerTable.getColumns()) {
                        if (column.getText().equals(oldName)) {
                            column.setText(newValue);
                            // Measure the width required to display the entire name
                            Text text = new Text(newValue);
                            text.setFont(Font.font("System", 12)); // Match the font used in the TableView header
                            int textWidth = (int)(text.getLayoutBounds().getWidth());
                            int padding = 40; // Add some padding for better visibility
                            column.setPrefWidth(textWidth + padding);
                            break;
                        }
                    }
                }
            }
        });

        TableColumn<Supplement, String> costColumn = new TableColumn<>("Weekly Cost");
        costColumn.setCellValueFactory(cellData -> {
            Double value = cellData.getValue().getWeeklyCost();
            return new SimpleStringProperty(value != null ? String.valueOf(value) : "");
        });
        costColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        costColumn.prefWidthProperty().bind(editSupplementTable.widthProperty().multiply(0.5)); // Set to 50% of the table width

        
        costColumn.setOnEditCommit(event -> {
            Supplement supplement = event.getRowValue();
            String newValue = event.getNewValue();
            try {
                double newCost = Double.parseDouble(newValue);
                if (newCost < 0) {
                    throw new NumberFormatException("Negative value");
                }
                supplement.setWeeklyCost(newCost);
                int index = supplementData.indexOf(supplement);
                if (index != -1) {
                    supplementData.set(index, supplement);
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a valid weekly cost greater than or equal to zero.");
                editSupplementTable.refresh();
            }
        });

        editSupplementTable.getColumns().addAll(nameColumn, costColumn);
        editSupplementTable.setItems(supplementData);
        editSupplementTable.setEditable(true);
    }

    public void addCustomer(ActionEvent e) {
        List<String> choices = Arrays.asList("Paying Customer", "Associate Customer");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Paying Customer", choices);
        dialog.setTitle("Add Customer");
        dialog.setHeaderText("Choose the type of customer to add:");
        dialog.setContentText("Customer Type:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(customerType -> {
            if (customerType.equals("Paying Customer")) {
                PayingCustomer payingCustomer = new PayingCustomer();
                // Add an empty PayingCustomer to the data list
                customerData.add(payingCustomer);
            } else if (customerType.equals("Associate Customer")) {
                AssociateCustomer associateCustomer = new AssociateCustomer();
                // Add an empty AssociateCustomer to the data list
                customerData.add(associateCustomer);
            }
        });
    }

    public void deleteCustomer(ActionEvent e) {
        Customer selectedCustomer = editCustomerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            magazineService.removeCustomer(selectedCustomer);
            customerData.clear(); 
            customerData.addAll(magazineService.getCustomers());
            editCustomerTable.refresh(); 
        }
    }

    public void addSupplement(ActionEvent e) {
        Supplement newSupplement = new Supplement();
        // Add an empty Supplement to the data list
        supplementData.add(newSupplement);
        magazineService.getMagazine().addSupplement(newSupplement);
        
        boolean exists = false;

        for (TableColumn<Customer, ?> column : editCustomerTable.getColumns()) {
            if (column.getText().equals(newSupplement.getName())) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            // Add a new column to the customer table
            TableColumn<Customer, Boolean> newSupplementColumn = new TableColumn<>(newSupplement.getName());
            newSupplementColumn.setCellValueFactory(cellData -> {
                Customer customer = cellData.getValue();
                BooleanProperty subscribedProperty = new SimpleBooleanProperty(customer.getSubscribedSupplements().contains(newSupplement));
                // Add listener to handle changes in the checkbox
                subscribedProperty.addListener((obs, oldValue, newValue) -> {
                    if (newValue) {
                        customer.getSubscribedSupplements().add(newSupplement);
                    } else {
                        customer.getSubscribedSupplements().remove(newSupplement);
                    }
                });
                return subscribedProperty;
            });
            newSupplementColumn.setCellFactory(CheckBoxTableCell.forTableColumn(newSupplementColumn));
            newSupplementColumn.setEditable(true);
            
            editCustomerTable.getColumns().add(newSupplementColumn);
        }   
    }

    public void deleteSupplement(ActionEvent e) {
        Supplement selectedSupplement = editSupplementTable.getSelectionModel().getSelectedItem();
        if (selectedSupplement != null) {
            magazineService.getMagazine().removeSupplement(selectedSupplement);
            supplementData.remove(selectedSupplement);
        }
        
        for (Customer customer : customerData) {
            if (customer.getSubscribedSupplements().contains(selectedSupplement)) {
                customer.getSubscribedSupplements().remove(selectedSupplement);
                customerData.set(customerData.indexOf(customer), customer);
            }
        }
        // Remove the corresponding column from the customer table
        for (TableColumn<Customer, ?> column : editCustomerTable.getColumns()) {
            if (column.getText().equals(selectedSupplement.getName())) {
                editCustomerTable.getColumns().remove(column);
                break;
            }
        }
    }
    
    public void generateBill(ActionEvent e) {
        // Iterate over all customers
        for (Customer customer : customerData) {
            // Check if the customer is a paying customer
            if (customer instanceof PayingCustomer) {
                PayingCustomer payingCustomer = (PayingCustomer) customer;
                // Generate a bill for the paying customer
                generateBillForPayingCustomer(payingCustomer);
            }
        }
    }

    private void generateBillForPayingCustomer(PayingCustomer payingCustomer) {
        // Retrieve the billing history of the paying customer
        BillingHistory billingHistory = payingCustomer.getBillingHistory();

        // Get the list of subscribed supplements for the paying customer
        List<Supplement> subscribedSupplements = payingCustomer.getSubscribedSupplements();

        // Get the list of associated customers for the paying customer
        List<AssociateCustomer> associateCustomers = payingCustomer.getAssociateCustomers();

        // Set the payment method for the bill (assuming it's already set)
        PaymentMethod paymentMethod = payingCustomer.getPaymentMethod();

        // Get the magazine cost (you need to define this value)
        double magazineCost = magazineService.getMagazine().getMainPartCost();
        System.out.println(magazineService.getMagazine().getMainPartCost());

        // Generate the bill for the paying customer
        billingHistory.generateBill(subscribedSupplements, associateCustomers, paymentMethod, magazineCost);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}