import static org.junit.Assert.*;

import Server.Commands.TurnCommand;
import Server.Robots.Direction;
import Server.Robots.Position;
import Server.World.Obstacles;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import Server.Robots.Robot;

import java.util.ArrayList;
import java.util.List;

@RunWith(Enclosed.class)
public class TurnCommandTest {

    public static class TurnRightTests {
        @Test
        public void testTurnRight() {
            List<Obstacles> testObs = new ArrayList<>();
            List<Robot> testRobs = new ArrayList<>();

            Position initialPosition = new Position(5, 5);
            Position topL = new Position(-50, 50);
            Position botR = new Position(50, -50);

            Robot robot = new Robot("robot", "sniper", 10, testObs, testRobs, 1, 5, initialPosition, 3, 3, Direction.NORTH, 5, 5, topL, botR, 5, 5, 10);

            JsonArray args = new JsonArray();
            args.add("right");

            TurnCommand command = new TurnCommand(args);

            JsonObject response = command.execute(robot);
            boolean ok = response.get("result").getAsString().equalsIgnoreCase("ok");
            assertTrue(ok);
            assertEquals(Direction.EAST, robot.getDirection());
        }


        @Test
        public void testTurnWhileRepairing() {
            List<Obstacles> testObs = new ArrayList<>();
            List<Robot> testRobs = new ArrayList<>();

            Position initialPosition = new Position(5, 5);
            Position topL = new Position(-50, 50);
            Position botR = new Position(50, -50);

            Robot robot = new Robot("robot", "sniper", 10, testObs, testRobs, 1, 5, initialPosition, 3, 3, Direction.NORTH, 5, 5, topL, botR, 5, 5, 10);
            robot.setRepairing(true);

            JsonArray args = new JsonArray();
            args.add("right");

            TurnCommand command = new TurnCommand(args);

            JsonObject response = command.execute(robot);
            boolean error = response.get("result").getAsString().equalsIgnoreCase("ERROR");
            assertTrue(error);
            assertEquals(Direction.NORTH, robot.getDirection());
        }


    }
}
