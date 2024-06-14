package Server.Commands;

import Server.Robots.Robot;
import com.google.gson.JsonObject;

public class StateCommand extends Command {
/**
 * The StateCommand class represents a command to get the state of a robot.
 * It extends the abstract Command class and provides the implementation for the execute method.
 */

    @Override
    public JsonObject execute(Robot target) {
    /**
     * Executes the state command on the specified target robot.
     *
     * @param target the robot on which the command is executed.
     * @return a JsonObject containing the state of the robot.
     */

    JsonObject response = new JsonObject();
    response.add("state",target.state());
    return response;
    }
}
