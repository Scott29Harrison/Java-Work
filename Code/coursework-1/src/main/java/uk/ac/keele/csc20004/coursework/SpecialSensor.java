package uk.ac.keele.csc20004.coursework;

import uk.ac.keele.csc20004.robots.parts.Sensor;
import uk.ac.keele.csc20004.specialcomponents.*;

/**
 * An adaptor class that allows sensing components to be used as sensors for robots.
 * 
 * @author 23018691
 */
public class SpecialSensor implements Sensor {
    private final SensingComponent specialSensor;

    /**
     * the constructor to adapt a sensing component into a sensor.
     * 
     * @param specialSensor the sensing component to adapt
     */
    public SpecialSensor(SensingComponent specialSensor) {
        this.specialSensor = specialSensor;
    }

    @Override
    public double getDefenceCoefficient() {
        return specialSensor.getProtectionValue()/specialSensor.getMaxProtection();
    }

    @Override
    public double getCurrentEnergy() {
        return specialSensor.getEnergy();
    }

    /**
     * Returns the initial energy level of this part, calculated as the base energy coefficient
     * (using 0.25 as energy coefficient for a sensor)
     * multiplied by the standard base energy level.
     * 
     * @return the initial energy level of this part
     */
    @Override
    public double getInitialEnergy() {
        return SimulationParameters.BASE_ENERGY * 0.25;
    }

    @Override
    public void updateEnergy(double delta) {
        
        if (specialSensor.getEnergy() + delta < 0) {
            specialSensor.setEnergy(0);
        }
        if (specialSensor.getEnergy() + delta > getInitialEnergy()) {
            specialSensor.setEnergy(getInitialEnergy());
        }
    }

    @Override
    public void refillEnergy() {
        specialSensor.setEnergy(getInitialEnergy());
    }

    @Override
    public String toString() {
        return (specialSensor.toString().split(" \\(")[0] + "-i");
    }
}