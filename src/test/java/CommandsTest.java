import static org.junit.Assert.*;
import Server.Commands.BackwardCommand;
import Server.Commands.FireCommand;
import Server.Commands.ForwardCommand;
import Server.Commands.RepairCommand;
import Server.Robots.Direction;
import Server.Robots.Position;
import Server.World.MountainObstacle;
import Server.World.Obstacles;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import Server.Commands.*;
import Server.Robots.*;

public class CommandsTest {
    @Test
    public void testRobotReload() {
        List<Obstacles> testObs = new ArrayList<>();
        List<Robot> testRobs = new ArrayList<>();
        testObs.add(new MountainObstacle(5, -10));

        Position initialPosition = new Position(5, 10);
        Position topL = new Position(-50, 50);
        Position botR = new Position(50, -50);

        Robot robot = new Robot("robot", "sniper", 10, testObs, testRobs, 1, 5, initialPosition, 3, 3, Direction.NORTH, 5, 5, topL, botR, 5, 5, 10);

        JsonArray args = new JsonArray();
        args.add(40);


        FireCommand fireCommand = new FireCommand();
        JsonObject fireResponse = fireCommand.execute(robot);
        boolean fireOk = fireResponse.get("result").getAsString().equals("ok");
        assertTrue(fireOk);
        int iShot = robot.getIShield();

        ReloadCommand reloadCommand = new ReloadCommand();
        JsonObject reloadResponse = reloadCommand.execute(robot);
        boolean reloadOK = reloadResponse.get("result").getAsString().equals("OK");
        assertTrue(reloadOK);
        assertEquals(iShot, robot.getIShot());
    }

    @Test
    public void testRobotErrorResponseReload(){
        List<Obstacles> testObs = new ArrayList<>();
        List<Robot> testRobs = new ArrayList<>();
        testObs.add(new MountainObstacle(5, -10));

        Position initialPosition = new Position(5, 10);
        Position topL = new Position(-50, 50);
        Position botR = new Position(50, -50);
        Robot robot = new Robot("robot", "sniper", 10, testObs, testRobs, 10, 5, initialPosition, 3, 3, Direction.NORTH, 10, 5, topL, botR, 10, 5, 10);


        ReloadCommand reloadCommand = new ReloadCommand();
        JsonObject reloadResponse = reloadCommand.execute(robot);
        JsonObject data = reloadResponse.getAsJsonObject("data");
        String errorMessage = data.get("message").getAsString();
        assertEquals("Robot is already fully loaded.", errorMessage);
        assertEquals(5, robot.getIShot());

    }

    @Test
    public void testRobotMovementErrorResponseReload(){
        List<Obstacles> testObs = new ArrayList<>();
        List<Robot> testRobs = new ArrayList<>();
        testObs.add(new MountainObstacle(5, -10));

        Position initialPosition = new Position(5, 10);
        Position topL = new Position(-50, 50);
        Position botR = new Position(50, -50);
        Robot robot = new Robot("robot", "sniper", 10, testObs, testRobs, 10, 5, initialPosition, 3, 3, Direction.NORTH, 10, 5, topL, botR, 10, 5, 10);

        robot.setReload(true);

        JsonArray args = new JsonArray();
        args.add(15);
        BackwardCommand backwardCommand = new BackwardCommand(args);
        JsonObject response = backwardCommand.execute(robot);
        JsonObject data = response.getAsJsonObject("data");
        String errorMessage = data.get("message").getAsString();
        assertEquals("Robot is currently reloading and can't move", errorMessage);
        assertTrue(robot.isReloading());

    }

    @Test
    public void testHelpCommandExecution() {
        List<Obstacles> testObs = new ArrayList<>();
        List<Robot> testRobs = new ArrayList<>();

        Position initialPosition = new Position(5, 5);
        Position topL = new Position(-50, 50);
        Position botR = new Position(50, -50);

        Robot robot = new Robot("robot", "sniper", 10, testObs, testRobs, 1, 5, initialPosition, 3, 3, Direction.NORTH, 5, 5, topL, botR, 5, 5, 10);

        JsonArray args = new JsonArray();

        HelpCommand command = new HelpCommand(args);

        JsonObject response = command.execute(robot);

        assertTrue(response.has("result"));
        assertEquals("OK", response.get("result").getAsString());

        assertTrue(response.has("data"));
        assertTrue(response.get("data").getAsJsonObject().has("help"));
        assertTrue(response.has("state"));
        assertEquals(robot.state(), response.get("state"));
    }

