import static  org.junit.Assert.*;

import Server.Commands.BackwardCommand;
import Server.Commands.ForwardCommand;
import Server.Robots.Direction;
import Server.Robots.Position;
import Server.World.MountainObstacle;
import Server.World.Obstacles;
import Server.World.World;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import Server.Commands.RepairCommand;
import Server.Robots.Robot;

import java.util.ArrayList;
import java.util.List;

public class CommandsTest {


    @Test
    public void testC(){
        List<Obstacles> testObs = new ArrayList<>();
        testObs.add(new MountainObstacle(5,5));

        List<Robot> testRobs = new ArrayList<>();

        Position testPos = new Position(5,5);
        Position topL = new Position(-50,50);
        Position botR = new Position(50,-50);
        Robot robot = new Robot("robot","sniper",10,testObs,testRobs,1,5,testPos,3,3, Direction.NORTH,5,5,topL,botR,5,5,10);

        RepairCommand command = new RepairCommand();
        robot.setRepair(5);
        JsonObject response = command.execute(robot);
        boolean ok = (response.get("result").toString().contains("OK"));
        assertTrue(ok);
    }

    public static class ForwardCommandTests {

        @Test
        public void testRobotMovesForwardSuccessfully() {
            List<Obstacles> testObs = new ArrayList<>();
            List<Robot> testRobs = new ArrayList<>();

            Position initialPosition = new Position(5, 5);
            Position topL = new Position(-50, 50);
            Position botR = new Position(50, -50);

            Robot robot = new Robot("robot", "sniper", 10, testObs, testRobs, 1, 5, initialPosition, 3, 3, Direction.NORTH, 5, 5, topL, botR, 5, 5, 10);

            JsonArray args = new JsonArray();
            args.add(5);

            ForwardCommand command = new ForwardCommand(args);

            JsonObject response = command.execute(robot);
            boolean ok = response.get("result").getAsString().equals("OK");
            assertTrue(ok);
            assertEquals(10, robot.getPosition().getY());
        }
        @Test
        public void testRobotMovesOutOfBounds() {
            List<Obstacles> testObs = new ArrayList<>();
            List<Robot> testRobs = new ArrayList<>();

            Position initialPosition = new Position(5, 5);
            Position topL = new Position(-50, 50);
            Position botR = new Position(50, -50);

            Robot robot = new Robot("robot", "sniper", 10, testObs, testRobs, 1, 5, initialPosition, 3, 3, Direction.NORTH, 5, 5, topL, botR, 5, 5, 10);

            JsonArray args = new JsonArray();
            args.add(100);

            ForwardCommand command = new ForwardCommand(args);

            JsonObject response = command.execute(robot);
            boolean ok = response.get("result").getAsString().equals("OK");
            assertTrue(ok);
            assertEquals(5, robot.getPosition().getY());
        }
        @Test
        public void testRobotPathObstructed() {
            List<Obstacles> testObs = new ArrayList<>();
            List<Robot> testRobs = new ArrayList<>();
            testObs.add(new MountainObstacle(5,15));

            Position initialPosition = new Position(5, 10);
            Position topL = new Position(-50, 50);
            Position botR = new Position(50, -50);

            Robot robot = new Robot("robot", "sniper", 10, testObs, testRobs, 1, 5, initialPosition, 3, 3, Direction.NORTH, 5, 5, topL, botR, 5, 5, 10);

            JsonArray args = new JsonArray();
            args.add(20);

            ForwardCommand command = new ForwardCommand(args);

            JsonObject response = command.execute(robot);
            boolean ok = response.get("result").getAsString().equals("OK");
            assertTrue(ok);
            assertEquals(10, robot.getPosition().getY());
        }
    }

    public static class BackwardCommandTests {
        @Test
        public void testRobotMovesBackwardSuccessfully() {
            List<Obstacles> testObs = new ArrayList<>();
            List<Robot> testRobs = new ArrayList<>();

            Position initialPosition = new Position(5, 5);
            Position topL = new Position(-50, 50);
            Position botR = new Position(50, -50);

            Robot robot = new Robot("robot", "sniper", 10, testObs, testRobs, 1, 5, initialPosition, 3, 3, Direction.NORTH, 5, 5, topL, botR, 5, 5, 10);

            JsonArray args = new JsonArray();
            args.add(5);

            BackwardCommand command = new BackwardCommand(args);

            JsonObject response = command.execute(robot);
            boolean ok = response.get("result").getAsString().equals("OK");
            assertTrue(ok);
            assertEquals(0, robot.getPosition().getY());
        }
        @Test
        public void testRobotMovesOutOfBounds() {
            List<Obstacles> testObs = new ArrayList<>();
            List<Robot> testRobs = new ArrayList<>();

            Position initialPosition = new Position(5, 5);
            Position topL = new Position(-50, 50);
            Position botR = new Position(50, -50);

            Robot robot = new Robot("robot", "sniper", 10, testObs, testRobs, 1, 5, initialPosition, 3, 3, Direction.NORTH, 5, 5, topL, botR, 5, 5, 10);

            JsonArray args = new JsonArray();
            args.add(100);

            BackwardCommand command = new BackwardCommand(args);

            JsonObject response = command.execute(robot);
            boolean ok = response.get("result").getAsString().equals("OK");
            assertTrue(ok);
            assertEquals(5, robot.getPosition().getY());
        }
        @Test
        public void testRobotPathObstructed() {
            List<Obstacles> testObs = new ArrayList<>();
            List<Robot> testRobs = new ArrayList<>();
            testObs.add(new MountainObstacle(5,-10));

            Position initialPosition = new Position(5, 10);
            Position topL = new Position(-50, 50);
            Position botR = new Position(50, -50);

            Robot robot = new Robot("robot", "sniper", 10, testObs, testRobs, 1, 5, initialPosition, 3, 3, Direction.NORTH, 5, 5, topL, botR, 5, 5, 10);

            JsonArray args = new JsonArray();
            args.add(40);

            BackwardCommand command = new BackwardCommand(args);

            JsonObject response = command.execute(robot);
            boolean ok = response.get("result").getAsString().equals("OK");
            assertTrue(ok);
            assertEquals(10, robot.getPosition().getY());
        }
    }

}
