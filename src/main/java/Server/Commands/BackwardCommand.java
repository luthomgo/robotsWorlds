package Server.Commands;

import Server.Robots.Robot;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class BackwardCommand extends Command{
    /**
     * The BackwardCommand class represents a command to move a robot backward by a specified number of steps.
     * It extends the Command class and overrides the execute method to perform the backward movement.
     */
    @Override
    public JsonObject execute(Robot target) {
        /**
         * Executes the backward command on the specified robot.
         *
         * @param target the robot on which the command is executed.
         * @return a JsonObject containing the result of the command execution, including the robot's state.
         */

        JsonObject response = new JsonObject();

        if (target.isRepairing()){
            return generateErrorResponse("Robot is currently repairing and can't move");
        }else if (target.isReloading()) {
            return generateErrorResponse("Robot is currently reloading and can't move");
        }

        int nrSteps = Integer.parseInt(getArgument().getAsString());
        if (target.updatePosition(-nrSteps)){
            response.addProperty("result","OK");
            JsonObject data = new JsonObject();
            data.addProperty("message","Done");
            response.add("data",data);
            response.add("state",target.state());

        } else {
            response.addProperty("result","OK");
            JsonObject data = new JsonObject();
            data.addProperty("message","Obstructed");
            response.add("data",data);
            response.add("state",target.state());
        }

        return response;
    }
    public BackwardCommand(JsonArray args) {
        super(args);
    }
    /**
     * Constructs a BackwardCommand with the specified arguments.
     * @param args the arguments for the command, expected to contain the number of steps to move backward.
     */
}