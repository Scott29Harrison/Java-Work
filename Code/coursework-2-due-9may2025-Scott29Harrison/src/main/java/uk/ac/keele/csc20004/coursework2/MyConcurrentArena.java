/* **********************
 * CSC-20004 COURSEWORK *
 * Component 2         * 
 * 2024/25 First sit    *
 * **********************/
 
 
 
 /* For this coursework, I have encountered several
 * instances in which deadlocks have occured.
 * One of the main causes of these locks is the requirement
 * for both mechanics and battle managers to have concurrent
 * access to the repairline and the bench. These deadlocks are
 * solved fairly easily by allowing the methods in repair line
 * and bench to be accessed using separate locks.
 *  
 * The locks used vary between reentrant locks and synchronized locks
 * depending on the needs of the lock. If an object needs to be locked
 * for smooth running, a synchronized lock is used to prevent the object
 * from being altered. A synchronized lock is also used on methods that
 * can fail if another method in the same class is running and on those 
 * other methods to prevent them from running at the same time. 
 * 
 * Reentrant locks are used when the process fails if it is being run
 * at the same time as another instance of itself or when the lock needs
 * to be removed in different places under certain conditions.
 * 
 * Furthermore, there is a case in which both the bench and repair line 
 * are full since a battle manager can place more robots on the repairline 
 * than they take off in a single round. This is solved by allowing battle
 * managers to take a robot off the bench when they are waiting on the repair 
 * ine while both the repair line and bench are full.
 * 
 * Another cause is the mechanics needing access to specific repair bays.
 * This means that the repair bays must be checked and locked 
 * in sequence to ensure they are not waiting for each other.
 * 
 * I ensured no livelocks or deadlocks occured by ensuring that
 * the bench and repair line are not locked while mechanics 
 * and battle managers are waiting for space on them to be freed.
 * 
 * Starvation is possible in this assignment if the locks are 
 * implimented within or directly before a loop. This is because 
 * the lock may never be released causing other threads to be 
 * starved of the resources used within the loop. This can be avoided
 * by ensuring any loop involving a resource either has a 
 * guaranteed way in which the resource will be unlocked or 
 * by ensuring the resource is not locked for the loop. */

 
package uk.ac.keele.csc20004.coursework2;

import uk.ac.keele.csc20004.Bench;
import uk.ac.keele.csc20004.RepairLine;
import uk.ac.keele.csc20004.RobotArena;
import uk.ac.keele.csc20004.robots.CycloBot;
import uk.ac.keele.csc20004.robots.HumanoidRobot;
import uk.ac.keele.csc20004.robots.Robot;
import uk.ac.keele.csc20004.robots.WheeledRobot;

/**
 * This is the main class for the robot arena simulation.
 * 
 * @author 23018691
 */
public class MyConcurrentArena extends RobotArena {
    private static enum RobotType {
        HUMANOID,
        WHEELED,
        CYCLOBOT;
    }

    /**
     * This is the constructor for this class.
     * It creates a number of robots for the simulation equal to the capacity given.
     * 
     * @param studentId my student ID, 23018691
     * @param capacity the number of robots that can be in the arena at once
     * @param repairLine the repair line used for the arena
     * @param bench the bench used for the arena
     */
    public MyConcurrentArena(int studentId, int capacity, RepairLine repairLine, Bench bench) {
        super(studentId, capacity, repairLine, bench);
        
        // the constructor creates a list of robots for the simulation
        RobotType[] values = RobotType.values();
        for (int i = 0; i < capacity; i++) {
            RobotType t = values[i % values.length];
            Robot r = createRobot(i, t);
            addRobot(r);
        }
    }

