package Server.Commands;

import Server.Robots.Robot;
import com.google.gson.JsonObject;

public class RepairCommand extends Command {
/**
 * The RepairCommand class represents a command to repair a robot.
 * It extends the abstract Command class and provides the implementation for the execute method.
 */
    @Override
    public JsonObject execute(Robot target) {
    /**
     * Executes the repair command on the specified target robot.
     * If the robot is not currently repairing, it sets the robot's status to "REPAIR"
     * and starts the repair process, simulating repair time with a sleep.
     * Once repaired, it sets the robot's status back to "NORMAL" and returns a JsonObject with the result.
     *
     * @param target the robot on which the command is executed.
     * @return a JsonObject containing the result of the repair command.
     */

        // Set the robot's status to repairing
        target.setStatus("REPAIR");
        target.setRepairing(true);

        // Check if the robot is already fully repaired
        if (target.getIShield() == target.getShield()) {
            return generateErrorResponse("Robot is already repaired");
        } else {
            int repairTime = target.getRepair();
            try {
                Thread.sleep(repairTime * 1000L);
                target.repairShields();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            target.setRepairing(false);
            target.setStatus("NORMAL");

            JsonObject response = new JsonObject();
            response.addProperty("result", "OK");
            JsonObject data = new JsonObject();
            data.addProperty("message", "Done");
            response.add("data", data);
            JsonObject state = target.state();
            response.add("state", state);
            return response;
        }
    }
}
