import static  org.junit.Assert.*;

import Server.Commands.BackwardCommand;
import Server.Commands.FireCommand;
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

    public static class fireCommandTests {
        private Robot createTestRobot(Direction direction, int shots, int shield) {
            List<Obstacles> testObs = new ArrayList<>();
            List<Robot> testRobs = new ArrayList<>();
            Position testPos = new Position(5, 5);
            Position topL = new Position(-50, 50);
            Position botR = new Position(50, -50);
            return new Robot("robot", "sniper", 10, testObs, testRobs, 1, 5, testPos, shots, shield, direction, 5, 5, topL, botR, 5, 5, 10);
        }

        private Robot createTargetRobot(Position position, String name) {
            List<Obstacles> testObs = new ArrayList<>();
            List<Robot> testRobs = new ArrayList<>();
            Position topL = new Position(-50, 50);
            Position botR = new Position(50, -50);
            return new Robot(name, "sniper", 10, testObs, testRobs, 1, 5, position, 3, 3, Direction.NORTH, 5, 5, topL, botR, 5, 5, 10);
        }

        private void addObstacle(JsonArray obstacles, String type, String direction, int distance, String name) {
            JsonObject obstacle = new JsonObject();
            obstacle.addProperty("type", type);
            obstacle.addProperty("direction", direction);
            obstacle.addProperty("distance", distance);
            if (name != null) {
                obstacle.addProperty("name", name);
            }
            obstacles.add(obstacle);
        }

        @Test
        public void testFireCommandHitsRobotNorth() {
            Robot robot = createTestRobot(Direction.NORTH, 3, 3);
            Robot targetRobot = createTargetRobot(new Position(5, 10), "targetRobot");
            robot.getRobotList().add(targetRobot);

            FireCommand fireCommand = new FireCommand();
            JsonArray obstacles = new JsonArray();
            addObstacle(obstacles, "Robot", "NORTH", 5, "targetRobot");
            fireCommand.ObstaclesNorth = obstacles;

            JsonObject response = fireCommand.execute(robot);
            boolean ok = response.get("result").getAsString().equals("ok");
            assertTrue(ok);
            assertEquals("Hit", response.getAsJsonObject("data").get("message").getAsString());
            assertEquals(5, response.getAsJsonObject("data").get("distance").getAsInt());
            assertEquals("targetRobot", response.getAsJsonObject("data").get("robot").getAsString());
            assertEquals(2, robot.getShots());
            assertEquals(2, targetRobot.getShield());
        }

        @Test
        public void testFireCommandMisses() {
            Robot robot = createTestRobot(Direction.NORTH, 3, 3);

            FireCommand fireCommand = new FireCommand();
            JsonArray obstacles = new JsonArray();
            fireCommand.ObstaclesNorth = obstacles;

            JsonObject response = fireCommand.execute(robot);
            boolean ok = response.get("result").getAsString().equals("ok");
            assertTrue(ok);
            assertEquals("Miss", response.getAsJsonObject("data").get("message").getAsString());
            assertEquals(2, robot.getShots());
        }

        @Test
        public void testFireCommandEmptyGun() {
            Robot robot = createTestRobot(Direction.NORTH, 0, 3);

            FireCommand fireCommand = new FireCommand();
            JsonArray obstacles = new JsonArray();
            fireCommand.ObstaclesNorth = obstacles;

            JsonObject response = fireCommand.execute(robot);
            boolean ok = response.get("result").getAsString().equals("ok");
            assertTrue(ok);
            assertEquals("Gun empty; Reload!", response.getAsJsonObject("data").get("message").getAsString());
            assertEquals(0, robot.getShots());
        }

        @Test
        public void testFireCommandHitsRobotSouth() {
            Robot robot = createTestRobot(Direction.SOUTH, 3, 3);
            Robot targetRobot = createTargetRobot(new Position(5, 0), "targetRobot");
            robot.getRobotList().add(targetRobot);

            FireCommand fireCommand = new FireCommand();
            JsonArray obstacles = new JsonArray();
            addObstacle(obstacles, "Robot", "SOUTH", 5, "targetRobot");
            fireCommand.ObstaclesSouth = obstacles;

            JsonObject response = fireCommand.execute(robot);
            boolean ok = response.get("result").getAsString().equals("ok");
            assertTrue(ok);
            assertEquals("Hit", response.getAsJsonObject("data").get("message").getAsString());
            assertEquals(5, response.getAsJsonObject("data").get("distance").getAsInt());
            assertEquals("targetRobot", response.getAsJsonObject("data").get("robot").getAsString());
            assertEquals(2, robot.getShots());
            assertEquals(2, targetRobot.getShield());
        }

        @Test
        public void testFireCommandHitsRobotEast() {
            Robot robot = createTestRobot(Direction.EAST, 3, 3);
            Robot targetRobot = createTargetRobot(new Position(10, 5), "targetRobot");
            robot.getRobotList().add(targetRobot);

            FireCommand fireCommand = new FireCommand();
            JsonArray obstacles = new JsonArray();
            addObstacle(obstacles, "Robot", "EAST", 5, "targetRobot");
            fireCommand.ObstaclesEast = obstacles;

            JsonObject response = fireCommand.execute(robot);
            boolean ok = response.get("result").getAsString().equals("ok");
            assertTrue(ok);
            assertEquals("Hit", response.getAsJsonObject("data").get("message").getAsString());
            assertEquals(5, response.getAsJsonObject("data").get("distance").getAsInt());
            assertEquals("targetRobot", response.getAsJsonObject("data").get("robot").getAsString());
            assertEquals(2, robot.getShots());
            assertEquals(2, targetRobot.getShield());
        }

        @Test
        public void testFireCommandHitsRobotWest() {
            Robot robot = createTestRobot(Direction.WEST, 3, 3);
            Robot targetRobot = createTargetRobot(new Position(0, 5), "targetRobot");
            robot.getRobotList().add(targetRobot);

            FireCommand fireCommand = new FireCommand();
            JsonArray obstacles = new JsonArray();
            addObstacle(obstacles, "Robot", "WEST", 5, "targetRobot");
            fireCommand.ObstaclesWest = obstacles;

            JsonObject response = fireCommand.execute(robot);
            boolean ok = response.get("result").getAsString().equals("ok");
            assertTrue(ok);
            assertEquals("Hit", response.getAsJsonObject("data").get("message").getAsString());
            assertEquals(5, response.getAsJsonObject("data").get("distance").getAsInt());
            assertEquals("targetRobot", response.getAsJsonObject("data").get("robot").getAsString());
            assertEquals(2, robot.getShots());
            assertEquals(2, targetRobot.getShield());
        }
    }

}
