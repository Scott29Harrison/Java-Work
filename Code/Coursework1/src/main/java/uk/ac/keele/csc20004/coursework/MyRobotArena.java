package uk.ac.keele.csc20004.coursework;

import uk.ac.keele.csc20004.Bench;
import uk.ac.keele.csc20004.CombatPair;
import uk.ac.keele.csc20004.RepairLine;
import uk.ac.keele.csc20004.RobotArena;
import uk.ac.keele.csc20004.robots.CycloBot;
import uk.ac.keele.csc20004.robots.HumanoidRobot;
import uk.ac.keele.csc20004.robots.Robot;
import uk.ac.keele.csc20004.robots.WheeledRobot;

/**
 * A concrete implementation of a Robot Arena.
 * 
 * @author 23018691
 */
public class MyRobotArena extends RobotArena {
    private static enum RobotType {
        HUMANOID,
        WHEELED,
        CYCLOBOT,
        SHIELDEDHUMANOID,
        SHIELDEDWHEELED,
        SHIELDEDCYCLO,
        SHIELDEDTANK,
        TANKROBOT;
    }
    /**
     * This constructs the arena for the robots to fight in.
     * 
     * @param studentId my student id (only used for some print outs in the code)
     * @param capacity the maximum number of robots in this arena
     * @param repairLine the repairline used to repair robots from the arena
     * @param bench the bench used for this arena, robots on the bench are waiting to fight
     */
    public MyRobotArena(int studentId, int capacity, RepairLine repairLine, Bench bench) {
        super(studentId, capacity, repairLine, bench);
        RobotType[] values = RobotType.values();

        // this creates a set of robots for the arena based on the capacity and the number of different types of robot
        for (int i = 0; i < capacity; i++) {
            RobotType t = values[i % values.length];
            Robot r = createRobot(i, t);
            addRobot(r);
        }
    }
    /**
     * This method simulates the repair of a robot on the repairline
     * Only components with 0 remaining energy are repaired
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
     *  
     * @param robotId the ID of the robot
     * @param type the type of robot to create
     * @return the robot created
     */
    private Robot createRobot(int robotId, RobotType type) {
        RobotPartGetter rpg = new RobotPartGetter();
        switch (type) {
            case HUMANOID:
                return new HumanoidRobot("R-" + robotId);
            case WHEELED:
                return new WheeledRobot("R-" + robotId);
            case TANKROBOT:
                return new TankRobot("R-" + robotId);
            case SHIELDEDHUMANOID:
                HumanoidRobot humanoidRobotToShield = new HumanoidRobot("R-" + robotId);
                String humanoidParts = humanoidRobotToShield.getPartsDescriptions();
                return new ShieldedRobot(humanoidRobotToShield.getName(), rpg.getFrame(humanoidParts), rpg.getMotors(humanoidParts), rpg.getSensors(humanoidParts), rpg.getActuators(humanoidParts), humanoidRobotToShield.toString());
            case SHIELDEDWHEELED:
                WheeledRobot wheeledRobotToShield = new WheeledRobot("R-" + robotId);
                String wheeledParts = wheeledRobotToShield.getPartsDescriptions();
                return new ShieldedRobot(wheeledRobotToShield.getName(), rpg.getFrame(wheeledParts), rpg.getMotors(wheeledParts), rpg.getSensors(wheeledParts), rpg.getActuators(wheeledParts), wheeledRobotToShield.toString());
            case SHIELDEDTANK:
                TankRobot tankRobotToShield = new TankRobot("R-" + robotId);
                String tankParts = tankRobotToShield.getPartsDescriptions();
                return new ShieldedRobot(tankRobotToShield.getName(), rpg.getFrame(tankParts), rpg.getMotors(tankParts), rpg.getSensors(tankParts), rpg.getActuators(tankParts), tankRobotToShield.toString());
            case SHIELDEDCYCLO:
                CycloBot CycloBotToShield = new CycloBot("R-" + robotId);
                String CycloParts = CycloBotToShield.getPartsDescriptions();
                return new ShieldedRobot(CycloBotToShield.getName(), rpg.getFrame(CycloParts), rpg.getMotors(CycloParts), rpg.getSensors(CycloParts), rpg.getActuators(CycloParts), CycloBotToShield.toString());
            case CYCLOBOT:
            default:
                return new CycloBot("R-" + robotId);
        }
    }   
    public static void main(String[] args) {
        Bench bench = new MyRobotBench();
        RepairLine repairLine = new MyRobotRepairLine();
        MyRobotArena arena = new MyRobotArena(23018691, SimulationParameters.NUM_ROBOTS, repairLine, bench);

        System.out.println("Simulation begins...");
        System.out.println("Base strike intensity: " + SimulationParameters.STRIKE_INTENSITY);
        arena.printStats();

        int rounds = 30;

        do { 
            // Select a random integer between 0 and size of arena/2
            // This will be the number of fights (since each fight involves 2 robots
            // it makes sense to involve at most half the number of robots in the arena)
            int numFights = (int) (Math.random() * (arena.getNumRobots() / 2));

            System.out.println();
            System.out.println("Simulating " + numFights + " fights");
            System.out.println();

            for (int i = 0; i < numFights; i++) {
                CombatPair p = arena.getOpponents();
                System.out.println("Fight #" + (i+1) + ":");
                System.out.println(String.format("%10s", "before: ") + p);
                p.fight();
                System.out.println(String.format("%10s", "after: ") + p);

                double energy1 = p.getRobot1().getFrameEnergy() + 
                    p.getRobot1().getMotorEnergy() + 
                    p.getRobot1().getSensorsEnergy() + 
                    p.getRobot1().getActuatorsEnergy();

                if (energy1 > 0) {
                    arena.addRobot(p.getRobot1());
                } else {
                    repairLine.addRobot(p.getRobot1());
                }

                double energy2 = p.getRobot2().getFrameEnergy() + 
                    p.getRobot2().getMotorEnergy() + 
                    p.getRobot2().getSensorsEnergy() + 
                    p.getRobot2().getActuatorsEnergy();

                if (energy2 > 0) {
                    arena.addRobot(p.getRobot2());
                } else {
                    repairLine.addRobot(p.getRobot2());
                }
            }
            System.out.println();
            arena.printStats();
            System.out.println();

            // Select a random integer between 0 and size of repair line
            // This will be the number of robots to repair
            int numRepairs = (int) (Math.random() * repairLine.size());

            for (int i = 0; i < numRepairs; i++) {
                Robot r = repairLine.getNextRobot();

                arena.repair(r);

                bench.addRobot(r);
            }

            // Select a random integer between 0 and size of bench
            // This will be the number of robots to add back to the arena
            int numBench = (int) (Math.random() * bench.size());
            for (int i = 0; i < numBench; i++) {
                Robot r = bench.getNextRobot();
                arena.addRobot(r);
            }
        } while (--rounds > 0);

    }


}