    @Test
    public void testVisibilityForObstacles() {
        List<Obstacles> testObs = new ArrayList<>();
        List<Robot> testRobs = new ArrayList<>();
        testObs.add(new MountainObstacle(5, -10));

        Position initialPosition = new Position(5, 10);
        Position topL = new Position(-50, 50);
        Position botR = new Position(50, -50);

        Robot target = new Robot("robot", "sniper", 10, testObs, testRobs, 1, 5, initialPosition, 3, 3, Direction.NORTH, 5, 5, topL, botR, 5, 5, 10);


        int tX = target.getPosition().getX();
        int tY = target.getPosition().getY();

        int visibility = target.getVisibility();

        Position look_north = new Position(tX,tY + visibility);
        Position look_south = new Position(tX,tY - visibility);
        Position look_west = new Position(tX - visibility,tY);
        Position look_east = new Position(tX + visibility,tY);


        assertEquals(tX, look_north.getX());
        assertEquals(tY + visibility, look_north.getY());

        assertEquals(tX, look_south.getX());
        assertEquals(tY - visibility, look_south.getY());

        assertEquals(tX -  visibility, look_west.getX());
        assertEquals(tY, look_west.getY());

        assertEquals(tX + visibility, look_east.getX());
        assertEquals(tY, look_east.getY());

    }
    @Test
    public void testVisibilityForRobots(){
        List<Obstacles> testObs = new ArrayList<>();
        List<Robot> testRobs = new ArrayList<>();
        testObs.add(new MountainObstacle(5, -10));

        Position initialPosition = new Position(5, 10);
        Position topL = new Position(-50, 50);
        Position botR = new Position(50, -50);

        Robot target = new Robot("robot", "sniper", 10, testObs, testRobs, 1, 5, initialPosition, 3, 3, Direction.NORTH, 5, 5, topL, botR, 5, 5, 10);

        Position currRobot = target.getPosition();
        int robotX = currRobot.getX();
        int robotY = currRobot.getY();

        int visibility = target.getVisibility();

        Position robot_north = new Position(robotX,robotY + visibility);
        Position robot_south = new Position(robotX,robotY - visibility);
        Position robot_west = new Position(robotX - visibility,robotY);
        Position robot_east = new Position(robotX + visibility,robotY);


        assertEquals(robotX, robot_north.getX());
        assertEquals(robotY + visibility, robot_north.getY());

        assertEquals(robotX, robot_south.getX());
        assertEquals(robotY - visibility, robot_south.getY());

        assertEquals(robotX -  visibility, robot_west.getX());
        assertEquals(robotY, robot_west.getY());

        assertEquals(robotX + visibility, robot_east.getX());
        assertEquals(robotY, robot_east.getY());


    }

    @Test
    public void NorthObstacles(){
        List<Obstacles> testObs = new ArrayList<>();
        List<Robot> testRobs = new ArrayList<>();
        testObs.add(new MountainObstacle(5, -10));

        Position initialPosition = new Position(5, 10);
        Position topL = new Position(-50, 50);
        Position botR = new Position(50, -50);

        Robot target = new Robot("robot", "sniper", 10, testObs, testRobs, 1, 5, initialPosition, 3, 3, Direction.NORTH, 5, 5, topL, botR, 5, 5, 10);

        Position look_north = new Position(target.getPosition().getX(), target.getPosition().getY()+ target.getVisibility());
        Position targetPosition = target.getPosition();
        JsonObject obstacleStats = new JsonObject();
        JsonArray objects = new JsonArray();

        for (Obstacles i : testObs){
            if (look_north.getY() >= i.getBottomLeftY() && targetPosition.getY() <= i.getBottomLeftY() && targetPosition.getX() >= i.getBottomLeftX() && targetPosition.getX() <= i.getBottomLeftX()+ i.getSize()) {
                obstacleStats.addProperty("direction", "NORTH");
                obstacleStats.addProperty("type", i.getType());
                int distance = targetPosition.getY() - i.getBottomLeftY();
                if (distance < 0) distance *= -1;
                obstacleStats.addProperty("distance", distance);
                objects.add(obstacleStats);
                assertEquals("NORTH", obstacleStats.get("direction").getAsString());
                assertEquals("Mountain", obstacleStats.get("type").getAsString());
                assertEquals(20, obstacleStats.get("distance").getAsInt());
            }
        }

    }
    @Test
    public void testBlockPosition(){
        Obstacles obstacles = new MountainObstacle(5,5);


        assertTrue(obstacles.blocksPosition(new Position(7,7)));
        assertFalse(obstacles.blocksPosition(new Position(15,15)));
        assertTrue(obstacles.blocksPosition(new Position(5,5)));
        assertFalse(obstacles.blocksPosition(new Position(15, 5)));
    }

