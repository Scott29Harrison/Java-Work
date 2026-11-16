package uk.ac.keele.csc20004.coursework;

import java.util.ArrayList;

import uk.ac.keele.csc20004.robots.parts.*;
import uk.ac.keele.csc20004.specialcomponents.Antenna;
import uk.ac.keele.csc20004.specialcomponents.Compass;
import uk.ac.keele.csc20004.specialcomponents.Infrared;
import uk.ac.keele.csc20004.specialcomponents.Lidar;

/**
 * A helper class that allows the parts of a robot to be retrieved from a string.
 * 
 * @author 23018691
 */

public class RobotPartGetter {
    /**
     * This method retrieves the frame of a robot from its string of parts
     * 
     * @param input the string containing all robot parts
     * @return the frame of the robot
     */
    public Frame getFrame(String input) {
        // Remove square brackets
        input = input.substring(1, input.length() - 1);
        // Extract segments
        String frameName = input.split(" on")[1].split("with:")[0].trim();
        String frameType = frameName.split("-")[1].trim();
        switch (frameType) {
            case "torso":
                return new Torso();
            case "chassis":
            default:
                return new Chassis();
        }
         
    }

    /**
     * This method retrieves the motors of a robot from its string of parts
     * 
     * @param input the string containing all robot parts
     * @return an array of the motors of the robot
     */
    public Motor[] getMotors(String input) {
        // Remove square brackets
        input = input.substring(1, input.length() - 1);
        // Extract segments
        String motorNames = input.split("on")[0].trim();
        String[] motorStrings = motorNames.split(" ");
        ArrayList<Motor> motors = new ArrayList<Motor>();
        for (String m : motorStrings) {
            String motorType = m.split("-")[0].trim();
            switch (motorType) {
                case "leg":
                    motors.add(new Leg());
                    break;
                case "wheel":
                    motors.add(new Wheel());
                    break;
                case "rotor":
                default:
                    motors.add(new Rotor());
            }
        }    
        return motors.toArray(new Motor[0]);
    }

    /**
     * This method retrieves the actuators of a robot from its string of parts
     * 
     * @param input the string containing all robot parts
     * @return an array of the actuators of the robot
     */
    public Actuator[] getActuators(String input) {
        // Remove square brackets
        input = input.substring(1, input.length() - 1);
        // Extract segments
        String actuatorNames = input.split("and")[1].trim();
        String[] actuatorStrings = actuatorNames.split(" ");
        ArrayList<Actuator> actuators = new ArrayList<Actuator>();
        for (String a : actuatorStrings) {
            String actuatorType = a.split("-")[0].trim();
            switch (actuatorType) {
                case "arm":
                    actuators.add(new Arm());
                    break;
                case "blaster":
                default:
                    actuators.add(new Blaster());
            }
        }    
        return actuators.toArray(new Actuator[0]);
    }

    /**
     * This method retrieves the sensors of a robot from its string of parts
     * 
     * @param input the string containing all robot parts
     * @return an array of the sensors of the robot
     */
    public Sensor[] getSensors(String input) {
        // Remove square brackets
        input = input.substring(1, input.length() - 1);
        // Extract segments
        String sensorNames = input.split("with:")[1].split("and")[0].trim();
        String[] sensorStrings = sensorNames.split(" ");
        ArrayList<Sensor> sensors = new ArrayList<Sensor>();
        for (String s : sensorStrings) {
            String sensorType = s.split("-")[0].trim();
            switch (sensorType) {
                case "lidar":
                    sensors.add(new SpecialSensor(new Lidar()));
                    break;
                case "infrared":
                    sensors.add(new SpecialSensor(new Infrared()));
                    break;
                case "antenna":
                    sensors.add(new SpecialSensor(new Antenna()));
                    break;
                case "compass":
                    sensors.add(new SpecialSensor(new Compass()));
                    break;
                case "camera":
                default:
                    sensors.add(new Camera());
            }
        }    
        return sensors.toArray(new Sensor[0]);
    }
}
