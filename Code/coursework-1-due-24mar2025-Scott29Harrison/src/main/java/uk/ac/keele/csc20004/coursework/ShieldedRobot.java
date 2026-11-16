package uk.ac.keele.csc20004.coursework;

import uk.ac.keele.csc20004.robots.*;
import uk.ac.keele.csc20004.robots.parts.Actuator;
import uk.ac.keele.csc20004.robots.parts.Frame;
import uk.ac.keele.csc20004.robots.parts.Motor;
import uk.ac.keele.csc20004.robots.parts.Sensor;

/**
 * A decorator class to allow shields to be added to robots.
 * The shield increases the defence coefficient by 20%, 
 * to a maximum of 100%.
 * 
 * A robot can only be shielded once.
 * 
 * @author 23018691
 */
public class ShieldedRobot extends AbstractRobot {
    
    private double newDefenceCoeff = 0.0;
    private String s;
    
    /**
     * The constructor for a shielded robot.
     * 
     * @param name the name of the robot being given a shield
     * @param frame the frame of the robot
     * @param motorParts an array of the motors of the robot
     * @param sensorParts an array of the sensors of the robot
     * @param actuatorParts an array of the actuators of the robot
     * @param robotType the short description of the robot (containing the name and overall energy of the robot)
     */
    public ShieldedRobot(String name, Frame frame, Motor[] motorParts, Sensor[] sensorParts, Actuator[] actuatorParts, String robotType) {
        super(name, frame, motorParts, sensorParts, actuatorParts);
        
        // Check if the robot is already shielded
        if (robotType.contains("Shielded") == true) {
            throw new IllegalArgumentException("This robot is already shielded!");
        }
        // Increase robot defence by 20%
        newDefenceCoeff = defenceCoeff * 1.2;
        if (newDefenceCoeff >= 1.0) {
            newDefenceCoeff = 1.0;
        }
        s = robotType;
    }

    @Override
    public double fendOff(double strikeCoefficient) {
        try {
            Thread.sleep(SimulationParameters.DEFENCE_DELAY);
        } catch (InterruptedException e) {
            System.err.format("IOException: %s%n", e);
        }
        
        double energyUpdateCoeff = strikeCoefficient - newDefenceCoeff;
        if (energyUpdateCoeff < 0.0) {
            energyUpdateCoeff = 0.0;
        }

        double finalEnergy = 0.0;

        double damage = energyUpdateCoeff * SimulationParameters.STRIKE_INTENSITY;
        System.out.println(name + " is hit with strike coeff " + strikeCoefficient + " - damage: " + damage);

        frame.updateEnergy(-damage);
        finalEnergy += frame.getCurrentEnergy();

        for (Motor m : motorParts) {
            m.updateEnergy(-damage);
            finalEnergy += m.getCurrentEnergy();
        }

        for (Sensor s : sensorParts) {
            s.updateEnergy(-damage);
            finalEnergy += s.getCurrentEnergy();
        }

        for (Actuator a : actuatorParts) {
            a.updateEnergy(-damage);
            finalEnergy += a.getCurrentEnergy();
        }

        return finalEnergy;
    }
    
    @Override
    public String toString() {
        return s + String.format("%10s", "Shielded");
    }
    
}
