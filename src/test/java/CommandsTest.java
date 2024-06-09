import static  org.junit.Assert.*;

import Server.Robots.Direction;
import Server.Robots.Position;
import Server.World.MountainObstacle;
import Server.World.Obstacles;
import com.google.gson.JsonObject;

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

}
