package Server.Commands;

import Server.Robots.Robot;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TurnCommand extends Command {
/**
 * The TurnCommand class represents a command to turn a robot either left or right.
 * It extends the abstract Command class and provides the implementation for the execute method.
 */

    public TurnCommand(JsonArray args) {
        super(args);
    }
    /**
     * Constructs a TurnCommand with the specified arguments.
     *
     * @param args the arguments for the command.
     */

    @Override
    public JsonObject execute(Robot target) {
    /**
     * Executes the turn command on the specified target robot.
     *
     * @param target the robot on which the command is executed.
     * @return a JsonObject containing the result of the execution.
     */

        JsonArray arguments = getArgument();
        String turnDirection = arguments.get(0).getAsString();
        JsonObject response = new JsonObject();
        // Check if the robot is currently repairing or reloading
        if (target.isRepairing()){
            return generateErrorResponse("Robot is currently repairing and can't move");
        }
        else if (target.isReloading()) {
            return generateErrorResponse("Robot is currently repairing and can't move");
        }
        // Turn the robot based on the direction specified
        if (turnDirection.equalsIgnoreCase("right")) {
            target.updateDirection(true);
            JsonObject data = new JsonObject();
            response.addProperty("result", "ok");
            JsonObject jsonData = new JsonObject();
            jsonData.addProperty("message", "done");
            response.add("data", jsonData);
            response.add("state", target.state());

        } else if (turnDirection.equalsIgnoreCase("left")) {
            target.updateDirection(false);
            JsonObject data = new JsonObject();
            response.addProperty("result", "ok");
            JsonObject jsonData = new JsonObject();
            jsonData.addProperty("message", "done");
            response.add("data", jsonData);
            response.add("state", target.state());

        }
        return response;
    }
}