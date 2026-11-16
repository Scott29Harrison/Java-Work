package uk.ac.keele.csc20004.coursework2;

import uk.ac.keele.csc20004.CombatPair;
import uk.ac.keele.csc20004.robots.Robot;

/**
 * This is a thread class for a battle manager that runs concurrently.
 * 
 * @author 23018691
 */
public class BattleManager extends Thread {
    
    private MyRepairLine repairLine;
    private MyBench bench;
    private MyConcurrentArena arena;
    private int managerID;
    private CombatPair combatPair;
 
    /**
     * This is the constructor for this class.
     * 
     * @param repairLine the repair line used for the arena
     * @param bench the bench used for the arena
     * @param arena the arena for the simulation
     * @param managerID an identifier for the battle manager
     */
    public BattleManager(MyRepairLine repairLine, MyBench bench, MyConcurrentArena arena, int managerID) {
        this.repairLine = repairLine; 
        this.bench = bench;
        this.arena = arena;
        this.managerID = managerID;
    }

    /**
     * This is the method that allows the Mechanic to run concurrently
     */
    @Override
    public void run() {
        // randomly determine the number of rounds the manager will run for
        int numRounds = (int) (Math.random() * (150 / (SimulationParameters.NUM_BATTLEMANAGERS*2)));
        

        System.out.println();
        System.out.println("Manager " + managerID +" Simulating " + numRounds + " fights");
        System.out.println();

        // create a while loop that runs until the manager has run all its rounds
        while (--numRounds > 0){

            // retrieve the opponents for the fight. If the number of battle managers is over half the number of
            // robots in the arena, the number of opponent pairs will be too low for all battle managers to
            // retrieve a pair to fight. This will be indicated by the string "Not enough robots to fight" being output.
            synchronized (arena){
                System.out.println("Manager " + this.managerID + " Get Opponents");
                combatPair = arena.getOpponents();
            }

            // have the robots fight eachother
            System.out.println(String.format("%10s", "before: ") + "Manager " + managerID + " " + combatPair);
            combatPair.fight();
            System.out.println(String.format("%10s", "after: ") + "Manager " + managerID + " " + combatPair);

            // simulate the delay for the manager to complete its operations
            try {
                Thread.sleep((long) Math.random() * SimulationParameters.OPERATION_DELAY);
                System.out.println("Manager " + this.managerID + " Operation Delay");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // check if the first robot needs repair and place it on the repair line if needed
            if ((combatPair.getRobot1().getFrameEnergy() > 0) && 
                (combatPair.getRobot1().getMotorEnergy() > 0) && 
                (combatPair.getRobot1().getSensorsEnergy() > 0) && 
                (combatPair.getRobot1().getActuatorsEnergy() > 0)) {
                    System.out.println("Manager " + this.managerID + " Robot 1 Return");
                    synchronized (arena){
                        arena.addRobot(combatPair.getRobot1());
                    }
            } else {
                // take a robot off the bench if both the bench and repair line are full to prevent a livelock
                if ((bench.size() >= SimulationParameters.MAX_BENCH_SIZE) && (repairLine.size() >= SimulationParameters.MAX_REPAIRLINE_SIZE)) {
                    System.out.println("Manager " + this.managerID + " retrieving Robot from bench");
                    Robot r = bench.getNextRobot();
                    synchronized (arena){
                        arena.addRobot(r);
                    }
                }
                System.out.println("Manager " + this.managerID + " Robot 1 Sent to Repair");
                repairLine.addRobot(combatPair.getRobot1());
            }

            // check if the second robot needs repair and place it on the repair line if needed
            if ((combatPair.getRobot2().getFrameEnergy() > 0) && 
                (combatPair.getRobot2().getMotorEnergy() > 0) && 
                (combatPair.getRobot2().getSensorsEnergy() > 0) && 
                (combatPair.getRobot2().getActuatorsEnergy() > 0)) {
                System.out.println("Manager " + this.managerID + " Robot 2 Return");
                synchronized (arena){
                    arena.addRobot(combatPair.getRobot2());
                }
            } else {
                // take a robot off the bench if both the bench and repair line are full to prevent a livelock
                if ((bench.size() >= SimulationParameters.MAX_BENCH_SIZE) && (repairLine.size() >= SimulationParameters.MAX_REPAIRLINE_SIZE)) {
                    System.out.println("Manager " + this.managerID + " retrieving Robot from bench");
                    Robot r = bench.getNextRobot();
                    synchronized (arena){
                        arena.addRobot(r);
                    }
                }
                System.out.println("Manager " + this.managerID + " Robot 2 Sent to Repair");
                repairLine.addRobot(combatPair.getRobot2());
            }

            // take a robot off the bench if there is one on the bench
            if (bench.size() > 0) {
                System.out.println("Manager " + this.managerID + " retrieving Robot from bench");
                Robot r = bench.getNextRobot();
                synchronized (arena){
                    arena.addRobot(r);
                }
            }
        }
        System.out.println("Manager " + this.managerID + " All fights Complete");
    }
}
