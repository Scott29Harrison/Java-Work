package uk.ac.keele.csc20004.coursework2;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

import uk.ac.keele.csc20004.Bench;
import uk.ac.keele.csc20004.robots.Robot;

/**
 * This is a concrete implementation of a Bench.
 * 
 * @author 23018691
 */
public class MyBench implements Bench {
    private LinkedList<Robot> queue = new LinkedList<>();
    private final ReentrantLock addLock = new ReentrantLock();
    private volatile boolean running = true;
    /**
     * Adds a robot to the bench.
     * 
     * @param robot the robot to add to the bench
     */
    public void addRobot(Robot robot) {
        addLock.lock();
        do {
            // Wait for queue size to be less than MAX_BENCH_SIZE
            try{
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (queue.size() >= SimulationParameters.MAX_BENCH_SIZE && running);
        
        // Check that the becnh has not been signaled to terminate
        if (running){
            queue.add(robot);
            System.out.println("Number of Robots on the bench " + queue.size());
        }
        addLock.unlock();
    }

    /**
     * This method allows other classes to safely request the bench to finish
     */
    public void finish() {
        running = false;
    }
    
    /**
     * Returns the next robot in the bench, and removes it from the bench.
     * 
     * @return the next robot in the bench
     */
    public synchronized Robot getNextRobot() {
        return queue.poll();
    }

    /**
     * Prints out the robots in the bench.
     */
    @Override
    public synchronized void printOut() {
        for (Robot robot : queue) {
            System.out.println(robot);
        }
    }

    /**
     * Returns the number of robots in the bench.
     * 
     * @return the number of robots in the bench
     */
    @Override
    public int size() {
        return queue.size();
    }
}
