import static  org.junit.Assert.*;

import com.google.gson.JsonObject;

import org.junit.Test;
import Server.Commands.RepairCommand;
import Server.Robots.Robot;

public class RepairTest {

    @Test
    public void testCommandWhileRepairingIsSuccess(){
        Robot robot = new Robot("Jon", "sniper", 5, 6, 5);
        RepairCommand command = new RepairCommand();
        robot.setRepair(5);
        JsonObject response = command.execute(robot);
        assertEquals("OK", response.get("result").toString());
        assertEquals("Done", response.get("data").toString());
        assertEquals("NORMAL", robot.getStatus());
        assertFalse(robot.isRepairing());

    }

}
