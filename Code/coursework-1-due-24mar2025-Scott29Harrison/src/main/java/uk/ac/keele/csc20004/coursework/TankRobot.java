package uk.ac.keele.csc20004.coursework;

import uk.ac.keele.csc20004.robots.AbstractRobot;
import uk.ac.keele.csc20004.robots.parts.Actuator;
import uk.ac.keele.csc20004.robots.parts.Blaster;
import uk.ac.keele.csc20004.robots.parts.Chassis;
import uk.ac.keele.csc20004.robots.parts.Motor;
import uk.ac.keele.csc20004.robots.parts.Sensor;
import uk.ac.keele.csc20004.robots.parts.Wheel;
import uk.ac.keele.csc20004.specialcomponents.Lidar;
import uk.ac.keele.csc20004.specialcomponents.Infrared;

/**
 * A concrete implementation of a Robot.
 * This class makes use of the hierarchy of types for the robot parts
 * to create a particular type of robot.
 * 
 * @author 23018691
 */

public class TankRobot extends AbstractRobot {
    /**
     * A TankRobot is defined by its parts: 4 wheels as Motor, a Chassis as Frame
     * and a Blaster as Actuator with Lidar and Infrared Sensors.
     * 
     * @param name a string to identify this robot
     * 
     */
    public TankRobot(String name) {
        super(name,
              new Chassis(),
              new Motor[]{new Wheel(), new Wheel(), new Wheel(), new Wheel()},
              new Sensor[]{new SpecialSensor(new Lidar()), new SpecialSensor(new Infrared())},
              new Actuator[]{new Blaster()});
    }

    @Override
    public String toString() {
        return String.format("%10s", "Tank ") + super.toString();
    }
}
