package uk.ac.keele.csc20004.coursework;

import java.util.ArrayDeque;
import java.util.Queue;
import uk.ac.keele.csc20004.Bench;
import uk.ac.keele.csc20004.robots.Robot;

/**
 * A concrete of implementation of a Bench.
 * This is a first in first out queue of robots waiting to enter the arena.
 * 
 * @author 23018691
 */

public class MyRobotBench implements Bench {
    private Queue<Robot> queue = new ArrayDeque<>();
    /**
     * Adds a robot to the bench.
     * 
     * @param robot the robot to add to the bench
     */
    public void addRobot(Robot robot) {
        queue.add(robot);
    }

    /**
     * Retrieves the next robot from the bench.
     * 
     * @return the next robot in the queue
     */
    public Robot getNextRobot() {
        return queue.poll();
    }

    /**
     * Prints out the robots in the bench.
     */
    public void printOut() {
        for (Robot robot : queue) {
            System.out.println(robot);
        }
    }

    /**
     * Returns the number of robots in the bench.
     * 
     * @return the number of robots in the bench
     */
    
    public int size() {
        return queue.size();
    }
}
