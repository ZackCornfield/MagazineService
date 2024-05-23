package magazine_service;
import java.io.Serializable;
import java.util.ArrayList; 
import java.util.List;

/**
 * The Customer class represents a customer of the magazine service.
 * It contains information about the customer's name, email, and subscribed supplements.
 * @author Zack Cornfield
 */
public class Customer implements Serializable {
    private String name;
    private String emailAddress;
    private int streetNumber;
    private String streetName;
    private String suburb;
    private String postcode;
    private List<Supplement> subscribedSupplements;

    /**
     * Default constructor for Customer class.
     * Initializes the attributes with default values.
     */
    public Customer() {
        this.name = "";
        this.emailAddress = "";
        this.streetNumber = Integer.MIN_VALUE ;
        this.streetName = "";
        this.suburb = "";
        this.postcode = "";
        this.subscribedSupplements = new ArrayList<>();    
    }

    /**
     * Parameterized constructor for Customer class.
     * 
     * @param name The name of the customer.
     * @param emailAddress The email address of the customer.
     * @param streetNumber The street number of the customer's address.
     * @param streetName The street name of the customer's address.
     * @param suburb The suburb of the customer's address.
     * @param postcode The postcode of the customer's address.
     * @param subscribedSupplements The list of supplements subscribed by the customer.
     */
    public Customer(String name, String emailAddress, int streetNumber, String streetName, String suburb, String postcode, List<Supplement> subscribedSupplements) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.suburb = suburb;
        this.postcode = postcode;
        this.subscribedSupplements = subscribedSupplements;
    }
    
    /**
     * Retrieves the name of the customer.
     * @return The name of the customer.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the email address of the customer.
     * @return The email address of the customer.
     */
    public String getEmailAddress() {
        return emailAddress;
    }
    
    /**
     * Retrieves the street number of the customer.
     * @return The streetNumber of the customer.
     */
    public int getStreetNumber() {
        return streetNumber;
    }

    /**
     * Retrieves the street name of the customer.
     * @return The streetName of the customer.
     */
    public String getStreetName() {
        return streetName;
    }

    /**
     * Retrieves the suburb of the customer.
     * @return The suburb of the customer.
     */
    public String getSuburb() {
        return suburb;
    }

    /**
     * Retrieves the postcode of the customer.
     * @return The postcode of the customer.
     */
    public String getPostcode() {
        return postcode;
    }
    
    /**
     * Retrieves the list of supplements subscribed by the customer.
     * @return The list of subscribed supplements.
     */
    public List<Supplement> getSubscribedSupplements() {
        return subscribedSupplements;
    }
    
    /**
     * Sets the name of the customer.
     * @param name The name of the customer to set.
     */
    public void setName(String name) {
        this.name = name; 
    }
    
    /**
     * Sets the email address of the customer.
     * @param emailAddress The email address of the customer to set.
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress; 
    }
    
    /**
     * Sets the street number of the customer.
     * @param streetNumber The street number of the customer to set.
     */
    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    /**
     * Sets the street name of the customer.
     * @param streetName The street name of the customer to set.
     */
    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    /**
     * Sets the suburb of the customer.
     * @param suburb The suburb of the customer to set.
     */
    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    /**
     * Sets the postcode of the customer.
     * @param postcode The postcode of the customer to set.
     */
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
   
    /**
     * Sets the subscribed supplements of the customer.
     * @param subscribedSupplements List of Supplement
     */
    public void setSubscribedSupplements(List<Supplement> subscribedSupplements) {
        this.subscribedSupplements = subscribedSupplements;
    }

    /**
     * Subscribes the customer to a supplement.
     * @param supplement The supplement to subscribe to.
     */
    public void subscribeToSupplement(Supplement supplement) {
        subscribedSupplements.add(supplement);
    }

    /**
     * Unsubscribes the customer from a supplement.
     * @param supplement The supplement to unsubscribe from.
     */
    public void unsubscribeFromSupplement(Supplement supplement) {
        subscribedSupplements.remove(supplement);
    }
}