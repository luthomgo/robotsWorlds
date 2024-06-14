package Server.Commands;

import Server.Robots.Robot;
import com.google.gson.JsonObject;

public class ExitCommand extends Command {
/**
 * The ExitCommand class represents a command to remove a robot from the world.
 * It extends the Command class and provides the implementation for executing
 * the exit command on a target robot.
 */

    @Override
    public JsonObject execute(Robot target) {
    /**
     * Executes the exit command on the specified target robot.
     * This method removes the target robot from the world and returns a response
     * indicating the success of the operation.
     *
     * @param target the robot on which the command is executed.
     * @return a JsonObject containing the result of the execution and a message indicating exit.
     */
        JsonObject response = new JsonObject();
        target.removeRobot(target);
        response.addProperty("result","OK");
        JsonObject data = new JsonObject();
        data.addProperty("message","exit");
        return response;
    }
}
