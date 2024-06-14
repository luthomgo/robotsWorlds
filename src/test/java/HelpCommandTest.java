import Server.Commands.*;
import Server.Robots.*;
import Server.Robots.Position;
import Server.World.Obstacles;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Enclosed.class)
public class HelpCommandTest {


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



}
