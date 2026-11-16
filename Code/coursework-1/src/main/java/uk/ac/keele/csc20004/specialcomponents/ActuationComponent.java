/* **********************
 * CSC-20004 COURSEWORK *
 * Component 1          * 
 * 2024/25 First sit    *
 * **********************/
package uk.ac.keele.csc20004.specialcomponents;

/**
 * This is the interface for the additional type of actuators, 
 * not part as the main hierarchy of robot parts.
 * 
 * @author marcoortolani
 */
public interface ActuationComponent {
    public static final int MAX_IMPACT_FORCE = 100;

    /**
     * This method returns the energy contrinution of the actuator.
     * 
     * @return the energy of the actuator
     */
    public double getEnergy();

    /**
     * This method sets the energy of the actuator.
     * Note that the energy is an absolute vaue, not a delta.
     * The new energy value will overwrite the previous one.
     * The energy value cannot be negative.
     * 
     * @param energy the new energy value
     */
    public void setEnergy(double energy);

    /**
     * This method returns the base energy of the actuator.
     * 
     * @return the base energy of the actuator
     */
    public double getBaseEnergy();

    /**
     * This method returns the impact force of the actuator,
     * i.e., the damage that the actuator can cause.
     * The impact force is a value between 0 and a maximum value, 
     * which is defined by the MAX_IMPACT_FORCE constant.
     * This is an absolute value, not a percentage.
     * 
     * @return the impact force of the actuator
     */
    public int getImpactForce();

    /**
     * This method returns the maximum impact force of actuators
     * in this hierarchy.
     * 
     * @return the maximum impact force of actuators
     */
    public int getMaxImpactForce();
}