    @Test
    public void testBlockPath(){
        Obstacles obstacles = new MountainObstacle(5,5);

        assertTrue(obstacles.blocksPath(new Position(3,7), new Position(7,7)));
        assertFalse(obstacles.blocksPath(new Position(7,7), new Position(12,7)));
        assertTrue(obstacles.blocksPath(new Position(7,3), new Position(7,7)));
        assertFalse(obstacles.blocksPath(new Position(3,15), new Position(12,15)));
        assertFalse(obstacles.blocksPath(new Position(15,3), new Position(15,12)));
    }

    @Test
    public void testRobotRepairs() {
        List<Obstacles> testObs = new ArrayList<>();
        List<Robot> testRobs = new ArrayList<>();
        testObs.add(new MountainObstacle(5, -10));

        Position initialPosition = new Position(5, 10);
        Position topL = new Position(-50, 50);
        Position botR = new Position(50, -50);

        Robot robot = new Robot("robot", "sniper", 10, testObs, testRobs, 1, 5, initialPosition, 3, 3, Direction.NORTH, 5, 5, topL, botR, 5, 5, 10);

        JsonArray args = new JsonArray();
        args.add(40);


        FireCommand fireCommand = new FireCommand();
        JsonObject fireResponse = fireCommand.execute(robot);
        boolean fireOk = fireResponse.get("result").getAsString().equals("ok");
        assertTrue(fireOk);
        int iShield = robot.getIShield();

        RepairCommand repairCommand = new RepairCommand();
        JsonObject repairResponse = repairCommand.execute(robot);
        boolean repairOK = repairResponse.get("result").getAsString().equals("OK");
        assertTrue(repairOK);
        assertEquals(iShield, robot.getIShield());
    }

    @Test
    public void testRobotErrorResponseRepair(){
        List<Obstacles> testObs = new ArrayList<>();
        List<Robot> testRobs = new ArrayList<>();
        testObs.add(new MountainObstacle(5, -10));

        Position initialPosition = new Position(5, 10);
        Position topL = new Position(-50, 50);
        Position botR = new Position(50, -50);
        Robot robot = new Robot("robot", "sniper", 10, testObs, testRobs, 10, 5, initialPosition, 3, 3, Direction.NORTH, 10, 5, topL, botR, 10, 5, 10);


        RepairCommand repairCommand = new RepairCommand();
        JsonObject repairResponse = repairCommand.execute(robot);
        JsonObject data = repairResponse.getAsJsonObject("data");
        String errorMessage = data.get("message").getAsString();
        assertEquals("Robot is already repaired", errorMessage);
        assertEquals(10, robot.getIShield());

    }

    @Test
    public void testRobotMovementErrorResponseRepair(){
        List<Obstacles> testObs = new ArrayList<>();
        List<Robot> testRobs = new ArrayList<>();
        testObs.add(new MountainObstacle(5, -10));

        Position initialPosition = new Position(5, 10);
        Position topL = new Position(-50, 50);
        Position botR = new Position(50, -50);
        Robot robot = new Robot("robot", "sniper", 10, testObs, testRobs, 10, 5, initialPosition, 3, 3, Direction.NORTH, 10, 5, topL, botR, 10, 5, 10);

        robot.setRepairing(true);

        JsonArray args = new JsonArray();
        args.add(15);
        BackwardCommand backwardCommand = new BackwardCommand(args);
        JsonObject response = backwardCommand.execute(robot);
        JsonObject data = response.getAsJsonObject("data");
        String errorMessage = data.get("message").getAsString();
        assertEquals("Robot is currently repairing and can't move", errorMessage);
        assertTrue(robot.isRepairing());

    }

    public static class fireCommandTests {
        private Robot createTestRobot(Direction direction, int shots, int shield) {
            List<Obstacles> testObs = new ArrayList<>();
            List<Robot> testRobs = new ArrayList<>();
            Position testPos = new Position(5, 5);
            Position topL = new Position(-50, 50);
            Position botR = new Position(50, -50);
            return new Robot("robot", "sniper", 10, testObs, testRobs, 1, shots, testPos, 3, 3, direction, 5, 5, topL, botR, 5, 5, shield);
        }

