/* **********************
 * CSC-20004 COURSEWORK *
 * Component 1          * 
 * 2024/25 First sit    * 
 * **********************/
package uk.ac.keele.csc20004.specialcomponents;

import uk.ac.keele.csc20004.coursework.SimulationParameters;

/**
 * A concrete implementation of a sensor in the separate hierarchy.
 * This class implements the SensingComponent interface, which is similar
 * but not identical to Sensor in the main hierarchy.
 * 
 * This class represents a lidar sensor.
 * 
 * @author marcoortolani
 */
public class Lidar implements SensingComponent {
    private static final int LIDAR_PROTECTION = 80;
    private double energy;
    private final int protection;

    /**
     * Constructor for the Lidar class.
     * It just sets the protection value to a default value.
     */
    public Lidar() {
        this.protection = LIDAR_PROTECTION;
        
        if (protection < 0 || protection > SensingComponent.MAX_PROTECTION) {
            throw new IllegalArgumentException("Protection must be between 0 and " + SensingComponent.MAX_PROTECTION);
        }

        this.energy = getBaseEnergy();
    }

    @Override
    public double getBaseEnergy() {
        return SimulationParameters.BASE_ENERGY;
    }

    @Override
    public void setEnergy(double energy) {
        if (energy < 0 ) {
            energy = 0;
        }
        this.energy = energy;
    }
    
    @Override
    public double getEnergy() {
        return energy;
    }
    
    @Override
    public int getProtectionValue() {
        return protection;
    }

    @Override
    public int getMaxProtection() {
        return SensingComponent.MAX_PROTECTION;
    }

    @Override
    public String toString() {
        return "lidar (" + protection + ")";
    }

}
