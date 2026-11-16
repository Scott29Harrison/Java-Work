package uk.ac.keele.csc20004.coursework;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import uk.ac.keele.csc20004.RepairLine;
import uk.ac.keele.csc20004.robots.Robot;

/**
 * A concrete of implementation of a RepairLine.
 * This is a priority queue of robot energies, managed by a hashmap of robot keys to energy values.
 * The lowest energy robot is the first to be repaired.
 * 
 * @author 23018691
 */
public class MyRobotRepairLine implements RepairLine {
    private HashMap<Robot, Double> robotEnergyMap;
    private PriorityQueue<Double> queue = new PriorityQueue<>();

    public MyRobotRepairLine() {
        // Initialize HashMap
        robotEnergyMap = new HashMap<>();
 
        // Initialize PriorityQueue with natural ordering (ascending order)
        queue = new PriorityQueue<>();
    }
    
    /**
     * Adds a robot to the repair line.
     * 
     * @param robot the robot to add to the repair line
     */
    public void addRobot(Robot robot) {
        if (!robotEnergyMap.containsKey(robot)) {
            Double energy = robot.getActuatorsEnergy() + robot.getFrameEnergy() 
                      + robot.getMotorEnergy() + robot.getSensorsEnergy();
            robotEnergyMap.put(robot, energy);
            queue.offer(energy);
        } else {
            System.out.println("Robot already exists in the repair line!");
        }
    }

    /**
     * Retrieves the next robot in the repair line, and removes it from the line.
     * The 'next' robot is the one with the lowest energy.
     * 
     * @return the next robot in the repair line
     */
    public Robot getNextRobot() {
        if (queue.isEmpty()) {
            System.out.println("No robots to repair!");
        }
        // Retrieve the lowest energy from the queue
        Double lowestEnergy = queue.poll();
 
        // Find the corresponding robot in the HashMap
        Robot robotToRepair = null;
        for (Map.Entry<Robot, Double> entry : robotEnergyMap.entrySet()) {
            if (entry.getValue() == lowestEnergy) {
                robotToRepair = entry.getKey();
                break;
            }
        }
        
        if (robotToRepair != null) {
            System.out.println("Repairing robot: " + robotToRepair);
             
            // Update both HashMap and PriorityQueue
            robotEnergyMap.remove(robotToRepair);
            
        } else {
            System.out.println("Null robot");
        }
        return robotToRepair;
    }

    /**
     * Prints out the robots in the repair line.
     */
    public void printOut() {
        
        System.out.println("Current Repair Line (Energy Levels):");
        PriorityQueue<Double> tempQueue = new PriorityQueue<>(queue); // Copy to avoid modification
        HashMap<Robot, Double> tempEnergyMap = new HashMap<>(robotEnergyMap); // Copy to avoid modification
        while (!tempQueue.isEmpty()) {
            double energy = tempQueue.poll();
            for (Map.Entry<Robot, Double> entry : tempEnergyMap.entrySet()) {
                if (entry.getValue() == energy) {
                    System.out.println(entry.getKey() + " | Energy: " + energy);
                    tempEnergyMap.remove(entry.getKey());
                    break;
                }
            }
        }
    }

    /**
     * Returns the number of robots in the repair line.
     * 
     * @return the number of robots in the repair line
     */
    public int size() {
        return queue.size();
    }
}
