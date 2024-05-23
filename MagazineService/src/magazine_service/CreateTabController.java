package magazine_service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.SelectionMode;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Controller class for the create tab in the magazine service application.
 * Handles creation of magazines, supplements, and customers.
 */
public class CreateTabController {
    @FXML
    private TextField magazineNameField;

    @FXML
    private TextField magazineCostField;
    
    @FXML
    private AnchorPane MagazineSection; 
    
    //---------------------------------------------

    @FXML
    private TextField supplementNameField;

    @FXML
    private TextField supplementCostField;

    @FXML
    private Button DoneButton;

    @FXML
    private TableView<Supplement> supplementTable;

    private ObservableList<Supplement> supplementData;
    
    @FXML
    private AnchorPane SupplementSection;
    
    //---------------------------------------------
    
    @FXML
    private AnchorPane CustomerSection;

    @FXML
    private TextField customerNameField;

    @FXML
    private TextField customerEmailField;

    @FXML
    private TextField customerStreetNumberField;

    @FXML
    private TextField customerStreetNameField;

    @FXML
    private TextField customerSuburbField;

    @FXML
    private TextField customerPostcodeField;

    @FXML
    private ListView<Supplement> supplementListView;

    @FXML
    private ChoiceBox<String> customerTypeChoiceBox;

    @FXML
    private ChoiceBox<String> paymentTypeChoiceBox;

    @FXML
    private TextField paymentNumberField;

    @FXML
    private ChoiceBox<String> payingCustomerChoiceBox;
    
    @FXML
    private Button saveMagazineButton; 
    
    @FXML
    private AnchorPane PayingSection;
    
    @FXML
    private AnchorPane AssociateSection;
    
    //---------------------------------------------