    /**
     * This method simulates the repair of a robot after it is
     * extracted from the repair line.
     * Only the components with 0 energy are repaired by using the
     * appropriate bay.
     * 
     * @param r the robot to repair
     */
    @Override
    public void repair(Robot r) {
        if (r.getFrameEnergy() == 0.0) {
            r.refillFrameEnergy(frameBay);
        }
        if (r.getMotorEnergy() == 0.0) {
            r.refillMotorEnergy(motorBay);
        }
        if (r.getSensorsEnergy() == 0.0) {
            r.refillSensorsEnergy(sensorBay);
        }
        if (r.getActuatorsEnergy() == 0.0) {
            r.refillActuatorsEnergy(actuatorBay);
        }
    }

    /**
     * Helper method to create a robot with the given ID and type.
     * The type is used to create either a humanoid robot, a wheeled robot or a cyclobot.
     * 
     * @param robotId the ID of the robot
     * @param type the type of robot to create
     * @return the robot created
     */
    private Robot createRobot(int robotId, RobotType type) {
        switch (type) {
            case HUMANOID:
                return new HumanoidRobot("R-" +robotId);
            case WHEELED:
                return new WheeledRobot("R-" + robotId);
            case CYCLOBOT:
            default:
                return new CycloBot("R-" + robotId);
        }
    }

    /**
     * Returns the winner of the arena.
     * 
     * @return the robot with the highest energy level from the arena
     */
    @Override
    public Robot getWinner() {
        Robot winner = null;

        // the winner is determined as the robot with the most energy after the battle managers have finished their rounds
        double maxEnergy = 0.0;
        for (Robot r : robots) {
            double energy = r.getFrameEnergy() + r.getMotorEnergy() + r.getSensorsEnergy() + r.getActuatorsEnergy();
            if (energy > maxEnergy) {
                maxEnergy = energy;
                winner = r;
            }
        }

        return winner;
    }

    public static void main(String[] args) {
        
        // create the bench, repairline and arena for the simulation
        MyBench bench = new MyBench();
        MyRepairLine repairLine = new MyRepairLine();
        MyConcurrentArena arena = new MyConcurrentArena(23018691, SimulationParameters.NUM_ROBOTS, repairLine, bench);

        // create and start the mechanics to run concurrently
        Mechanic mechanic[] = new Mechanic[SimulationParameters.NUM_MECHANICS];
        for (int i = 0; i < SimulationParameters.NUM_MECHANICS; i++) {
            mechanic[i] = new Mechanic(repairLine, bench, arena, i);
            mechanic[i].start();
        }

        // prepare array to store battle managers
        BattleManager[] battleManager = new BattleManager[SimulationParameters.NUM_BATTLEMANAGERS];

        // start simulation
        System.out.println("Simulation begins...");
        System.out.println("Base strike intensity: " + SimulationParameters.STRIKE_INTENSITY);
        arena.printStats();

        // while the simulation can run indefinitely, a number of rounds is set for testing purposes
        int rounds = 30;

        // this do... while loop can run indefinitely but is bounded by a number of rounds when testing
        do { 
            // create and start the battle managers to run concurrently
            for (int i = 0; i < SimulationParameters.NUM_BATTLEMANAGERS; i++) {
                battleManager[i] = new BattleManager(repairLine, bench, arena, i);
                battleManager[i].start();
            }
            
            // wait for every battle manager to finish its rounds before continuing
            for(int i = 0; i < SimulationParameters.NUM_BATTLEMANAGERS; i++) {
                try {
                    battleManager[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // print the arena's stats after all managers have finished their rounds
            synchronized(arena){
                arena.printStats();
            }
 
            // get the winner from the arena
            Robot winner = arena.getWinner(); 
            if (winner != null) {
                System.out.println("The winner is: " + winner);
            } else {
                System.out.println("No winner found");
            }

        } while (--rounds > 0); // this condition is used for testing purposes  
         
        // signal mechanic to finish. This code cannot be reached when the simulation runs indefinitely
        for (int i = 0; i < SimulationParameters.NUM_MECHANICS; i++) {
            mechanic[i].finish();
        }

        // signal the bench to finish
        bench.finish();

        // print to confirm the simulation has ended
        System.out.println("Simulation ends.");
    }
}