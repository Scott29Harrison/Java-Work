package uk.ac.keele.csc20004.coursework2;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

import uk.ac.keele.csc20004.RepairLine;
import uk.ac.keele.csc20004.robots.Robot;

/**
 * This is a concrete implementation of a RepaiLine.
 * 
 * @author 23018691
 */
public class MyRepairLine implements RepairLine{
    private LinkedList<Robot> queue = new LinkedList<>();
    private final ReentrantLock addLock = new ReentrantLock();
 
    /**
     * Adds a robot to the repair line.
     * 
     * @param robot the robot to add to the repair line
     */
    public void addRobot(Robot robot) {
        addLock.lock();
        do {
            // Wait for queue size to be less than MAX_BENCH_SIZE
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (queue.size() >= SimulationParameters.MAX_REPAIRLINE_SIZE);
        queue.add(robot);
        System.out.println("Number of Robots being Repaired " + queue.size());
        addLock.unlock();
    }

    /**
     * Returns the next robot in the repair line, and removes it from the line.
     * 
     * @return the next robot in the repair line
     */
    public synchronized Robot getNextRobot() {
        return queue.poll();
    }

    /**
     * Prints out the robots in the repair line.
     */
    @Override
    public synchronized void printOut() {
        
        addLock.lock();
        for (Robot robot : queue) {
            System.out.println(robot);
        }
        addLock.unlock();
    }

    /**
     * Returns the number of robots in the repair line.
     * 
     * @return the number of robots in the repair line
     */
    @Override
    public int size() {
        return queue.size();
    }
}