    private MagazineService magazineService;
    private Magazine magazine;
    
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
    public void setMagazineService(MagazineService magazineService) 
    {
        this.magazineService = magazineService; 
    }

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        magazine = new Magazine();
        supplementData = FXCollections.observableArrayList();
        initializeChoiceBoxes();
        initializeSupplementTable();
        initializeSupplementList(); 
        initializeCustomerTypeListener(); 
    }

    private void initializeSupplementTable() {
        TableColumn<Supplement, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn()); 
        nameColumn.setOnEditCommit(event -> {
            String newValue = event.getNewValue();
            if (newValue != null && !newValue.trim().isEmpty()) {
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setName(newValue);
            } else {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a valid name.");
                supplementTable.refresh();
            }
        });

        TableColumn<Supplement, String> costColumn = new TableColumn<>("Weekly Cost");
        costColumn.setCellValueFactory(cellData -> {
            Double value = cellData.getValue().getWeeklyCost();
            return new SimpleStringProperty(value != null ? String.valueOf(value) : "");
        });
        costColumn.setCellFactory(TextFieldTableCell.forTableColumn());
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
                supplementTable.refresh();
            }
        });
      
        supplementTable.getColumns().addAll(nameColumn, costColumn);
        supplementTable.setItems(supplementData);
        supplementTable.setEditable(true); // Enable editing for the entire table
    }

    private void updateListView() {
        // Update the ListView with the new data from the TableView
        supplementListView.setItems(FXCollections.observableArrayList(supplementTable.getItems()));
    }
    
    private void initializeSupplementList() {
        supplementListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        supplementListView.setItems(supplementData);
        supplementListView.setCellFactory(param -> new ListCell<Supplement>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    if (!isEmpty() && newVal != null) {
                        if (newVal) {
                            getListView().getSelectionModel().select(getIndex());
                        } else {
                            getListView().getSelectionModel().clearSelection(getIndex());
                        }
                    }
                });
                selectedProperty().addListener((obs, oldVal, newVal) -> {
                    checkBox.setSelected(newVal);
                });
            }

            @Override
            protected void updateItem(Supplement item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    checkBox.setText(item.getName());
                    checkBox.setSelected(isSelected());
                    setGraphic(checkBox);
                }
            }
        });
    }

    
    private void initializeChoiceBoxes() {
        customerTypeChoiceBox.getItems().addAll("Paying Customer", "Associate Customer");
        paymentTypeChoiceBox.getItems().addAll("Credit Card", "Bank Account");
    }
    
    private void initializeCustomerTypeListener() {
        customerTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                if (newVal.equals("Paying Customer")) {
                    PayingSection.setVisible(true);
                    PayingSection.setDisable(false);                    
                    AssociateSection.setVisible(false);
                    AssociateSection.setDisable(true);                                        
                } else if (newVal.equals("Associate Customer")) {
                    PayingSection.setVisible(false);
                    PayingSection.setDisable(true);                                     
                    AssociateSection.setVisible(true);
                    AssociateSection.setDisable(false);                    
                }
            }
        });
    }

    public void createMagazine(ActionEvent e) {
        String name = magazineNameField.getText();
        String costText = magazineCostField.getText();

        // Validate name
        if (name.isEmpty()) {
            showAlert(AlertType.ERROR, "Form Error!", "Please enter a magazine name.");
            return;
        }

        // Validate cost
        double cost;
        try {
            cost = Double.parseDouble(costText);
            if (cost <= 0) {
                showAlert(AlertType.ERROR, "Form Error!", "Please enter a valid magazine cost greater than zero.");
                return;
            }
        } catch (NumberFormatException ex) {
            showAlert(AlertType.ERROR, "Form Error!", "Please enter a valid numeric value for magazine cost.");
            return;
        }

        magazine.setName(name);
        magazine.setMainPartCost(cost);
        MagazineSection.setDisable(true); 
        SupplementSection.setDisable(false); 
        DoneButton.setDisable(true);
    }

    public void addSupplement(ActionEvent e) {
        String name = supplementNameField.getText();
        String costText = supplementCostField.getText();

        // Validate name
        if (name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a supplement name.");
            return;
        }

        // Validate cost
        double cost;
        try {
            cost = Double.parseDouble(costText);
            if (cost <= 0) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a valid supplement cost greater than zero.");
                return;
            }
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a valid numeric value for supplement cost.");
            return;
        }

        Supplement supplement = new Supplement(name, cost);
        magazine.addSupplement(supplement);

        supplementData.add(supplement);
        DoneButton.setDisable(false);
    }
    
    public void deleteSupplement(ActionEvent e) {
        // Get the selected supplement from the table
        Supplement selectedSupplement = supplementTable.getSelectionModel().getSelectedItem();

        if (selectedSupplement != null) {
            // Remove the selected supplement from the TableView
            supplementTable.getItems().remove(selectedSupplement);
            // Remove the selected supplement from the magazine
            magazine.removeSupplement(selectedSupplement);
        } else {
            System.out.println("No supplement selected.");
        }
    }
    
    public void finishedSupplements(ActionEvent e) 
    {
        SupplementSection.setDisable(true); 
        CustomerSection.setDisable(false); 
        saveMagazineButton.setDisable(true);
        magazineService = new MagazineService(magazine, new ArrayList<>()); 
    }
          
    public void createCustomer(ActionEvent e) {
        String name = customerNameField.getText();
        String email = customerEmailField.getText();
        String streetNumberText = customerStreetNumberField.getText();
        String streetName = customerStreetNameField.getText();
        String suburb = customerSuburbField.getText();
        String postcode = customerPostcodeField.getText();
        List<Supplement> selectedSupplements = new ArrayList<>();
        String customerType = customerTypeChoiceBox.getSelectionModel().getSelectedItem();

        // Validate name
        if (name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a customer name.");
            return;
        }

        // Validate email
        if (email.isEmpty() || !email.contains("@")) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a valid email address.");
            return;
        }

        // Validate street number
        int streetNumber;
        try {
            streetNumber = Integer.parseInt(streetNumberText);
            if (streetNumber <= 0) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a valid street number greater than zero.");
                return;
            }
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a valid numeric value for street number.");
            return;
        }

        // Validate street name
        if (streetName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a street name.");
            return;
        }

        // Validate suburb
        if (suburb.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a suburb.");
            return;
        }

        // Validate postcode
        if (postcode.isEmpty() || postcode.length() != 4 || !postcode.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a valid 4-digit postcode.");
            return;
        }

        // Check if customer type is selected
        if (customerType == null) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please select a customer type.");
            return;
        }

        // Collect selected supplements
        for (int i = 0; i < supplementListView.getItems().size(); i++) {
            if (supplementListView.getSelectionModel().isSelected(i)) {
                selectedSupplements.add(supplementListView.getItems().get(i));
            }
        }

        if (customerType.equals("Paying Customer")) {
            String paymentType = paymentTypeChoiceBox.getValue();
            if (paymentType == null) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Please select a payment type.");
                return;
            }
            
            String paymentNumber = paymentNumberField.getText(); 
            if(paymentNumber.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter an account/card number"); 
                return; 
            }

            PaymentMethod paymentMethod = null;
            if (paymentType.equals("Credit Card")) {
                paymentMethod = new PaymentMethod(PaymentMethod.PaymentType.CREDIT_DEBIT_CARD, paymentNumber);
            } else if (paymentType.equals("Bank Account")) {
                paymentMethod = new PaymentMethod(PaymentMethod.PaymentType.BANK_ACCOUNT, paymentNumber);
            }

            PayingCustomer payingCustomer = new PayingCustomer(new ArrayList<>(), name, email, streetNumber, streetName, suburb, postcode, paymentMethod, selectedSupplements);
            payingCustomerChoiceBox.getItems().add(payingCustomer.getName());
            magazineService.addCustomer(payingCustomer);
        } else if (customerType.equals("Associate Customer")) {
            String payingCustomerName = payingCustomerChoiceBox.getValue();
            if (payingCustomerName == null) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Please select a paying customer.");
                return;
            }

            PayingCustomer payingCustomer = magazineService.findPayingCustomerByName(payingCustomerName);
            AssociateCustomer associateCustomer = new AssociateCustomer(payingCustomer, name, email, streetNumber, streetName, suburb, postcode, selectedSupplements);
            magazineService.addCustomer(associateCustomer);
        }

        // Clear fields after creating customer
        clearCustomerFields();

        saveMagazineButton.setDisable(false);
    }


    private void clearCustomerFields() {
        customerNameField.clear();
        customerEmailField.clear();
        customerStreetNumberField.clear();
        customerStreetNameField.clear();
        customerSuburbField.clear();
        customerPostcodeField.clear();
        supplementListView.getSelectionModel().clearSelection();
        customerTypeChoiceBox.getSelectionModel().clearSelection();
        paymentTypeChoiceBox.getSelectionModel().clearSelection();
        paymentNumberField.clear();
        payingCustomerChoiceBox.getSelectionModel().clearSelection();
    }
    
    public void saveMagazine(ActionEvent e) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save MagazineService");
            fileChooser.setInitialFileName("magazineService.ser");

            // Set the initial directory to the project folder
            String projectFolder = System.getProperty("user.dir");
            fileChooser.setInitialDirectory(new File(projectFolder));

            File file = fileChooser.showSaveDialog(null);

            // Serialize the magazineService object to the selected file
            if (file != null) {
                FileOutputStream fileOut = new FileOutputStream(file);
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
                objectOut.writeObject(magazineService);
                objectOut.close();
                fileOut.close();
                System.out.println("MagazineService saved successfully.");

                // Clear all fields and reinitialize components
                resetAllFields();
                initialize();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void resetAllFields() {
        // Clear all text fields
        magazineNameField.clear();
        magazineCostField.clear();
        supplementNameField.clear();
        supplementCostField.clear();
        customerNameField.clear();
        customerEmailField.clear();
        customerStreetNumberField.clear();
        customerStreetNameField.clear();
        customerSuburbField.clear();
        customerPostcodeField.clear();
        paymentNumberField.clear();

        // Clear and reset choice boxes and list views
        customerTypeChoiceBox.getSelectionModel().clearSelection();
        paymentTypeChoiceBox.getSelectionModel().clearSelection();
        payingCustomerChoiceBox.getSelectionModel().clearSelection();
        supplementListView.getSelectionModel().clearSelection();
        supplementData.clear();

        // Reset visibility and disable state of sections
        MagazineSection.setDisable(false);
        SupplementSection.setDisable(true);
        CustomerSection.setDisable(true);
        PayingSection.setDisable(true);
        AssociateSection.setDisable(true);
        DoneButton.setDisable(true);
        saveMagazineButton.setDisable(true);

        // Reinitialize magazine and magazineService
        magazine = new Magazine();
        magazineService = new MagazineService(magazine, new ArrayList<>());
    }
   
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}   

