/* **********************
 * CSC-20004 COURSEWORK *
 * Component 1          * 
 * 2024/25 First sit    *
 * **********************/
package uk.ac.keele.csc20004.specialcomponents;

/**
 * This is the interface for the additional type of sensors,
 * not part as the main hierarchy of robot parts.
 * 
 * @author marcoortolani
 */
public interface SensingComponent {
    public static final int MAX_PROTECTION = 500;

    /**
     * This method returns the energy contrinution of the sensor.
     * 
     * @return the energy of the sensor
     */
    public double getEnergy();

    /**
     * This method sets the energy of the sensor.
     * Note that the energy is an absolute vaue, not a delta.
     * The new energy value will overwrite the previous one.
     * The energy value cannot be negative.
     * 
     * @param energy the new energy value
     */
    public void setEnergy(double energy);

    /**
     * This method returns the base energy of the sensor.
     * 
     * @return the base energy of the sensor
     */
    public double getBaseEnergy();

    /**
     * This method returns the protection value of the sensor,
     * i.e., the protection that the sensor can provide.
     * The protection value is a value between 0 and a maximum value, 
     * which is defined by the MAX_PROTECTION constant.
     * This is an absolute value, not a percentage.
     * 
     * @return the protection value of the sensor
     */
    public int getProtectionValue();

    /**
     * This method returns the maximum protection value of sensors
     * in this hierarchy.
     * 
     * @return the maximum protection value of sensors
     */
    public int getMaxProtection();
}
