import Server.Commands.*;
import Server.Robots.Direction;
import Server.Robots.Position;
import Server.Robots.Robot;
import Server.World.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RepairTest {
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
    public void testRobotErrorResponse(){
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
    public void testRobotMovementErrorResponse(){
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
}
