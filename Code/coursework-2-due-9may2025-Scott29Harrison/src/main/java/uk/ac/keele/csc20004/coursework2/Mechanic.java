package uk.ac.keele.csc20004.coursework2;

import uk.ac.keele.csc20004.robots.Robot;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This is a thread class for a mechanic that runs concurrently.
 * 
 * @author 23018691
 */
public class Mechanic extends Thread {
    
    private MyRepairLine repairLine;
    private MyBench bench;
    private MyConcurrentArena arena;
    private int mechanicID;
    private final ReentrantLock frameLock = new ReentrantLock();
    private final ReentrantLock motorLock = new ReentrantLock();
    private final ReentrantLock sensorLock = new ReentrantLock();
    private final ReentrantLock actuatorLock = new ReentrantLock();
    private volatile boolean running = true;

    /**
     * This is the constructor for this class.
     * 
     * @param repairLine the repair line used for the arena
     * @param bench the bench used for the arena
     * @param arena the arena for the simulation
     * @param mechanicID an identifier for the mechanic
     */
    public Mechanic(MyRepairLine repairLine, MyBench bench, MyConcurrentArena arena, int mechanicID) {
        this.repairLine = repairLine; 
        this.bench = bench;
        this.arena = arena;
        this.mechanicID = mechanicID;
    }

    /**
     * This method allows other classes to safely request mechanics to finish
     */
    public void finish() {
        running = false;
    }

    /**
     * This is the method that allows the Mechanic to run concurrently
     */
    @Override
    public void run() {
        // create a while loop that causes the mechanic to run indefinitely until the finish method is called
        do {

            // retrieve robots from the repair line until it recieve a robot that isn't null
            Robot r = repairLine.getNextRobot();
            if (r != null) {
                
                System.out.println();
                System.out.println("Mechanic "+ mechanicID +" repairing " + r.toString());
                System.out.println();

                // simulate the delay for a mechanic to complete their operations
                try {
                    Thread.sleep((long) Math.random() * SimulationParameters.OPERATION_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // lock each bay in sequence and unlock them if they are not required for repair
                frameLock.lock();
                if (r.getFrameEnergy() != 0.0) {
                    frameLock.unlock();
                }
                motorLock.lock();
                if (r.getMotorEnergy() != 0.0) {
                    motorLock.unlock();
                }
                sensorLock.lock();
                if (r.getSensorsEnergy() != 0.0) {
                    sensorLock.unlock();
                }
                actuatorLock.lock();
                if (r.getActuatorsEnergy() != 0.0) {
                    actuatorLock.unlock();
                }

                // repair the robot
                arena.repair(r);

                // unlock any used bays
                if (this.frameLock.isLocked()){
                    frameLock.unlock();
                }
                if (this.motorLock.isLocked()){
                    motorLock.unlock();
                }                
                if (this.sensorLock.isLocked()){
                    sensorLock.unlock();
                }                
                if (this.actuatorLock.isLocked()){
                    actuatorLock.unlock();
                }               

                // add the repaired robot to the bench
                bench.addRobot(r);
            } else {
                // wait when the robot is null
                try {
                    Thread.sleep((long) Math.random() * SimulationParameters.OPERATION_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (running);
     }
}
