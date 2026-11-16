/* **********************
 * CSC-20004 COURSEWORK *
 * Component 1          * 
 * 2024/25 First sit    * 
 * **********************/
package uk.ac.keele.csc20004.specialcomponents;

import uk.ac.keele.csc20004.coursework.SimulationParameters;

/**
 * A concrete implementation of an actuator in the separate hierarchy.
 * This class implements the ActuationComponent interface, which is similar
 * but not identical to Actuator in the main hierarchy.
 * 
 * This class represents a hook.
 * 
 * @author marcoortolani
 */
public class Hook implements ActuationComponent {
    private static final int HOOK_IMPACT_FORCE = 60;

    private double energy;
    private final int impactForce;

    /**
     * Constructor for the Hook class.
     * It just sets the impact force to a default value.
     */
    public Hook() {
        this.impactForce = HOOK_IMPACT_FORCE;

        if (impactForce < 0 || impactForce > ActuationComponent.MAX_IMPACT_FORCE) {
            throw new IllegalArgumentException("Impact force must be between 0 and " + ActuationComponent.MAX_IMPACT_FORCE);
        }

        this.energy = getBaseEnergy();
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
    public double getBaseEnergy() {
        return SimulationParameters.BASE_ENERGY;
    }

    @Override
    public int getImpactForce() {
        return impactForce;
    }

    @Override
    public int getMaxImpactForce() {
        return ActuationComponent.MAX_IMPACT_FORCE;
    }

    @Override
    public String toString() {
        return "hook (" + impactForce + ")";
    }
 }