        private Robot createTargetRobot(Position position, String name) {
            List<Obstacles> testObs = new ArrayList<>();
            List<Robot> testRobs = new ArrayList<>();
            Position topL = new Position(-50, 50);
            Position botR = new Position(50, -50);
            return new Robot(name, "sniper", 10, testObs, testRobs, 5, 5, position, 3, 3, Direction.NORTH, 5, 5, topL, botR, 5, 5, 10);
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
            Robot robot = createTestRobot(Direction.NORTH, 5, 5);
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
            assertEquals(4, robot.getShots());
            assertEquals(4, targetRobot.getShield());
        }

        @Test
        public void testFireCommandMisses() {
            Robot robot = createTestRobot(Direction.NORTH, 5, 5);

            FireCommand fireCommand = new FireCommand();
            JsonArray obstacles = new JsonArray();
            fireCommand.ObstaclesNorth = obstacles;

            JsonObject response = fireCommand.execute(robot);
            boolean ok = response.get("result").getAsString().equals("ok");
            assertTrue(ok);
            assertEquals("Miss", response.getAsJsonObject("data").get("message").getAsString());
            assertEquals(4, robot.getShots());
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
            Robot robot = createTestRobot(Direction.SOUTH, 5, 5);
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
            assertEquals(4, robot.getShots());
            assertEquals(4, targetRobot.getShield());
        }

        @Test
        public void testFireCommandHitsRobotEast() {
            Robot robot = createTestRobot(Direction.EAST, 5, 5);
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
            assertEquals(4, robot.getShots());
            assertEquals(4, targetRobot.getShield());
        }

        @Test
        public void testFireCommandHitsRobotWest() {
            Robot robot = createTestRobot(Direction.WEST, 5, 5);
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
            assertEquals(4, robot.getShots());
            assertEquals(4, targetRobot.getShield());
        }
    }



    public static class mainCommandTests {

        @Test
        public void testCommandCreationState() {
            JsonObject request = new JsonObject();
            request.addProperty("command", "state");
            request.add("arguments", new JsonArray());

            Command command = Command.create(request);
            assertTrue(command instanceof StateCommand);
        }

        @Test
        public void testCommandCreationLook() {
            JsonObject request = new JsonObject();
            request.addProperty("command", "look");
            request.add("arguments", new JsonArray());

            Command command = Command.create(request);
            assertTrue(command instanceof LookCommand);
        }

        @Test
        public void testCommandCreationForward() {
            JsonObject request = new JsonObject();
            request.addProperty("command", "forward");
            JsonArray args = new JsonArray();
            args.add(10);
            request.add("arguments", args);

            Command command = Command.create(request);
            assertTrue(command instanceof ForwardCommand);
            assertEquals(10, command.getArgument().get(0).getAsInt());
        }

        @Test
        public void testCommandCreationInvalidCommand() {
            JsonObject request = new JsonObject();
            request.addProperty("command", "invalid");
            request.add("arguments", new JsonArray());

            Command command = Command.create(request);
            assertTrue(command instanceof Command.ErrorResponse);
            JsonObject response = command.execute(null);

            assertEquals("ERROR", response.get("result").getAsString());
            assertEquals("Unsupported command", response.getAsJsonObject("data").get("message").getAsString());
        }

        @Test
        public void testCommandCreationInvalidArgs() {
            JsonObject request = new JsonObject();
            request.addProperty("command", "forward");
            JsonArray args = new JsonArray();
            args.add("invalid");
            request.add("arguments", args);

            Command command = Command.create(request);
            assertTrue(command instanceof Command.ErrorResponse);
            JsonObject response = command.execute(null);
            assertEquals("ERROR", response.get("result").getAsString());
            assertEquals("Could not parse arguments", response.getAsJsonObject("data").get("message").getAsString());
        }

        @Test
        public void testIsValidArg() {
            JsonArray validArgs = new JsonArray();
            validArgs.add("sniper");
            validArgs.add("tank");
            validArgs.add("brad1");
            validArgs.add("left");
            validArgs.add("right");
            validArgs.add(10);

            assertTrue(Command.isValidArg(validArgs));
        }

        @Test
        public void testIsValidArgInvalid() {
            JsonArray invalidArgs = new JsonArray();
            invalidArgs.add("invalid");

            assertFalse(Command.isValidArg(invalidArgs));
        }

