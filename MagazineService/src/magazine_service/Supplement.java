package magazine_service;

import java.io.Serializable;

/**
 * The Supplement class represents a supplement offered in the magazine service.
 * It contains information about the supplement's name and weekly cost.
 * @author Zack Cornfield
 */
public class Supplement implements Serializable {
    private String name;
    private double weeklyCost; 
    
    /**
     * Default constructor for Supplement class.
     * Initializes the name and weeklyCost with default values.
     */
    public Supplement() {
        this.name = "";
        this.weeklyCost = Double.MIN_VALUE; 
    }
    
    /**
     * Constructs a Supplement object with the specified name and weekly cost.
     * @param name The name of the supplement.
     * @param weeklyCost The weekly cost of the supplement.
     */
    public Supplement(String name, double weeklyCost) {
        this.name = name;
        this.weeklyCost = weeklyCost; 
    }
    
    /**
     * Retrieves the name of the supplement.
     * @return The name of the supplement.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the weekly cost of the supplement.
     * @return The weekly cost of the supplement.
     */
    public double getWeeklyCost() {
        return weeklyCost;
    }
    
    /**
     * Sets the name of the supplement.
     * @param name The name of the supplement to set.
     */
    public void setName(String name) {
        this.name = name; 
    }

    /**
     * Sets the weekly cost of the supplement.
     * @param weeklyCost The weekly cost of the supplement to set.
     */
    public void setWeeklyCost(double weeklyCost) {
        this.weeklyCost = weeklyCost;
    }
}
