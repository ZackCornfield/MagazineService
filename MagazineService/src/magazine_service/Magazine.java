package magazine_service;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * The Magazine class represents a weekly personalized magazine.
 * It contains details about the main part cost and supplements offered.
 * @author Zack Cornfield
 */
public class Magazine implements Serializable {
    private String name;
    private double mainPartCost;
    private List<Supplement> supplements;  
    
    /**
     * Default constructor for Magazine class.
     * Initializes the attributes with default values.
     */
    public Magazine() {
        this.name = "";
        this.mainPartCost = 0.0f;
        this.supplements = new ArrayList<>(); 
    }

    /**
     * Parameterized constructor for Magazine class.
     * 
     * @param name The name of the magazine.
     * @param mainPartCost The cost of the main part of the magazine.
     * @param supplements The list of supplements associated with the magazine.
     */
    public Magazine(String name, double mainPartCost, List<Supplement> supplements) {
        this.name = name; 
        this.mainPartCost = mainPartCost;
        this.supplements = supplements;
    }
   
    /**
     * Retrieves the name of the magazine.
     * @return The name of the magazine.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the cost of the main part of the magazine.
     * @return The cost of the main part of the magazine.
     */
    public double getMainPartCost() {
        return mainPartCost;
    }
    
    /**
     * Retrieves the list of supplements offered by the magazine.
     * @return The list of supplements offered by the magazine.
     */
    public List<Supplement> getSupplements() {
        return supplements;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the cost of the main part of the magazine.
     * @param mainPartCost The cost of the main part of the magazine to set.
     */
    public void setMainPartCost(double mainPartCost) {
        this.mainPartCost = mainPartCost;
    }
    
    public void setSupplements(List<Supplement> supplements) {
        this.supplements = supplements;
    }

    /**
     * Adds a supplement to the list of supplements offered by the magazine.
     * @param supplement The supplement to add.
     */
    public void addSupplement(Supplement supplement) {
        supplements.add(supplement);
    }

    /**
     * Removes a supplement from the list of supplements offered by the magazine.
     * @param supplement The supplement to remove.
     */
    public void removeSupplement(Supplement supplement) {
        supplements.remove(supplement);
    }
}