        @Test
        public void testGenerateErrorResponse() {
            JsonObject response = Command.generateErrorResponse("Test error message");

            assertEquals("ERROR", response.get("result").getAsString());
            assertEquals("Test error message", response.getAsJsonObject("data").get("message").getAsString());
        }

        @Test
        public void testForwardCommand() {
            JsonArray args = new JsonArray();
            args.add(5);
            ForwardCommand command = new ForwardCommand(args);
            Robot robot = createTestRobot(Direction.NORTH, new Position(0, 0));
            JsonObject result = command.execute(robot);

            assertEquals("OK", result.get("result").getAsString());
            assertEquals(5, robot.getPosition().getY());
        }

        @Test
        public void testBackwardCommand() {
            JsonArray args = new JsonArray();
            args.add(5);
            BackwardCommand command = new BackwardCommand(args);
            Robot robot = createTestRobot(Direction.NORTH, new Position(0, 5));
            JsonObject result = command.execute(robot);

            assertEquals("OK", result.get("result").getAsString());
            assertEquals(0, robot.getPosition().getY());
        }

        private Robot createTestRobot(Direction direction, Position position) {
            return new Robot("testRobot", "sniper", 100, new ArrayList<>(), new ArrayList<>(), 1, 10, position, 3, 3, direction, 5, 5, new Position(-50, 50), new Position(50, -50), 5, 5, 10);
        }
    }

    public static class RobotTest {
        List<Obstacles> testObs = new ArrayList<>();
        List<Robot> testRobs = new ArrayList<>();

        Position initialPosition = new Position(5, 10);
        Position topL = new Position(-50, 50);
        Position botR = new Position(50, -50);
        Robot testRobot = new Robot("robot", "sniper", 10, testObs, testRobs, 5, 5, initialPosition, 3, 3, Direction.NORTH, 5, 5, topL, botR, 5, 5, 10);

        @Test
        public void testName(){
            String name = testRobot.getName();
            assertEquals(name, "robot");
        }

        @Test
        public void testDirection(){
            Direction direction = testRobot.getDirection();
            assertEquals(direction, Direction.NORTH);
        }

        @Test
        public void testPosition(){
            Position position = testRobot.getPosition();
            assertEquals(position, initialPosition);
        }

        @Test
        public void testShots(){
            int shots = testRobot.getShots();
            assertEquals(shots, 5);
        }

        @Test
        public void testShield(){
            int shield = testRobot.getShield();
            assertEquals(shield, 5);
        }

        @Test
        public void testObstacles(){
            List<Obstacles> obstacles = testRobot.getObstacles();
            assertEquals(obstacles, testObs);
        }

        @Test
        public void testRobotList(){
            List<Robot> robotList = testRobot.getRobotList();
            assertEquals(robotList, testRobs);
        }

        @Test
        public void testGetTopLeft(){
            Position topLeft = testRobot.getTOP_LEFT();
            assertEquals(topLeft, topL);
        }

        @Test
        public void testGetBottomRight(){
            Position bottomRight = testRobot.getBOTTOM_RIGHT();
            assertEquals(bottomRight, botR);
        }

        @Test
        public void testGetInitialPosition(){
            Position initialPosition = testRobot.getPosition();
            assertEquals(initialPosition, this.initialPosition);
        }

        @Test
        public void testGetInitialDirection(){
            Direction initialDirection = testRobot.getDirection();
            assertEquals(initialDirection, Direction.NORTH);
        }

        @Test
        public void testGetInitialShots(){
            int initialShots = testRobot.getShots();
            assertEquals(initialShots, 5);
        }

        @Test
        public void testGetInitialShield(){
            int initialShield = testRobot.getShield();
            assertEquals(initialShield, 5);
        }


        @Test
        public void testGetInitialObstacles(){
            List<Obstacles> initialObstacles = testRobot.getObstacles();
            assertEquals(initialObstacles, testObs);
        }

        @Test
        public void testGetInitialRobotList(){
            List<Robot> initialRobotList = testRobot.getRobotList();
            assertEquals(initialRobotList, testRobs);
        }

        @Test
        public void testGetInitialTopLeft(){
            Position initialTopLeft = testRobot.getTOP_LEFT();
            assertEquals(initialTopLeft, topL);
        }

        @Test
        public void testGetInitialBottomRight(){
            Position initialBottomRight = testRobot.getBOTTOM_RIGHT();
            assertEquals(initialBottomRight, botR);
        }
    }
}
