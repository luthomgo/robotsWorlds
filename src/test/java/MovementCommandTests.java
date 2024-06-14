import Server.Commands.BackwardCommand;
import Server.Commands.ForwardCommand;
import Server.Robots.Direction;
import Server.Robots.Position;
import Server.Robots.Robot;
import Server.World.MountainObstacle;
import Server.World.Obstacles;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MovementCommandTests {
        @Test
        public void TestRobotMovesForwardSuccessfully() {
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
        public void TestRobotForwardMovesOutOfBounds() {
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
        public void TestRobotForwardPathObstructed() {
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

        @Test
        public void TestRobotMovesBackwardSuccessfully() {
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
        public void TestRobotBackwardMovesOutOfBounds() {
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
        public void TestRobotBackwardPathObstructed() {
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


